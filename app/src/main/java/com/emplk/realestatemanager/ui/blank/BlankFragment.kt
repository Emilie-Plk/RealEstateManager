package com.emplk.realestatemanager.ui.blank

import androidx.fragment.app.Fragment
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.BlankFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlankFragment : Fragment(R.layout.blank_fragment) {
    private val binding by viewBinding { BlankFragmentBinding.bind(it) }
}