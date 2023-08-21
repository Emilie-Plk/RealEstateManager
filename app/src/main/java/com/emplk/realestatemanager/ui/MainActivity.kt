package com.emplk.realestatemanager.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emplk.realestatemanager.databinding.MainActivityBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding { MainActivityBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}