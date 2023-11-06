package com.emplk.realestatemanager.ui.filter

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.FilterPropertiesFragmentBinding

import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterPropertiesFragment : DialogFragment(R.layout.filter_properties_fragment) {

    private val binding by viewBinding { FilterPropertiesFragmentBinding.bind(it) }

    companion object {
        fun newInstance() = FilterPropertiesFragment()
    }

// I want to set width and height to fit almost all screen
    override fun onResume() {
        super.onResume()
        val width = (Resources.getSystem().displayMetrics.widthPixels * 0.98).toInt()
     // for height I just want to wrap content
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

}