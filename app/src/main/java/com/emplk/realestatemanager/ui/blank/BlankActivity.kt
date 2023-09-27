package com.emplk.realestatemanager.ui.blank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.BlankActivityBinding
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.ui.add.AddPropertyFragment
import com.emplk.realestatemanager.ui.add.draft_dialog.PropertyDraftDialogFragment
import com.emplk.realestatemanager.ui.edit.EditPropertyFragment
import com.emplk.realestatemanager.ui.main.MainActivity
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlankActivity : AppCompatActivity() {

    companion object {
        fun navigate(context: Context, fragmentTag: String): Intent {
            val intent = Intent(context, BlankActivity::class.java)
            intent.putExtra(KEY_FRAGMENT_TAG, fragmentTag)
            return intent
        }

        private const val KEY_FRAGMENT_TAG = "KEY_FRAGMENT_TAG"
    }

    private val binding by viewBinding { BlankActivityBinding.inflate(it) }
    private val viewModel by viewModels<BlankActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.blankToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        onBackPress()

        val fragmentTag = intent.getStringExtra(KEY_FRAGMENT_TAG)

        if (savedInstanceState == null) {
            if (fragmentTag == NavigationFragmentType.EDIT_FRAGMENT.name) {
                supportFragmentManager.commitNow {
                    add(
                        binding.blankFrameLayoutContainer.id,
                        EditPropertyFragment.newInstance()
                    )
                }
            } else if (fragmentTag == NavigationFragmentType.ADD_FRAGMENT.name) {
                supportFragmentManager.commitNow {
                    add(
                        binding.blankFrameLayoutContainer.id,
                        AddPropertyFragment.newInstance()
                    )
                }
            }
        }

        viewModel.viewEventLiveData.observeEvent(this) { blankViewEvent ->
            when (blankViewEvent) {
                is BlankViewEvent.NavigateToMain -> {
                    MainActivity.navigate(this)
                    finish()
                }

                BlankViewEvent.DisplayDraftDialog ->
                    PropertyDraftDialogFragment.newInstance().show(supportFragmentManager, null)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                viewModel.onBackClicked()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //   viewModel.onBackClicked()
                val backStackCount = supportFragmentManager.backStackEntryCount
                if (backStackCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        })
    }
}
