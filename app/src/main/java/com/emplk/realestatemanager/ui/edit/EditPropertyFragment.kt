package com.emplk.realestatemanager.ui.edit

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.AddPropertyFragmentBinding
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
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

        binding.addPropertySubmitButton.setOnClickListener {
            viewModel.onUpdatePropertyClicked()
        }

        binding.addPropertyTypeActv.setOnItemClickListener { _, _, position, _ ->
            typeAdapter.getItem(position)?.let {
                viewModel.onPropertyTypeChanged(it.name)
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

        viewModel.viewEventLiveData.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is EditPropertyEvent.Toast -> {
                    Toast.makeText(requireContext(), event.text.toCharSequence(requireContext()), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            typeAdapter.setData(viewState.propertyTypes)
            agentAdapter.setData(viewState.agents)
            picturePreviewAdapter.submitList(viewState.pictures)
            amenityAdapter.submitList(viewState.amenities)
            predictionAdapter.submitList(viewState.addressPredictions)

            binding.addPropertySubmitButton.isEnabled = viewState.isSubmitButtonEnabled
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
                if (!hasFocus) {
                    hideKeyboard(binding.addPropertyAddressTextInputEditText)
                    binding.addPropertyAddressTextInputEditText.clearFocus()
                }
            }

            binding.addPropertyPriceCurrencyTv.text = viewState.priceCurrency.toCharSequence(requireContext())
            binding.addPropertySurfaceUnitTv.text = viewState.surfaceUnit.toCharSequence(requireContext())

            binding.addPropertyAddressTextInputEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.onAddressChanged(null)
                    hideKeyboard(binding.addPropertyAddressTextInputEditText)
                    binding.addPropertyAddressTextInputEditText.clearFocus()
                    return@setOnEditorActionListener true
                }
                false
            }
            binding.addPropertyTypeActv.setText(viewState.propertyType, false)
            binding.addPropertyAgentActv.setText(viewState.selectedAgent, false)
        }
    }
}