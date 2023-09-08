package com.emplk.realestatemanager.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.DetailActivityBinding
import com.emplk.realestatemanager.ui.main.MainActivity
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding by viewBinding { DetailActivityBinding.inflate(it) }
    private val viewModel by viewModels<DetailActivityViewModel>()

    companion object {
        fun newIntent(requireContext: Context): Intent = Intent(requireContext, DetailActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.detailToolbar)
       supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.toolbarSubtitleLiveData.observe(this) {
            Log.d("COUCOU kiki", "updateToolbar from Detail: $it ")
            binding.detailToolbar.subtitle = it // returns null
        }

        viewModel.isTabletLiveData.observe(this) { isTablet ->
            if (isTablet) {
                Log.d("COUCOU DetailActivity", "isTablet: ")
                startActivity(MainActivity.newIntent(this))
                finish()
            } else {
                Log.d("COUCOU DetailActivity", "isPhone: ")
                supportFragmentManager.commit {
                    replace(
                        R.id.detail_FrameLayout,
                        DetailFragment.newInstance()
                    )
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                viewModel.onBackPressed()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume(resources.getBoolean(R.bool.isTablet))
    }
}
