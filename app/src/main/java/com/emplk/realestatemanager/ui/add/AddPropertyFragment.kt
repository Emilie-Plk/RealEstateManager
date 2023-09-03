package com.emplk.realestatemanager.ui.add

import androidx.fragment.app.Fragment
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddPropertyFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPropertyFragment : Fragment(R.layout.add_property_fragment) {

    private val binding by viewBinding { AddPropertyFragmentBinding.bind(it) }

    companion object {
        fun newInstance() = AddPropertyFragment()

    }

    override fun onResume() {
        super.onResume()

    }
}

