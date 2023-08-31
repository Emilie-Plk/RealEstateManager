package com.emplk.realestatemanager.ui.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddPropertyFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPropertyFragment : Fragment(R.layout.add_property_fragment) {

    private val binding by viewBinding { AddPropertyFragmentBinding.bind(it) }

    companion object {
        private const val EXTRA_PROPERTY_ID = "EXTRA_PROPERTY_ID"

        fun newInstance(propertyId: Long): EditPropertyFragment {
            return EditPropertyFragment().apply {
                arguments = Bundle().apply {
                    putLong(EXTRA_PROPERTY_ID, propertyId)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}