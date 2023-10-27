package com.emplk.realestatemanager.ui.add

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.BuildConfig
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.FormFragmentBinding
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionListAdapter
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentSpinnerAdapter
import com.emplk.realestatemanager.ui.add.amenity.AmenityListAdapter
import com.emplk.realestatemanager.ui.add.picture_preview.PropertyPicturePreviewListAdapter
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeSpinnerAdapter
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.math.BigDecimal


@AndroidEntryPoint
class AddOrEditPropertyFragment : Fragment(R.layout.form_fragment) {

    companion object {
        const val PROPERTY_ID_KEY = "PROPERTY_ID_KEY"

        fun newInstance(id: Long?): Fragment {
            val bundle = Bundle().apply {
                id?.let { putLong(PROPERTY_ID_KEY, it) }
            }
            val fragment = AddOrEditPropertyFragment()
            fragment.arguments = bundle
            return fragment
        }

        private var currentPhotoUri: Uri? = null
    }

    private val viewModel by viewModels<AddOrEditPropertyViewModel>()
    private val binding by viewBinding { FormFragmentBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("COUCOU", "AddOrEditPropertyFragment onViewCreated: ")

        setNumberPickersListeners()

        initFormFieldsTextWatchers()

        val typeAdapter = AddPropertyTypeSpinnerAdapter()
        val agentAdapter = AddPropertyAgentSpinnerAdapter()
        val picturePreviewAdapter = PropertyPicturePreviewListAdapter()
        val amenityAdapter = AmenityListAdapter()
        val predictionAdapter = PredictionListAdapter()

        binding.formTypeActv.setAdapter(typeAdapter)
        binding.formAgentActv.setAdapter(agentAdapter)
        binding.formPreviewPicturesRecyclerView.adapter = picturePreviewAdapter
        binding.formAmenitiesRecyclerView.adapter = amenityAdapter
        binding.formAddressPredictionsRecyclerView.adapter = predictionAdapter

        binding.formSubmitButton.setOnClickListener {
            viewModel.onAddPropertyClicked()
        }

        viewModel.viewEventLiveData.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is FormEvent.Toast -> Toast.makeText(
                    requireContext(),
                    event.text.toCharSequence(requireContext()),
                    Toast.LENGTH_SHORT
                ).show()

                is FormEvent.Error -> Toast.makeText(
                    requireContext(),
                    event.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            typeAdapter.setData(viewState.propertyTypes)
            agentAdapter.setData(viewState.agents)
            picturePreviewAdapter.submitList(viewState.pictures)
            amenityAdapter.submitList(viewState.amenities)
            predictionAdapter.submitList(viewState.addressPredictions)

            binding.formSubmitButton.text = viewState.submitButtonText.toCharSequence(requireContext())
            binding.formSoldStatusSwitch.isVisible = viewState.isSoldSwitchVisible
            binding.formSoldStatusTv.isVisible = viewState.isSoldSwitchVisible

            binding.formTypeActv.setOnItemClickListener { _, _, position, _ ->
                typeAdapter.getItem(position)?.let {
                    viewModel.onPropertyTypeSelected(it.name)
                }
            }

            binding.formAgentActv.setOnItemClickListener { _, _, position, _ ->
                agentAdapter.getItem(position)?.let {
                    viewModel.onAgentSelected(it.name)
                }
            }

            binding.formSubmitButton.isEnabled = viewState.isSubmitButtonEnabled
            binding.formProgressBar.isVisible = viewState.isProgressBarVisible

            setNumberPickers()
            binding.formRoomsNumberPicker.value = viewState.nbRooms
            binding.formBedroomsNumberPicker.value = viewState.nbBedrooms
            binding.formBathroomsNumberPicker.value = viewState.nbBathrooms

            val currentDescription = binding.formDescriptionTextInputEditText.text.toString()
            if (currentDescription != viewState.description) {
                binding.formDescriptionTextInputEditText.setText(viewState.description)
            }

            val currentSurface = binding.formSurfaceTextInputEditText.text.toString()
            if (currentSurface != viewState.surface) {
                binding.formSurfaceTextInputEditText.setText(viewState.surface)
            }

            val currentPrice = binding.formPriceTextInputEditText.text.toString()
            if (currentPrice != viewState.price) {
                binding.formPriceTextInputEditText.setText(viewState.price)
            }

            val currentAddress = binding.formAddressTextInputEditText.text.toString()
            if (currentAddress != viewState.address) {
                binding.formAddressTextInputEditText.setText(viewState.address)
            }

            binding.formAddressTextInputEditText.doAfterTextChanged {
                binding.formAddressTextInputEditText.setSelection(it.toString().length)
                val newText = it.toString()
                if (newText != viewState.address) {
                    viewModel.onAddressChanged(newText)
                }
            }

            binding.formAddressTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
                viewModel.onAddressEditTextFocused(hasFocus)
                if (!hasFocus) {
                    hideKeyboard(binding.formAddressTextInputEditText)
                }
            }

            binding.formAddressIsValidHelperTv.isVisible = viewState.isAddressValid

            binding.formSoldStatusSwitch.isChecked = viewState.isSold ?: false
            binding.formSoldStatusSwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onSoldStatusChanged(isChecked)
            }

            binding.formPriceCurrencyTv.text = viewState.priceCurrency.toCharSequence(requireContext())
            binding.formSurfaceUnitTv.text = viewState.surfaceUnit.toCharSequence(requireContext())

            binding.formTypeActv.setText(viewState.propertyType, false)
            binding.formAgentActv.setText(viewState.selectedAgent, false)
        }

        // region Import pictures
        val importPictureCallback = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                viewModel.onPictureSelected(uri.toString())
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        binding.formPicturesFromStorageButton.setOnClickListener {
            importPictureCallback.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.formFromCameraButton.setOnClickListener {
            currentPhotoUri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                File.createTempFile(
                    "JPEG_",
                    ".jpg",
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                )
            )

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)

            @Suppress("DEPRECATION")
            startActivityForResult(intent, 0)
            // endregion Import pictures
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            viewModel.onPictureSelected(currentPhotoUri.toString())
        }
    }

    private fun initFormFieldsTextWatchers() {


        binding.formPriceTextInputEditText.doAfterTextChanged {
            if (it.isNullOrBlank()) {
                viewModel.onPriceChanged(BigDecimal.ZERO)
                return@doAfterTextChanged
            }
            viewModel.onPriceChanged(BigDecimal(it.toString()))
        }

        binding.formSurfaceTextInputEditText.doAfterTextChanged {
            viewModel.onSurfaceChanged(it.toString())
        }

        binding.formDescriptionTextInputEditText.doAfterTextChanged {
            viewModel.onDescriptionChanged(it.toString())
        }
    }

    private fun setNumberPickers() {
        binding.formRoomsNumberPicker.minValue = 0
        binding.formRoomsNumberPicker.maxValue = 30

        binding.formBedroomsNumberPicker.minValue = 0
        binding.formBedroomsNumberPicker.maxValue = 15

        binding.formBathroomsNumberPicker.minValue = 0
        binding.formBathroomsNumberPicker.maxValue = 15
    }

    private fun setNumberPickersListeners() {
        binding.formRoomsNumberPicker.setOnValueChangedListener { _, _, value ->
            viewModel.onRoomsNumberChanged(value)
        }

        binding.formBedroomsNumberPicker.setOnValueChangedListener { _, _, value ->
            viewModel.onBedroomsNumberChanged(value)
        }

        binding.formBathroomsNumberPicker.setOnValueChangedListener { _, _, value ->
            viewModel.onBathroomsNumberChanged(value)
        }
    }

    private fun hideKeyboard(view: View?) {
        if (view != null) {
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

