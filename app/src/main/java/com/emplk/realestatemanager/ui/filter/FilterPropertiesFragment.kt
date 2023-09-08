package com.emplk.realestatemanager.ui.filter

import androidx.fragment.app.Fragment
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.FilterPropertiesFragmentBinding

import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterPropertiesFragment : Fragment(R.layout.filter_properties_fragment) {

private val binding by viewBinding { FilterPropertiesFragmentBinding.bind(it) }

    companion object {
        fun newInstance() = FilterPropertiesFragment()
    }
}