package com.emplk.realestatemanager.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.MainActivityBinding
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.ui.blank.BlankActivity
import com.emplk.realestatemanager.ui.detail.DetailFragment
import com.emplk.realestatemanager.ui.property_list.PropertiesFragment
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        fun navigate(context: Context, fragmentTag: String): Intent {  // TODO maybe add stringExtra for fragment type
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_TAG, fragmentTag)
            context.startActivity(intent)
            return intent
        }

        private const val KEY_FRAGMENT_TAG = "KEY_FRAGMENT_TAG"
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
        val fragmentName = intent.getStringExtra(KEY_FRAGMENT_TAG)

        if (savedInstanceState == null) {
            if (fragmentName != null) viewModel.onNavigationChanged(fragmentName)
            else
                supportFragmentManager.commit {
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
            binding.mainToolbar.menu.findItem(R.id.main_menu_form)?.let {
                it.isVisible = mainViewState.isAddAppBarButtonVisible
            }
        }

        viewModel.viewEventLiveData.observeEvent(this) { event ->
            when (event) {
                MainViewEvent.DisplayPropertyList -> {
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            PropertiesFragment.newInstance(),
                            PROPERTIES_FRAGMENT_TAG
                        ).addToBackStack(PROPERTIES_FRAGMENT_TAG)
                    }
                }

                MainViewEvent.DisplayDetailFragmentOnPhone -> {
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            DetailFragment.newInstance(),
                            DETAIL_FRAGMENT_TAG
                        ).addToBackStack(DETAIL_FRAGMENT_TAG)
                    }
                }

                MainViewEvent.DisplayDetailFragmentOnTablet -> {
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            PropertiesFragment.newInstance(),
                            PROPERTIES_FRAGMENT_TAG
                        )
                    }

                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerDetail?.id?.let {
                            replace(
                                it,
                                DetailFragment.newInstance(),
                                DETAIL_FRAGMENT_TAG
                            )
                        }
                    }
                }

                MainViewEvent.DisplayFilterPropertiesFragmentOnPhone -> {
                    TODO()
                }

                MainViewEvent.DisplayFilterPropertiesFragmentOnTablet -> {
                    TODO()
                }

                MainViewEvent.NavigateToBlank(NavigationFragmentType.ADD_FRAGMENT.name) -> {
                    startActivity(BlankActivity.navigate(this, NavigationFragmentType.ADD_FRAGMENT.name))
                }

                MainViewEvent.NavigateToBlank(NavigationFragmentType.EDIT_FRAGMENT.name) -> {
                    startActivity(BlankActivity.navigate(this, NavigationFragmentType.EDIT_FRAGMENT.name))
                }

                MainViewEvent.NavigateToBlank(NavigationFragmentType.MAP_FRAGMENT.name) -> {
                    startActivity(BlankActivity.navigate(this, NavigationFragmentType.MAP_FRAGMENT.name))
                }

                is MainViewEvent.NavigateToBlank -> TODO()
            }
        }
    }

    private fun getNavigationType(fragmentName: String): NavigationFragmentType =
        NavigationFragmentType.valueOf(fragmentName)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_map_view -> {
                viewModel.onMapClicked()
                true
            }

            R.id.main_menu_property_filter -> {
                viewModel.onFilterPropertiesClicked()
                true
            }

            R.id.main_menu_form -> {
                viewModel.onAddPropertyClicked()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume(resources.getBoolean(R.bool.isTablet))
    }
}