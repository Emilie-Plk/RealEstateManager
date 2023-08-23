package com.emplk.realestatemanager.ui.property_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.PropertiesFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PropertiesFragment : Fragment(R.layout.properties_fragment) {
    // TODO: Rename and change types of parameters

    private val binding by viewBinding { PropertiesFragmentBinding.bind(it) }
    private val viewModel by viewModels<PropertyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}