package com.emplk.realestatemanager.ui.edit

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.FormFragmentBinding
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionListAdapter
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentSpinnerAdapter
import com.emplk.realestatemanager.ui.add.amenity.AmenityListAdapter
import com.emplk.realestatemanager.ui.add.picture_preview.PropertyPicturePreviewListAdapter
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeSpinnerAdapter
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BasePropertyFragment : Fragment(R.layout.form_fragment) {

    private val binding by viewBinding { FormFragmentBinding.bind(it) }

    open val typeAdapter: AddPropertyTypeSpinnerAdapter = AddPropertyTypeSpinnerAdapter()
    open val agentAdapter: AddPropertyAgentSpinnerAdapter = AddPropertyAgentSpinnerAdapter()
    open val picturePreviewAdapter: PropertyPicturePreviewListAdapter = PropertyPicturePreviewListAdapter()
    open val amenityAdapter: AmenityListAdapter = AmenityListAdapter()
    open val predictionAdapter: PredictionListAdapter = PredictionListAdapter()

    open var currentPhotoUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        setNumberPickers()
    }

    private fun setNumberPickers() {
        binding.formRoomsNumberPicker.minValue = 0
        binding.formRoomsNumberPicker.maxValue = 30

        binding.formBedroomsNumberPicker.minValue = 0
        binding.formBedroomsNumberPicker.maxValue = 15

        binding.formBathroomsNumberPicker.minValue = 0
        binding.formBathroomsNumberPicker.maxValue = 15
    }

    private fun setAdapters() {
        binding.formTypeActv.setAdapter(typeAdapter)
        binding.formAgentActv.setAdapter(agentAdapter)
        binding.formPreviewPicturesRecyclerView.adapter = picturePreviewAdapter
        binding.formAmenitiesRecyclerView.adapter = amenityAdapter
        binding.formAddressPredictionsRecyclerView.adapter = predictionAdapter
    }

    open fun hideKeyboard(view: View?) {
        if (view != null) {
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}