package com.emplk.realestatemanager.ui.blank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.emplk.realestatemanager.ui.map.MapsFragment
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
        private const val ADD_FRAGMENT_TAG = "ADD_FRAGMENT_TAG"
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
            when (fragmentTag) {
                NavigationFragmentType.EDIT_FRAGMENT.name -> {
                    supportFragmentManager.commitNow {
                        add(
                            binding.blankFrameLayoutContainer.id,
                            EditPropertyFragment.newInstance()
                        )
                    }
                }

                NavigationFragmentType.MAP_FRAGMENT.name -> {
                    supportFragmentManager.commitNow {
                        add(
                            binding.blankFrameLayoutContainer.id,
                            MapsFragment.newInstance()
                        )
                    }
                }

                NavigationFragmentType.ADD_FRAGMENT.name -> {
                    supportFragmentManager.commitNow {
                        add(
                            binding.blankFrameLayoutContainer.id,
                            AddPropertyFragment.newInstance(),
                            ADD_FRAGMENT_TAG
                        )
                    }
                }
            }
        }

        viewModel.viewEventLiveData.observeEvent(this) { blankViewEvent ->
            when (blankViewEvent) {
                is BlankViewEvent.NavigateToMain -> {
                    MainActivity.navigate(this, NavigationFragmentType.LIST_FRAGMENT.name)
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
// if fragment displayed is ADD_FRAGMENT_TAG
                if (supportFragmentManager.findFragmentByTag(ADD_FRAGMENT_TAG) != null) {
                    viewModel.onBackClicked()
                    return
                }
                val backStackCount = supportFragmentManager.backStackEntryCount
                if (backStackCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        })
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
}
