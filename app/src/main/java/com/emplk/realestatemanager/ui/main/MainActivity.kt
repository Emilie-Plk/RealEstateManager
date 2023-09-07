package com.emplk.realestatemanager.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.MainActivityBinding
import com.emplk.realestatemanager.ui.add.AddPropertyFragment
import com.emplk.realestatemanager.ui.blank.BlankFragment
import com.emplk.realestatemanager.ui.detail.DetailActivity
import com.emplk.realestatemanager.ui.detail.DetailFragment
import com.emplk.realestatemanager.ui.edit.EditPropertyFragment
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setupToolbar()
        setupMenu()

        binding.mainAddPropertyFab?.setOnClickListener {
            viewModel.onAddPropertyClicked()
        }

        viewModel.isFabVisibleLiveData.observe(this) { isVisible ->
            binding.mainAddPropertyFab?.isVisible = isVisible
        }

        viewModel.viewEventLiveData.observeEvent(this) { event ->
            when (event) {
                MainViewEvent.DisplayPropertyListFragmentOnPhone -> {
                    Log.d("COUCOU MainActivity", "DisplayPropertyListFragmentOnPhone: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            PropertiesFragment.newInstance()
                        )
                    }
                }

                MainViewEvent.DisplayPropertyListFragmentOnTablet -> {
                    Log.d("COUCOU MainActivity", "DisplayPropertyListFragmentOnTablet: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            PropertiesFragment.newInstance()
                        )
                    }

                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerDetail?.let {
                            replace(
                                it.id,
                                BlankFragment.newInstance()
                            )
                        }
                    }
                }

                MainViewEvent.DisplayAddPropertyFragmentOnPhone -> {
                    Log.d("COUCOU MainActivity", "DisplayAddPropertyFragmentOnPhone: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            AddPropertyFragment.newInstance()
                        ).addToBackStack(null)
                    }
                }

                MainViewEvent.DisplayAddPropertyFragmentOnTablet -> {
                    Log.d("COUCOU MainActivity", "DisplayAddPropertyFragmentOnTablet: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            BlankFragment.newInstance()
                        ).addToBackStack(null)
                    }
                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerDetail?.let {
                            replace(
                                it.id,
                                AddPropertyFragment.newInstance()
                            ).addToBackStack(null)
                        }
                    }
                }

                MainViewEvent.DisplayBlankFragment -> {
                    Log.d("COUCOU MainActivity", "DisplayBlankFragment: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            BlankFragment.newInstance()
                        )
                    }
                }

                MainViewEvent.DisplayDetailFragment -> {
                    Log.d("COUCOU MainActivity", "DisplayDetailFragment: ")
                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerDetail?.let {
                            replace(
                                it.id,
                                DetailFragment.newInstance()
                            ).addToBackStack(null)
                        }
                        binding.mainFrameLayoutContainerProperties.id.let {
                            replace(
                                it,
                                PropertiesFragment.newInstance()
                            )
                        }
                    }
                }

                MainViewEvent.StartDetailActivity -> {
                    Log.d("COUCOU MainActivity", "StartDetailActivity: ")
                    startActivity(DetailActivity.newIntent(this))
                }

                MainViewEvent.DisplayEditPropertyFragmentOnPhone -> {
                    Log.d("COUCOU MainActivity", "DisplayEditPropertyFragmentOnPhone: ")
                    supportFragmentManager.commit {
                        replace(
                            binding.mainFrameLayoutContainerProperties.id,
                            EditPropertyFragment.newInstance()
                        )
                    }
                }

                MainViewEvent.DisplayEditPropertyFragmentOnTablet -> {
                    Log.d("COUCOU MainActivity", "DisplayEditPropertyFragmentOnTablet: ")
                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerProperties.id.let {
                            replace(
                                it,
                                BlankFragment.newInstance()
                            )
                        }
                    }
                    supportFragmentManager.commit {
                        binding.mainFrameLayoutContainerDetail?.let {
                            replace(
                                it.id,
                                EditPropertyFragment.newInstance()
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_map_view -> {
                Log.d("COUCOU MainActivity", "DisplayMapFragment: ")
                true
            }

            R.id.main_menu_property_filter -> {
                Log.d("COUCOU MainActivity", "DisplayFilterFragment: ")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.mainToolbar
        toolbar.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
    }

    private fun setupMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
                R.id.main_menu_property_filter -> {
                    Log.d("COUCOU MainActivity", "DisplayFilterFragment: ")
                    true
                }

                R.id.main_menu_map_view -> {
                    Log.d("COUCOU MainActivity", "DisplayMapFragment: ")
                    true
                }

                R.id.main_menu_add_property -> {
                    viewModel.onAddPropertyClicked()
                    true
                }

                else -> false
            }
        })
    }


    override fun onResume() {
        super.onResume()
        viewModel.onResume(resources.getBoolean(R.bool.isTablet))
    }
}