package com.emplk.realestatemanager.ui.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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
abstract class BasePropertyFragment : Fragment(R.layout.add_property_fragment) {

    private val binding by viewBinding { AddPropertyFragmentBinding.bind(it) }

    abstract val typeAdapter: AddPropertyTypeSpinnerAdapter
    abstract val agentAdapter: AddPropertyAgentSpinnerAdapter
    abstract val picturePreviewAdapter: PropertyPicturePreviewListAdapter
    abstract val amenityAdapter: AmenityListAdapter
    abstract val predictionAdapter: PredictionListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapters()
        setNumberPickers()
    }

    private fun setNumberPickers() {
        binding.addPropertyRoomsNumberPicker.minValue = 0
        binding.addPropertyRoomsNumberPicker.maxValue = 30

        binding.addPropertyBedroomsNumberPicker.minValue = 0
        binding.addPropertyBedroomsNumberPicker.maxValue = 15

        binding.addPropertyBathroomsNumberPicker.minValue = 0
        binding.addPropertyBathroomsNumberPicker.maxValue = 15
    }

    private fun setAdapters() {
        binding.addPropertyTypeActv.setAdapter(typeAdapter)
        binding.addPropertyAgentActv.setAdapter(agentAdapter)
        binding.addPropertyPreviewPicturesRecyclerView.adapter = picturePreviewAdapter
        binding.addPropertyAmenitiesRv.adapter = amenityAdapter
        binding.addPropertyAddressPredictionsRecyclerView.adapter = predictionAdapter
    }
}