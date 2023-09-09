package com.emplk.realestatemanager.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.MainActivityBinding
import com.emplk.realestatemanager.ui.add.AddPropertyFragment
import com.emplk.realestatemanager.ui.blank.BlankFragment
import com.emplk.realestatemanager.ui.detail.DetailFragment
import com.emplk.realestatemanager.ui.edit.EditPropertyFragment
import com.emplk.realestatemanager.ui.filter.FilterPropertiesFragment
import com.emplk.realestatemanager.ui.property_list.PropertiesFragment
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding { MainActivityBinding.inflate(it) }
    private val viewModel by viewModels<MainViewModel>()

    companion object {
        fun newIntent(requireContext: Context) = Intent(requireContext, MainActivity::class.java)

        private const val PROPERTIES_FRAGMENT_TAG = "PROPERTIES_FRAGMENT_TAG"
        private const val DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT_TAG"
        private const val ADD_FRAGMENT_TAG = "ADD_FRAGMENT_TAG"
        private const val EDIT_FRAGMENT_TAG = "EDIT_FRAGMENT_TAG"
        private const val FILTER_FRAGMENT_TAG = "FILTER_FRAGMENT_TAG"
        private const val BLANK_FRAGMENT_TAG = "BLANK_FRAGMENT_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setSupportActionBar(binding.mainToolbar)

        updateToolbar()

        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                Log.d("COUCOU MainActivity", "supportFragmentManager onCreate: ")
                replace(
                    binding.mainFrameLayoutContainerProperties.id,
                    PropertiesFragment.newInstance(),
                    PROPERTIES_FRAGMENT_TAG
                )
            }
        }

        binding.mainAddPropertyFab?.setOnClickListener {
            viewModel.onAddPropertyClicked()
        }

        viewModel.isFabVisibleLiveData.observe(this) { isVisible ->
            binding.mainAddPropertyFab?.isVisible = isVisible
        }

        viewModel.viewEventLiveData.observeEvent(this) { event ->
            when (event) {
                MainViewEvent.DisplayPropertyListFragmentOnPhone -> {
                    val existingFragment =
                        supportFragmentManager.findFragmentByTag(PROPERTIES_FRAGMENT_TAG)
                    if (existingFragment == null) {
                        Log.d("COUCOU MainActivity", "DisplayPropertyListFragmentOnPhone: ")
                        supportFragmentManager.commit {
                            replace(
                                binding.mainFrameLayoutContainerProperties.id,
                                PropertiesFragment.newInstance(),
                                PROPERTIES_FRAGMENT_TAG
                            )
                        }
                    }
                }

                MainViewEvent.DisplayPropertyListFragmentOnTablet -> {
                    Log.d("COUCOU MainActivity", "DisplayPropertyListFragmentOnTablet: ")
                    val existingFragment =
                        supportFragmentManager.findFragmentByTag(PROPERTIES_FRAGMENT_TAG)
                    if (existingFragment == null) {
                        supportFragmentManager.commit {
                            replace(
                                binding.mainFrameLayoutContainerProperties.id,
                                PropertiesFragment.newInstance(),
                                PROPERTIES_FRAGMENT_TAG
                            )
                        }
                    }

                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerDetail?.let {
                            replace(
                                it.id,
                                BlankFragment.newInstance(),
                                BLANK_FRAGMENT_TAG
                            )
                        }
                    }
                }

                MainViewEvent.DisplayAddPropertyFragmentOnPhone -> {
                    Log.d("COUCOU MainActivity", "DisplayAddPropertyFragmentOnPhone: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            AddPropertyFragment.newInstance(),
                            ADD_FRAGMENT_TAG
                        )
                    }
                }

                MainViewEvent.DisplayAddPropertyFragmentOnTablet -> {
                    Log.d("COUCOU MainActivity", "DisplayAddPropertyFragmentOnTablet: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            BlankFragment.newInstance(),
                            BLANK_FRAGMENT_TAG
                        )
                    }
                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerDetail?.let {
                            replace(
                                it.id,
                                AddPropertyFragment.newInstance(),
                                ADD_FRAGMENT_TAG
                            )
                        }
                    }
                }

                MainViewEvent.DisplayBlankFragment -> {
                    Log.d("COUCOU MainActivity", "DisplayBlankFragment: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            BlankFragment.newInstance(),
                            BLANK_FRAGMENT_TAG
                        )
                    }
                }


                MainViewEvent.DisplayDetailFragmentOnPhone -> {
                    Log.d("COUCOU MainActivity", "DisplayDetailFragmentOnPhone: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            DetailFragment.newInstance(),
                            DETAIL_FRAGMENT_TAG
                        )
                    }
                }

                MainViewEvent.DisplayDetailFragmentOnTablet -> {
                    Log.d("COUCOU MainActivity", "DisplayDetailFragmentOnTablet: ")
                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerDetail?.let {
                            replace(
                                it.id,
                                DetailFragment.newInstance(),
                                DETAIL_FRAGMENT_TAG
                            )
                        }
                        val existingFragment =
                            supportFragmentManager.findFragmentByTag(PROPERTIES_FRAGMENT_TAG)
                        if (existingFragment == null) {
                            replace(
                                binding.mainFrameLayoutContainerProperties.id,
                                PropertiesFragment.newInstance(),
                                PROPERTIES_FRAGMENT_TAG
                            )
                        }
                    }
                }


                MainViewEvent.DisplayEditPropertyFragmentOnPhone -> {
                    Log.d("COUCOU MainActivity", "DisplayEditPropertyFragmentOnPhone: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            EditPropertyFragment.newInstance(),
                            EDIT_FRAGMENT_TAG
                        )
                    }
                }

                MainViewEvent.DisplayEditPropertyFragmentOnTablet -> {
                    Log.d("COUCOU MainActivity", "DisplayEditPropertyFragmentOnTablet: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            BlankFragment.newInstance(),
                            BLANK_FRAGMENT_TAG
                        )
                    }
                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerDetail?.let {
                            replace(
                                it.id,
                                EditPropertyFragment.newInstance(),
                                EDIT_FRAGMENT_TAG
                            )
                        }
                    }
                }

                MainViewEvent.DisplayFilterPropertiesFragmentOnPhone -> {
                    Log.d("COUCOU MainActivity", "DisplayFilterPropertiesFragmentOnPhone: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            FilterPropertiesFragment.newInstance(),
                            FILTER_FRAGMENT_TAG
                        )
                    }
                }

                MainViewEvent.DisplayFilterPropertiesFragmentOnTablet -> {
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            PropertiesFragment.newInstance(),
                            PROPERTIES_FRAGMENT_TAG
                        )
                    }
                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerDetail?.let {
                            replace(
                                it.id,
                                FilterPropertiesFragment.newInstance(),
                                FILTER_FRAGMENT_TAG
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateToolbar() {
        viewModel.toolbarSubtitleLiveData.observe(this) {
            Log.d("COUCOU kiki", "updateToolbar: $it ")
            binding.mainToolbar.subtitle = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_map_view -> {
                Log.d("COUCOU MainActivity", "DisplayMapFragment: ")
                true
            }

            R.id.main_menu_property_filter -> {
                Log.d("COUCOU MainActivity", "DisplayFilterFragment: ")
                viewModel.onFilterPropertiesClicked()
                true
            }

            R.id.main_menu_add_property -> {
                viewModel.onAddPropertyClicked()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.onResume(resources.getBoolean(R.bool.isTablet))
    }
}