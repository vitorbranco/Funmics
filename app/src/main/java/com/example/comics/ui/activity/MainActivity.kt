package com.example.comics.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.comics.R
import com.example.comics.databinding.ActivityMainBinding
import com.example.comics.ui.fragment.ComicsHomeFragment

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ComicsHomeFragment())
                .commit()
        }
    }
}