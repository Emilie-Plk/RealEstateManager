package com.emplk.realestatemanager.ui.filter

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.FilterPropertiesFragmentBinding
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionListAdapter
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterPropertiesFragment : DialogFragment(R.layout.filter_properties_fragment) {
    companion object {
        fun newInstance() = FilterPropertiesFragment()
    }

    private val binding by viewBinding { FilterPropertiesFragmentBinding.bind(it) }
    private val viewModel by viewModels<FilterPropertiesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val width = (Resources.getSystem().displayMetrics.widthPixels * 0.98).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)

        val predictionsAdapter = PredictionListAdapter()
        binding.filterLocationPredictionsRecyclerView.adapter = predictionsAdapter // why is it nullable??

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            predictionsAdapter.submitList(viewState.locationPredictions)

            val currentLocation = binding.filterPropertyLocationEditText.text.toString()

            if (currentLocation != viewState.location && viewState.location != null) {
                binding.filterPropertyLocationEditText.setText(viewState.location)
            }

            binding.filterPropertyLocationEditText.doAfterTextChanged {
                binding.filterPropertyLocationEditText.setSelection(it.toString().length)
                val newText = it?.toString() ?: ""
                if (newText != viewState.location) {
                    viewModel.onLocationChanged(newText)
                }
            }

            binding.filterPropertyLocationEditText.setOnFocusChangeListener { _, hasFocus ->
                viewModel.onAddressEditTextFocused(hasFocus)
                if (!hasFocus) {
                    hideKeyboard(binding.filterPropertyLocationEditText)
                }
            }

            binding.filterPropertyRadiusTextInputLayout?.isVisible = viewState.isRadiusEditTextVisible
            binding.filterPropertyLocationTextInputLayout.isHelperTextEnabled = viewState.isRadiusEditTextVisible

            binding.filterPropertyPriceRangeSlider.valueFrom = viewState.minPrice.toFloat()
            binding.filterPropertyPriceRangeSlider.valueTo = viewState.maxPrice.toFloat()
            binding.filterPropertyPriceRangeSlider.values = listOf(
                viewState.minPrice.toFloat(),
                viewState.maxPrice.toFloat()
            )
            binding.filterPropertyPriceRangeSlider.setLabelFormatter { value ->
                "${value.toInt()} €" // unit
            }

            binding.filterPropertyEntryDateChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                checkedIds.forEach { id ->
                    when (id) {
                        R.id.filter_property_entry_more_than_6_months_chip -> viewModel.onEntryDateStatusChanged(
                            EntryDateStatus.MORE_THAN_6_MONTHS
                        )

                        R.id.filter_property_entry_date_less_than_3_months_chip -> viewModel.onEntryDateStatusChanged(
                            EntryDateStatus.LESS_THAN_3_MONTHS
                        )

                        R.id.filter_property_entry_date_less_than_1_month_chip -> viewModel.onEntryDateStatusChanged(
                            EntryDateStatus.LESS_THAN_1_MONTH
                        )

                        R.id.filter_property_entry_date_less_than_1_week_chip -> viewModel.onEntryDateStatusChanged(
                            EntryDateStatus.LESS_THAN_1_WEEK
                        )

                        else -> viewModel.onEntryDateStatusChanged(EntryDateStatus.NONE)
                    }
                }
            }

            binding.filterPropertySurfaceRangeSlider.valueFrom = viewState.minSurface.toFloat()
            binding.filterPropertySurfaceRangeSlider.valueTo = viewState.maxSurface.toFloat()
            binding.filterPropertySurfaceRangeSlider.values = listOf(
                viewState.minSurface.toFloat(),
                viewState.maxSurface.toFloat()
            )
            binding.filterPropertySurfaceRangeSlider.setLabelFormatter { value ->
                "${value.toInt()} sq ft"
            }

            binding.filterPropertyFilterBtn.text = viewState.filterButtonText.toCharSequence(requireContext())
            binding.filterPropertyFilterBtn.setOnClickListener {
                viewState.onFilterClicked()
            }
        }

        binding.filterPropertyCancelBtn.setOnClickListener {
            dismiss()
            //viewModel.onCancelClicked()
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