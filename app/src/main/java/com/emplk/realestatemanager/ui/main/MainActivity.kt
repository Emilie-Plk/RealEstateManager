package com.emplk.realestatemanager.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.MainActivityBinding
import com.emplk.realestatemanager.ui.property_list.PropertiesFragment
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding { MainActivityBinding.inflate(it) }
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                replace(
                    binding.mainFrameLayoutContainerProperties.id,
                    PropertiesFragment()
                )
            }
        }

        binding.addPropertyFab?.setOnClickListener {
            Log.d("COUCOU", "addPropertyFab clicked")
            viewModel.onAddPropertyClicked()
        }

        viewModel.mainViewEventLiveData.observeEvent(this) { event ->
            when (event) {
                is MainViewEvent.DoNothingForTheMoment -> {
                  Snackbar.make(binding.root, "Do nothing for the moment", Snackbar.LENGTH_SHORT).show()
                    Log.d("COUCOU Main", "Do nothing for the moment")
                }

                is MainViewEvent.NavigateToAddPropertyActivity -> {
                   Snackbar.make(binding.root, "NavigateToAddPropertyActivity", Snackbar.LENGTH_SHORT).show()
                    Log.d("COUCOU Main", "NavigateToAddPropertyActivity")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume(resources.getBoolean(R.bool.isTablet))
    }
}