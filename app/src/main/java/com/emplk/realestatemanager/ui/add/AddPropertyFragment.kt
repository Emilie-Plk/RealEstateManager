package com.emplk.realestatemanager.ui.add

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddPropertyFragmentBinding
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionListAdapter
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentSpinnerAdapter
import com.emplk.realestatemanager.ui.add.amenity.AmenityListAdapter
import com.emplk.realestatemanager.ui.add.picture_preview.PropertyPicturePreviewListAdapter
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeSpinnerAdapter
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddPropertyFragment : Fragment(R.layout.add_property_fragment) {

    companion object {
        fun newInstance(): Fragment = AddPropertyFragment()
    }

    private val binding by viewBinding { AddPropertyFragmentBinding.bind(it) }
    private val viewModel by viewModels<AddPropertyViewModel>()
    private var currentPhotoUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPropertySoldStatusTv.isVisible = false
        binding.addPropertySoldStatusSwitch.isVisible = false

        val typeAdapter = AddPropertyTypeSpinnerAdapter()
        binding.addPropertyTypeActv.setAdapter(typeAdapter)
        binding.addPropertyTypeActv.setOnItemClickListener { _, _, position, _ ->
            typeAdapter.getItem(position)?.let {
                viewModel.onPropertyTypeSelected(it.name)
            }
        }

        val agentAdapter = AddPropertyAgentSpinnerAdapter()
        binding.addPropertyAgentActv.setAdapter(agentAdapter)
        binding.addPropertyAgentActv.setOnItemClickListener { _, _, position, _ ->
            agentAdapter.getItem(position)?.let {
                viewModel.onAgentSelected(it.name)
            }
        }

        val picturePreviewAdapter = PropertyPicturePreviewListAdapter()
        binding.addPropertyPreviewPicturesRecyclerView.adapter = picturePreviewAdapter

        val amenityAdapter = AmenityListAdapter()
        binding.addPropertyAmenitiesRv.adapter = amenityAdapter

        val predictionAdapter = PredictionListAdapter()
        binding.addPropertyAddressPredictionsRecyclerView.adapter = predictionAdapter

        setNumberPickers()

        initFormFieldsTextWatchers()

        binding.addPropertyCreateButton.setOnClickListener {
            viewModel.onAddPropertyClicked()
        }

        viewModel.viewEventLiveData.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is AddPropertyEvent.ShowToast -> {
                    Toast.makeText(
                        requireContext(),
                        event.text.toCharSequence(requireContext()),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            typeAdapter.setData(viewState.propertyTypes)
            agentAdapter.setData(viewState.agents)
            picturePreviewAdapter.submitList(viewState.pictures)
            amenityAdapter.submitList(viewState.amenities)
            predictionAdapter.submitList(viewState.addressPredictions)
            binding.addPropertyCreateButton.isEnabled = viewState.isAddButtonEnabled
            binding.addPropertyProgressBar.isVisible = viewState.isProgressBarVisible


            binding.addPropertyRoomsNumberPicker.value = viewState.nbRooms
            binding.addPropertyBedroomsNumberPicker.value = viewState.nbBedrooms
            binding.addPropertyBathroomsNumberPicker.value = viewState.nbBathrooms

            val currentDescription = binding.addPropertyDescriptionTextInputEditText.text.toString()
            if (currentDescription != viewState.description) {
                binding.addPropertyDescriptionTextInputEditText.setText(viewState.description)
            }

            val currentSurface = binding.addPropertySurfaceTextInputEditText.text.toString()
            if (currentSurface != viewState.surface) {
                binding.addPropertySurfaceTextInputEditText.setText(viewState.surface)
            }

            val currentPrice = binding.addPropertyPriceTextInputEditText.text.toString()
            if (currentPrice != viewState.price) {
                binding.addPropertyPriceTextInputEditText.setText(viewState.price)
            }

            val currentAddress = binding.addPropertyAddressTextInputEditText.text.toString()
            if (viewState.address != null && currentAddress != viewState.address) {
                binding.addPropertyAddressTextInputEditText.setText(viewState.address)
            }

            binding.addPropertyAddressTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
                viewModel.onAddressEditTextFocused(hasFocus)
            }

            binding.addPropertyAddressTextInputEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.onAddressChanged(null)
                    hideKeyboard(binding.addPropertyAddressTextInputEditText)
                    binding.addPropertyAddressTextInputEditText.clearFocus()
                    return@setOnEditorActionListener true
                }
                false // Return false to indicate that you didn't handle the event
            }
            binding.addPropertyTypeActv.setText(viewState.propertyType, false)
            binding.addPropertyAgentActv.setText(viewState.selectedAgent, false)
        }

        // region Import pictures
        val importPictureCallback = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                viewModel.onPictureFromGallerySelected(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        val takePictureCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) { successful ->
            if (successful) {
                currentPhotoUri?.let { viewModel.onPictureFromCameraTaken(it.toString()) }
            }
        }

        binding.addPropertyPicturesFromStorageButton.setOnClickListener {
            importPictureCallback.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.addPropertyFromCameraButton.setOnClickListener {
            currentPhotoUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                File.createTempFile(
                    "JPEG_",
                    ".jpg",
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                )
            )
            takePictureCallback.launch(currentPhotoUri)
        }
        // endregion Import pictures
    }

    private fun hideKeyboard(view: View?) {
        if (view != null) {
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun initFormFieldsTextWatchers() {
        binding.addPropertyAddressTextInputEditText.doAfterTextChanged {
            viewModel.onAddressChanged(it.toString())
            binding.addPropertyAddressTextInputEditText.setSelection(it.toString().length)
        }

        binding.addPropertyPriceTextInputEditText.doAfterTextChanged {
            viewModel.onPriceChanged(it.toString())
        }

        binding.addPropertySurfaceTextInputEditText.doAfterTextChanged {
            viewModel.onSurfaceChanged(it.toString())
        }

        binding.addPropertyDescriptionTextInputEditText.doAfterTextChanged {
            viewModel.onDescriptionChanged(it.toString())
        }
    }

    private fun setNumberPickers() {
        binding.addPropertyRoomsNumberPicker.minValue = 0
        binding.addPropertyRoomsNumberPicker.maxValue = 30
        binding.addPropertyRoomsNumberPicker.setOnValueChangedListener { _, _, value ->
            viewModel.onRoomsNumberChanged(value)
        }

        binding.addPropertyBedroomsNumberPicker.minValue = 0
        binding.addPropertyBedroomsNumberPicker.maxValue = 15
        binding.addPropertyBedroomsNumberPicker.setOnValueChangedListener { _, _, value ->
            viewModel.onBedroomsNumberChanged(value)
        }

        binding.addPropertyBathroomsNumberPicker.minValue = 0
        binding.addPropertyBathroomsNumberPicker.maxValue = 15
        binding.addPropertyBathroomsNumberPicker.setOnValueChangedListener { _, _, value ->
            viewModel.onBathroomsNumberChanged(value)
        }
    }
}

