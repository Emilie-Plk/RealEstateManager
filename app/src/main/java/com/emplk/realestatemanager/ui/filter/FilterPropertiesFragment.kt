package com.emplk.realestatemanager.ui.filter

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.FilterPropertiesFragmentBinding
import com.emplk.realestatemanager.ui.add.amenity.AmenityListAdapter
import com.emplk.realestatemanager.ui.add.type.PropertyTypeSpinnerAdapter
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

        val propertyTypeAdapter = PropertyTypeSpinnerAdapter()
        binding.filterPropertyTypeActv.setAdapter(propertyTypeAdapter)

        val amenityAdapter = AmenityListAdapter()
        binding.filterPropertyAmenitiesRecyclerView?.adapter = amenityAdapter

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            propertyTypeAdapter.setData(viewState.propertyTypes)
            amenityAdapter.submitList(viewState.amenities)

            binding.filterPropertyTypeActv.setOnItemClickListener { _, _, position, _ ->
                propertyTypeAdapter.getItem(position)?.let {
                    viewModel.onPropertyTypeSelected(it.name)
                }
            }

            binding.filterPropertyPriceRangeSlider.valueFrom = viewState.minPrice.toFloat()
            binding.filterPropertyPriceRangeSlider.valueTo = viewState.maxPrice.toFloat()
            binding.filterPropertyPriceRangeSlider.values = listOf(
                viewState.minPrice.toFloat(),
                viewState.maxPrice.toFloat()
            )
            binding.filterPropertyPriceRangeSlider.setLabelFormatter { value ->
                "$ ${value.toInt()}" // unit
            }

            binding.filterPropertyEntrySaleStateToggleGroup?.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
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