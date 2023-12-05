package com.emplk.realestatemanager.ui.main.empty_detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.emplk.realestatemanager.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmptyDetailFragment : Fragment(R.layout.detail_empty_fragment) {

    companion object {
        fun newInstance() = EmptyDetailFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}