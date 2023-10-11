package com.emplk.realestatemanager.ui.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddPropertyFragmentBinding
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionListAdapter
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentSpinnerAdapter
import com.emplk.realestatemanager.ui.add.amenity.AmenityListAdapter
import com.emplk.realestatemanager.ui.add.picture_preview.PropertyPicturePreviewListAdapter
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeSpinnerAdapter
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPropertyFragment(
    override val typeAdapter: AddPropertyTypeSpinnerAdapter = AddPropertyTypeSpinnerAdapter(),
    override val agentAdapter: AddPropertyAgentSpinnerAdapter = AddPropertyAgentSpinnerAdapter(),
    override val picturePreviewAdapter: PropertyPicturePreviewListAdapter = PropertyPicturePreviewListAdapter(),
    override val amenityAdapter: AmenityListAdapter = AmenityListAdapter(),
    override val predictionAdapter: PredictionListAdapter = PredictionListAdapter(),
) : BasePropertyFragment() {

    private val binding by viewBinding { AddPropertyFragmentBinding.bind(it) }
    private val viewModel by viewModels<EditPropertyViewModel>()

    companion object {
        fun newInstance() = EditPropertyFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addPropertySoldStatusTv.isVisible = true
        binding.addPropertySoldStatusSwitch.isVisible = true
        binding.addPropertySubmitButton.text = getString(R.string.edit_property_btn)

        binding.addPropertyTypeActv.setOnItemClickListener { _, _, position, _ ->
            binding.addPropertyTypeActv.adapter.getItem(position)?.let {
             //   viewModel.onPropertyTypeChanged(it.name)
            }
        }

        binding.addPropertyAgentActv.setOnItemClickListener { _, _, position, _ ->
            binding.addPropertyAgentActv.adapter.getItem(position)?.let {
               // viewModel.onAgentChanged(it.name)
            }
        }
    }
}