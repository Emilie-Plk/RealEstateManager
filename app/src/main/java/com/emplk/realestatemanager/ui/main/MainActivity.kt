package com.emplk.realestatemanager.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.MainActivityBinding
import com.emplk.realestatemanager.ui.add.AddPropertyFragment
import com.emplk.realestatemanager.ui.edit.EditPropertyFragment
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
            viewModel.onAddPropertyClicked()
        }

        viewModel.viewEventLiveData.observeEvent(this) { event ->
            when (event) {
                is MainViewEvent.DisplayAddPropertyFragment -> {
                    Log.d("COUCOU MainActivity", "DisplayAddPropertyFragment: ")
                    supportFragmentManager.commitNow {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            AddPropertyFragment()
                        )
                    }
                }

                is MainViewEvent.DisplayEditPropertyFragment -> {
                    Log.d("COUCOU MainActivity", "DisplayEditPropertyFragment: ")
                    supportFragmentManager.commitNow {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            AddPropertyFragment()
                        )
                    }
                }

                is MainViewEvent.DidNotClickedAddPropertyButtonPhone -> {
                    Snackbar.make(
                        binding.root,
                        "Phone - Did Not Clicked Add Property Button",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                is MainViewEvent.DidNotClickedAddPropertyButtonTablet -> {
                    Snackbar.make(
                        binding.root,
                        "Tablet - Did Not Clicked Add Property Button",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume(resources.getBoolean(R.bool.isTablet))
    }
}