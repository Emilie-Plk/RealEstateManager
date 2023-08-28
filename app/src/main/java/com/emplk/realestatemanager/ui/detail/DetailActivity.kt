package com.emplk.realestatemanager.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emplk.realestatemanager.databinding.DetailActivityBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by viewBinding { DetailActivityBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.detailFrameLayout.id, DetailFragment())
                .commitNow()
        }
    }

    companion object {
        fun navigate(requireContext: Context): Intent {
            return Intent(requireContext, DetailActivity::class.java)
        }
    }
}
