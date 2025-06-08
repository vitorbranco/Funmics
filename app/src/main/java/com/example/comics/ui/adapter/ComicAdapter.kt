package com.example.comics.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comics.databinding.ComicItemBinding
import com.example.comics.ui.ComicItemVO

class ComicAdapter : ListAdapter<ComicItemVO, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ComicViewHolder).bind(item = getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        getItemHolder(parent = parent)

    private fun getItemHolder(parent: ViewGroup) = ComicViewHolder(
        comicItemBinding = ComicItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    private class ComicViewHolder(val comicItemBinding: ComicItemBinding) :
        RecyclerView.ViewHolder(comicItemBinding.root) {

        fun bind(
            item: ComicItemVO,
        ) = with(comicItemBinding) {

            Glide.with(comicItemBinding.root)
                .load(item.image)
                .centerCrop()
                .into(comicItemBinding.comicImage)

            comicTitle.text = item.title
            comicSubtitle.text = item.subtitle
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ComicItemVO>() {
            override fun areItemsTheSame(oldItem: ComicItemVO, newItem: ComicItemVO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ComicItemVO, newItem: ComicItemVO): Boolean {
                return oldItem == newItem
            }
        }
    }
}
