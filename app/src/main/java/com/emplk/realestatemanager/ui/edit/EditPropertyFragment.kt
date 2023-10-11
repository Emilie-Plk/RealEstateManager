package com.emplk.realestatemanager.ui.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddPropertyFragmentBinding
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPropertyFragment : BasePropertyFragment() {

    private val viewModel by viewModels<EditPropertyViewModel>()
    private val binding by viewBinding { AddPropertyFragmentBinding.bind(it) }

    companion object {
        fun newInstance() = EditPropertyFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addPropertySoldStatusTv.isVisible = true
        binding.addPropertySoldStatusSwitch.isVisible = true
        binding.addPropertySubmitButton.text = getString(R.string.edit_property_btn)

        binding.addPropertyTypeActv.setOnItemClickListener { _, _, position, _ ->
            typeAdapter.getItem(position)?.let {
             //   viewModel.onPropertyTypeChanged(it.name)
            }
        }

        binding.addPropertyAgentActv.setOnItemClickListener { _, _, position, _ ->
            agentAdapter.getItem(position)?.let {
              //  viewModel.onAgentChanged(it.name)
            }
        }

        binding.addPropertySubmitButton.setOnClickListener {
      //      viewModel.onEditPropertyClicked()
        }

    }
}