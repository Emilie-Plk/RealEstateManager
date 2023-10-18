package com.emplk.realestatemanager.ui.edit

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.FormFragmentBinding
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPropertyFragment : BasePropertyFragment() {

    private val viewModel by viewModels<EditPropertyViewModel>()
    private val binding by viewBinding { FormFragmentBinding.bind(it) }

    companion object {
        fun newInstance() = EditPropertyFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.formSoldStatusTv.isVisible = true
        binding.formSoldStatusSwitch.isVisible = true
        binding.formSubmitButton.text = getString(R.string.edit_property_btn)

        binding.formSubmitButton.setOnClickListener {
            viewModel.onUpdatePropertyClicked()
        }

        binding.formTypeActv.setOnItemClickListener { _, _, position, _ ->
            typeAdapter.getItem(position)?.let {
                viewModel.onPropertyTypeChanged(it.name)
            }
        }

        binding.formAgentActv.setOnItemClickListener { _, _, position, _ ->
            agentAdapter.getItem(position)?.let {
                viewModel.onAgentChanged(it.name)
            }
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

            binding.formSubmitButton.isEnabled = viewState.isSubmitButtonEnabled
            binding.formProgressBar.isVisible = viewState.isProgressBarVisible

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
            if (viewState.address != null && currentAddress != viewState.address) {
                binding.formAddressTextInputEditText.setText(viewState.address)
            }

            binding.formAddressTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
                viewModel.onAddressEditTextFocused(hasFocus)
                if (!hasFocus) {
                    hideKeyboard(binding.formAddressTextInputEditText)
                    binding.formAddressTextInputEditText.clearFocus()
                }
            }

            binding.formPriceCurrencyTv.text = viewState.priceCurrency.toCharSequence(requireContext())
            binding.formSurfaceUnitTv.text = viewState.surfaceUnit.toCharSequence(requireContext())

            binding.formAddressTextInputEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.onAddressChanged(null)
                    hideKeyboard(binding.formAddressTextInputEditText)
                    binding.formAddressTextInputEditText.clearFocus()
                    return@setOnEditorActionListener true
                }
                false
            }
            binding.formTypeActv.setText(viewState.propertyType, false)
            binding.formAgentActv.setText(viewState.selectedAgent, false)

            viewModel.onIsSoldStateChanged(binding.formSoldStatusSwitch.isChecked)
        }
    }
}