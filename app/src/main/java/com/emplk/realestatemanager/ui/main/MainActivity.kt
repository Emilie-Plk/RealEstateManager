package com.emplk.realestatemanager.ui.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.MainActivityBinding
import com.emplk.realestatemanager.ui.add.AddPropertyFragment
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

    private companion object {
        private const val PROPERTIES_FRAGMENT_TAG = "PROPERTIES_FRAGMENT_TAG"
        private const val DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT_TAG"
        private const val ADD_FRAGMENT_TAG = "ADD_FRAGMENT_TAG"
        private const val EDIT_FRAGMENT_TAG = "EDIT_FRAGMENT_TAG"
        private const val FILTER_FRAGMENT_TAG = "FILTER_FRAGMENT_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setSupportActionBar(binding.mainToolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                Log.d("COUCOU MainActivity", "supportFragmentManager onCreate: ")
                add(
                    binding.mainFrameLayoutContainerProperties.id,
                    PropertiesFragment.newInstance(),
                    PROPERTIES_FRAGMENT_TAG
                ).addToBackStack(PROPERTIES_FRAGMENT_TAG)
            }
        }

        binding.mainAddPropertyFab?.setOnClickListener {
            viewModel.onAddPropertyClicked()
        }

        viewModel.mainViewState.observe(this) { mainViewState ->
            binding.mainToolbar.subtitle = mainViewState.subtitle
            binding.mainAddPropertyFab?.isVisible = mainViewState.isAddFabVisible
            binding.mainToolbar.menu.findItem(R.id.main_menu_property_filter)?.let {
                it.isVisible = mainViewState.isFilterAppBarButtonVisible
            }
            binding.mainToolbar.menu.findItem(R.id.main_menu_add_property)?.let {
                it.isVisible = mainViewState.isAddAppBarButtonVisible
            }
        }

        viewModel.viewEventLiveData.observeEvent(this) { event ->
            when (event) {
                MainViewEvent.DisplayPropertyListFragmentOnPhone -> {
                        Log.d("COUCOU MainActivity", "existing frag is null ! DisplayPropertyListFragmentOnPhone: ")
                        supportFragmentManager.commit {
                            replace(
                                binding.mainFrameLayoutContainerProperties.id,
                                PropertiesFragment.newInstance(),
                                PROPERTIES_FRAGMENT_TAG
                            ).addToBackStack(PROPERTIES_FRAGMENT_TAG)
                    }
                }

                MainViewEvent.DisplayPropertyListFragmentOnTablet -> {
                    val existingPropertiesFragment =
                        supportFragmentManager.findFragmentByTag(PROPERTIES_FRAGMENT_TAG)
                    if (existingPropertiesFragment == null) {
                        Log.d("COUCOU MainActivity", "existing frag is null ! DisplayPropertyListFragmentOnTablet: ")
                        supportFragmentManager.commit {
                            replace(
                                binding.mainFrameLayoutContainerProperties.id,
                                PropertiesFragment.newInstance(),
                                PROPERTIES_FRAGMENT_TAG
                            ).addToBackStack(PROPERTIES_FRAGMENT_TAG)
                        }
                    }
                }

                MainViewEvent.DisplayAddPropertyFragmentOnPhone -> {
                    val existingAddFragment = supportFragmentManager.findFragmentByTag(ADD_FRAGMENT_TAG)
                    if (existingAddFragment == null) {
                        supportFragmentManager.commit {
                            replace(
                                R.id.main_FrameLayout_container_properties,
                                AddPropertyFragment.newInstance(),
                                ADD_FRAGMENT_TAG
                            ).addToBackStack(ADD_FRAGMENT_TAG)
                        }
                    }
                }

                MainViewEvent.DisplayAddPropertyFragmentOnTablet -> {
                    binding.mainViewSeparator?.isVisible = false
                    val existingAddFragment = supportFragmentManager.findFragmentByTag(ADD_FRAGMENT_TAG)
                    if (existingAddFragment == null) {
                        adjustConstraintSet()
                        supportFragmentManager.commit {
                            replace(
                                R.id.main_FrameLayout_container_properties,
                                AddPropertyFragment.newInstance(),
                                ADD_FRAGMENT_TAG
                            ).addToBackStack(ADD_FRAGMENT_TAG)
                        }
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
                    resetConstraintSet()
                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerDetail?.let {
                            replace(
                                it.id,
                                DetailFragment.newInstance(),
                                DETAIL_FRAGMENT_TAG
                            )
                        }

                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            PropertiesFragment.newInstance(),
                            PROPERTIES_FRAGMENT_TAG
                        )
                    }
                }

                MainViewEvent.DisplayEditPropertyFragmentOnPhone -> {
                    val existingEditFragment = supportFragmentManager.findFragmentByTag(EDIT_FRAGMENT_TAG)
                    if (existingEditFragment == null) {
                        Log.d("COUCOU MainActivity", "existing frag is null ! DisplayEditPropertyFragmentOnPhone: ")
                        supportFragmentManager.commit {
                            replace(
                                binding.mainFrameLayoutContainerProperties.id,
                                EditPropertyFragment.newInstance(),
                                EDIT_FRAGMENT_TAG
                            ).addToBackStack(EDIT_FRAGMENT_TAG)
                        }
                    }
                }

                MainViewEvent.DisplayEditPropertyFragmentOnTablet -> {
                    val detailFragment = supportFragmentManager.findFragmentByTag(DETAIL_FRAGMENT_TAG)
                    if (detailFragment != null) {
                        Log.d("COUCOU MainActivity", "Detail  frag is null  ! DisplayEditPropertyFragmentOnTablet: ")
                        supportFragmentManager.commit {
                            remove(detailFragment)
                        }
                    }

                    supportFragmentManager.commit {
                        Log.d(
                            "COUCOU MainActivity",
                            "Replace Edit Fg + adjust constraints DisplayEditPropertyFragmentOnTablet: "
                        )
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            EditPropertyFragment.newInstance(),
                            EDIT_FRAGMENT_TAG
                        )
                        adjustConstraintSet()
                    }
                }

                MainViewEvent.DisplayFilterPropertiesFragmentOnPhone -> {
                    val existingFilterFragment = supportFragmentManager.findFragmentByTag(FILTER_FRAGMENT_TAG)
                    if (existingFilterFragment == null) {
                        Log.d(
                            "COUCOU MainActivity",
                            "Existing filter fragment is null:  DisplayFilterPropertiesFragmentOnPhone: "
                        )
                        supportFragmentManager.commit {
                            replace(
                                binding.mainFrameLayoutContainerProperties.id,
                                FilterPropertiesFragment.newInstance(),
                                FILTER_FRAGMENT_TAG
                            )
                        }
                    }
                }

                MainViewEvent.DisplayFilterPropertiesFragmentOnTablet -> {
                    resetConstraintSet()
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_map_view -> {
                true
            }

            R.id.main_menu_property_filter -> {
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

    private fun adjustConstraintSet() {
        val constraintSet = ConstraintSet()
        constraintSet.connect(
            R.id.main_FrameLayout_container_properties,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        binding.mainViewSeparator?.isVisible = false
    }

    private fun resetConstraintSet() {
        val constraintSet = ConstraintSet()
        constraintSet.connect(
            R.id.main_FrameLayout_container_properties,
            ConstraintSet.END,
            R.id.main_View_separator,
            ConstraintSet.START
        )
        binding.mainViewSeparator?.isVisible = true
    }


    override fun onResume() {
        super.onResume()
        val backStackCount = supportFragmentManager.backStackEntryCount
        for (i in 0 until backStackCount) {
            val entry = supportFragmentManager.getBackStackEntryAt(i)
            Log.d("BackStackEntry $i", "Name: ${entry.name}, ID: ${entry.id}")
        }
        viewModel.onResume(resources.getBoolean(R.bool.isTablet))
    }
}