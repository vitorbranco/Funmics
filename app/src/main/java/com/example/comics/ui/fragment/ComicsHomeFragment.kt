package com.example.comics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comics.databinding.FragmentComicsHomeBinding
import com.example.comics.ui.adapter.ComicAdapter
import com.example.comics.ui.viewmodel.ComicsHomeViewModel
import com.example.comics.ui.viewmodel.ComicsViewState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComicsHomeFragment : Fragment() {

    private var _binding: FragmentComicsHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var comicAdapter: ComicAdapter

    private val viewModel: ComicsHomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return _binding?.root ?: FragmentComicsHomeBinding.inflate(inflater, container, false)
            .also {
                _binding = it
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setCollectors()
        setupSwipeRefresh()
        getComics()
    }

    private fun setupRecyclerView() {
        binding.apply {
            comicAdapter = ComicAdapter()
            comicsListRecyclerView.adapter = comicAdapter
            comicsListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.comicsViewState.collect(::validateComics)
                }
            }
        }
    }

    private fun validateComics(state: ComicsViewState) = with(binding) {
        when (state) {
            is ComicsViewState.Loading -> {
                errorTextView.isGone = true
                swipeRefresh.isRefreshing = true
                emptyStateConstraintLayout.isGone = true
            }

            is ComicsViewState.EmptyComics -> {
                errorTextView.isGone = true
                swipeRefresh.isRefreshing = false
                emptyStateConstraintLayout.isVisible = true
            }

            is ComicsViewState.UpdatedComics -> {
                errorTextView.isGone = true
                emptyStateConstraintLayout.isGone = true
                swipeRefresh.isRefreshing = false
                comicsListRecyclerView.isVisible = true
                comicAdapter.submitList(state.comics)
                displayUpdatedFeedback()
            }

            is ComicsViewState.Fail -> {
                emptyStateConstraintLayout.isGone = true
                swipeRefresh.isRefreshing = false
                comicsListRecyclerView.isGone = true
                errorTextView.isVisible = true
            }
        }
    }

    private fun setupSwipeRefresh() = with(binding.swipeRefresh) {
        setOnRefreshListener {
            getComics()
        }
    }

    private fun getComics() {
        lifecycle.coroutineScope.launch {
            viewModel.getComics()
        }
    }

    private fun displayUpdatedFeedback() {
        Toast.makeText(requireContext(), UPDATED_MESSAGE, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val UPDATED_MESSAGE = "Lista atualizada"
    }
}