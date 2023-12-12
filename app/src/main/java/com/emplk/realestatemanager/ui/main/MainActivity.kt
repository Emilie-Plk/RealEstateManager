package com.emplk.realestatemanager.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.MainActivityBinding
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.ui.blank.BlankActivity
import com.emplk.realestatemanager.ui.detail.DetailFragment
import com.emplk.realestatemanager.ui.filter.FilterPropertiesFragment
import com.emplk.realestatemanager.ui.list.PropertiesFragment
import com.emplk.realestatemanager.ui.loan_simulator.LoanSimulatorFragment
import com.emplk.realestatemanager.ui.main.empty_detail.EmptyDetailFragment
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        fun navigate(context: Context, fragmentTag: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_TAG, fragmentTag)
            context.startActivity(intent)
            return intent
        }

        private const val KEY_FRAGMENT_TAG = "KEY_FRAGMENT_TAG"
        private const val PROPERTIES_FRAGMENT_TAG = "PROPERTIES_FRAGMENT_TAG"
        private const val DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT_TAG"
        private const val FILTER_FRAGMENT_TAG = "FILTER_FRAGMENT_TAG"
        private const val LOAN_SIM_BOTTOM_SHEET_FRAGMENT = "LOAN_SIM_BOTTOM_SHEET_FRAGMENT"
    }

    private val binding by viewBinding { MainActivityBinding.inflate(it) }
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setSupportActionBar(binding.mainToolbar)
        onBackPress()

        val fragmentName = intent.getStringExtra(KEY_FRAGMENT_TAG)
        if (savedInstanceState == null) {
            if (fragmentName != null) {
                when (fragmentName) {
                    NavigationFragmentType.DETAIL_FRAGMENT.name -> {
                        supportFragmentManager.commit {
                            add(
                                binding.mainFrameLayoutContainerProperties.id,
                                DetailFragment.newInstance(),
                                DETAIL_FRAGMENT_TAG
                            )
                        }
                    }

                    NavigationFragmentType.LIST_FRAGMENT.name -> {
                        supportFragmentManager.commit {
                            add(
                                binding.mainFrameLayoutContainerProperties.id,
                                PropertiesFragment.newInstance(),
                                PROPERTIES_FRAGMENT_TAG
                            )
                        }

                        supportFragmentManager.commit {
                            binding.mainFrameLayoutContainerDetail?.id?.let {
                                replace(
                                    it,
                                    EmptyDetailFragment.newInstance(),
                                    null
                                )
                            }
                        }
                    }
                }
            } else {
                binding.mainToolbar.menu.findItem(R.id.main_menu_property_reset_filter)?.let {
                    it.isVisible = false
                }
                supportFragmentManager.commit {
                    add(
                        binding.mainFrameLayoutContainerProperties.id,
                        PropertiesFragment.newInstance(),
                        PROPERTIES_FRAGMENT_TAG
                    )
                }

                supportFragmentManager.commit {
                    binding.mainFrameLayoutContainerDetail?.id?.let {
                        replace(
                            it,
                            EmptyDetailFragment.newInstance(),
                            null
                        )
                    }
                }
            }
        }

        binding.mainAddPropertyFab?.setOnClickListener {
            viewModel.onAddPropertyClicked()
        }

        viewModel.mainViewState.observe(this) { mainViewState ->
            binding.mainToolbar.subtitle = mainViewState.subtitle
            binding.mainAddPropertyFab?.isVisible = mainViewState.isAddFabVisible
            binding.mainToolbar.menu.findItem(R.id.main_menu_property_filter)?.isVisible =
                mainViewState.isFilterAppBarButtonVisible

            binding.mainToolbar.menu.findItem(R.id.main_menu_form)?.isVisible = mainViewState.isAddAppBarButtonVisible

            binding.mainToolbar.menu.findItem(R.id.main_menu_property_reset_filter)?.isVisible =
                mainViewState.isResetFilterAppBarButtonVisible
        }

        viewModel.viewEventLiveData.observeEvent(this) { event ->
            when (event) {
                MainViewEvent.PropertyList -> {
                    if (supportFragmentManager.findFragmentByTag(PROPERTIES_FRAGMENT_TAG) == null) {
                        supportFragmentManager.commit {
                            replace(
                                binding.mainFrameLayoutContainerProperties.id,
                                PropertiesFragment.newInstance(),
                                PROPERTIES_FRAGMENT_TAG
                            ).addToBackStack(null)
                        }
                    }
                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerDetail?.id?.let {
                            replace(
                                it,
                                EmptyDetailFragment.newInstance(),
                                null
                            )
                        }
                    }
                }

                is MainViewEvent.DetailOnPhone -> {
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            DetailFragment.newInstance(),
                            DETAIL_FRAGMENT_TAG
                        ).addToBackStack(DETAIL_FRAGMENT_TAG)
                    }
                }

                is MainViewEvent.DetailOnTablet -> {
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

                MainViewEvent.FilterProperties -> {
                    if (supportFragmentManager.findFragmentByTag(FILTER_FRAGMENT_TAG) == null)
                        FilterPropertiesFragment.newInstance().show(supportFragmentManager, FILTER_FRAGMENT_TAG)
                }

                is MainViewEvent.NavigateToBlank -> {
                    startActivity(BlankActivity.navigate(this, event.fragmentTag))
                }

                MainViewEvent.LoanSimulator -> {
                    if (supportFragmentManager.findFragmentByTag(LOAN_SIM_BOTTOM_SHEET_FRAGMENT) == null)
                        LoanSimulatorFragment.newInstance().show(supportFragmentManager, LOAN_SIM_BOTTOM_SHEET_FRAGMENT)
                }

                MainViewEvent.NoEvent -> {}
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            R.id.main_menu_map_view -> {
                viewModel.onMapClicked()
                true
            }

            R.id.main_menu_property_filter -> {
                viewModel.onFilterPropertiesClicked()
                true
            }

            R.id.main_menu_property_reset_filter -> {
                viewModel.onResetFiltersClicked()
                true
            }

            R.id.main_menu_form -> {
                viewModel.onAddPropertyClicked()
                true
            }

            R.id.main_menu_loan_simulator -> {
                viewModel.onLoanSimulatorClicked()
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.findFragmentByTag(DETAIL_FRAGMENT_TAG) != null) {
                viewModel.onBackClicked()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val resetFilterItem = menu?.findItem(R.id.main_menu_property_reset_filter)
        resetFilterItem?.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    private fun hideKeyboard(view: View?) {
        if (view != null) {
            val inputMethodManager =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (currentFocus != null) {
            hideKeyboard(currentFocus)
            currentFocus!!.clearFocus()
        }
        return super.dispatchTouchEvent(event)
    }

    fun setFilterAppBarButtonVisibility(isVisible: Boolean) {
        binding.mainToolbar.menu.findItem(R.id.main_menu_property_filter)?.isVisible = isVisible
    }
}