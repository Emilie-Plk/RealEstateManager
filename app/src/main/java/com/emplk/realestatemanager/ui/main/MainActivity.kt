package com.emplk.realestatemanager.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.MainActivityBinding
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.ui.blank.BlankActivity
import com.emplk.realestatemanager.ui.detail.DetailFragment
import com.emplk.realestatemanager.ui.filter.FilterPropertiesFragment
import com.emplk.realestatemanager.ui.property_list.PropertiesFragment
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        fun navigate(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        private const val PROPERTIES_FRAGMENT_TAG = "PROPERTIES_FRAGMENT_TAG"
        private const val DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT_TAG"
        private const val FILTER_FRAGMENT_TAG = "FILTER_FRAGMENT_TAG"
    }


    private val binding by viewBinding { MainActivityBinding.inflate(it) }
    private val viewModel by viewModels<MainViewModel>()


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

        onBackPress()

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
                    displayFragment(
                        binding.mainFrameLayoutContainerProperties.id,
                        PROPERTIES_FRAGMENT_TAG,
                        PropertiesFragment.newInstance()
                    )
                }

                MainViewEvent.DisplayPropertyListFragmentOnTablet -> {
                    displayFragment(
                        binding.mainFrameLayoutContainerProperties.id,
                        PROPERTIES_FRAGMENT_TAG,
                        PropertiesFragment.newInstance()
                    )
                }


                MainViewEvent.DisplayDetailFragmentOnPhone -> {
                    val existingFragment = supportFragmentManager.findFragmentByTag(DETAIL_FRAGMENT_TAG)
                    if (existingFragment != null) {
                        supportFragmentManager.commitNow {
                            remove(existingFragment)
                        }
                    }
                    displayFragment(
                        binding.mainFrameLayoutContainerProperties.id,
                        DETAIL_FRAGMENT_TAG,
                        DetailFragment.newInstance()
                    )
                }

                MainViewEvent.DisplayDetailFragmentOnTablet -> {
                    val existingFragment = supportFragmentManager.findFragmentByTag(DETAIL_FRAGMENT_TAG)
                    if (existingFragment != null) {
                        supportFragmentManager.commitNow {
                            remove(existingFragment)
                        }
                    }

                    binding.mainFrameLayoutContainerDetail?.id?.let {
                        displayFragment(
                            it,
                            DETAIL_FRAGMENT_TAG,
                            DetailFragment.newInstance()
                        )
                    }
                    displayFragment(
                        binding.mainFrameLayoutContainerProperties.id,
                        PROPERTIES_FRAGMENT_TAG,
                        PropertiesFragment.newInstance()
                    )
                }

                MainViewEvent.DisplayFilterPropertiesFragmentOnPhone -> {
                    displayFragment(
                        binding.mainFrameLayoutContainerProperties.id,
                        FILTER_FRAGMENT_TAG,
                        FilterPropertiesFragment.newInstance()
                    )
                }

                MainViewEvent.DisplayFilterPropertiesFragmentOnTablet -> {
                    displayFragment(
                        binding.mainFrameLayoutContainerProperties.id,
                        PROPERTIES_FRAGMENT_TAG,
                        PropertiesFragment.newInstance()
                    )

                    binding.mainFrameLayoutContainerDetail?.id?.let {
                        displayFragment(
                            it,
                            FILTER_FRAGMENT_TAG,
                            FilterPropertiesFragment.newInstance()
                        )
                    }
                }

                MainViewEvent.DisplayPropertyListFragmentWithNoSelectedPropertyOnTablet -> TODO()

                MainViewEvent.NavigateToBlank(NavigationFragmentType.ADD_FRAGMENT.name) -> {
                    startActivity(BlankActivity.navigate(this, NavigationFragmentType.ADD_FRAGMENT.name))
                }


                MainViewEvent.NavigateToBlank(NavigationFragmentType.EDIT_FRAGMENT.name) -> {
                    startActivity(BlankActivity.navigate(this, NavigationFragmentType.EDIT_FRAGMENT.name))
                }

                is MainViewEvent.NavigateToBlank -> TODO()
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

    private fun displayFragment(containerViewId: Int, fragmentTag: String, fragment: Fragment) {
        val existingFragment = supportFragmentManager.findFragmentByTag(fragmentTag)
        val tagList = mutableListOf<String>()

        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            val backStackEntry = supportFragmentManager.getBackStackEntryAt(i)
            val tag = backStackEntry.name ?: "DefaultTag"
            tagList.add(tag)
        }

        if (existingFragment == null) {
            supportFragmentManager.commit {
                replace(
                    containerViewId,
                    fragment,
                    fragmentTag
                ).addToBackStack(fragmentTag)
            }
        } else if (tagList.contains(fragmentTag)) {
            return
        } else {
            supportFragmentManager.commit {
                replace(
                    containerViewId,
                    fragment,
                    fragmentTag
                )
            }
        }
    }

    private fun onBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            val backStackCount = supportFragmentManager.backStackEntryCount
            if (backStackCount > 1) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }
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