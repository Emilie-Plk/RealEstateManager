package com.emplk.realestatemanager.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.MainActivityBinding
import com.emplk.realestatemanager.ui.add.AddPropertyFragment
import com.emplk.realestatemanager.ui.blank.BlankFragment
import com.emplk.realestatemanager.ui.property_list.PropertiesFragment
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding { MainActivityBinding.inflate(it) }
    private val viewModel by viewModels<MainViewModel>()

    companion object {
        fun newIntent(requireContext: Context): Intent = Intent(requireContext, MainActivity::class.java)
    }

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

                is MainViewEvent.DisplayPropertyListFragment -> {
                    Log.d("COUCOU MainActivity", "DisplayPropertiesFragment: ")
                    supportFragmentManager.commitNow {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            PropertiesFragment()
                        )
                    }
                }

                is MainViewEvent.DisplayAddPropertyFragmentOnPhone -> {
                    Log.d("COUCOU MainActivity", "DisplayAddPropertyFragment: ")
                    supportFragmentManager.commitNow {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            AddPropertyFragment()
                        )
                    }
                }

                is MainViewEvent.DisplayAddPropertyFragmentOnTablet -> {
                    Log.d("COUCOU MainActivity", "DisplayAddPropertyFragment: ")
                    supportFragmentManager.commitNow {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            BlankFragment()
                        )
                    }
                    supportFragmentManager.commitNow {
                        binding.mainFrameLayoutContainerDetail?.let {
                            replace(
                                it.id,
                                AddPropertyFragment()
                            )
                        }
                    }
                }

                is MainViewEvent.DisplayEditPropertyFragment -> {
                    Log.d("COUCOU MainActivity", "DisplayEditPropertyFragment: ")
                    supportFragmentManager.commitNow {
                        binding.mainFrameLayoutContainerDetail?.let {
                            replace(
                                it.id,
                                AddPropertyFragment()
                            )
                        }
                    }
                }

                is MainViewEvent.DisplayBlankFragment -> {
                    Log.d("COUCOU MainActivity", "DisplayBlankFragment: ")
                    supportFragmentManager.commitNow {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            BlankFragment()
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume(resources.getBoolean(R.bool.isTablet))
    }
}