package com.emplk.realestatemanager.ui.add

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
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
import com.google.android.material.snackbar.Snackbar
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
                is AddPropertyViewEvent.ShowSnackBarPropertyCreated -> {
                    Snackbar.make(
                        binding.root,
                        event.text.toCharSequence(requireContext()), Snackbar.LENGTH_LONG
                    ).show()
                }

                is AddPropertyViewEvent.OnAddPropertyClicked -> {
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

            binding.addPropertyAddressTextInputEditText.doAfterTextChanged { editable ->
                editable?.let { viewModel.onAddressChanged(editable.toString()) }
            }
        }

        // region Import pictures
        val importPictureCallback = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                viewModel.onPictureSelected(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        val takePictureCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) { successful ->
            if (successful) {
                currentPhotoUri?.let { viewModel.onPictureSelected(it) }
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

    private fun initFormFieldsTextWatchers() {
        binding.addPropertyAddressTextInputEditText.doAfterTextChanged {
            viewModel.onAddressChanged(it.toString())
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

