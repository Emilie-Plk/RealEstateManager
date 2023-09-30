package com.emplk.realestatemanager.ui.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddPropertyFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPropertyFragment : Fragment(R.layout.add_property_fragment) {
    private val binding by viewBinding { AddPropertyFragmentBinding.bind(it) }

    companion object {
        fun newInstance() = EditPropertyFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addPropertySoldStatusTv.isVisible = true
        binding.addPropertySoldStatusSwitch.isVisible = true
    }
}