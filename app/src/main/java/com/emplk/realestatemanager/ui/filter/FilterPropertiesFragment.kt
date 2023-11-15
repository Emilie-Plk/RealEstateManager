package com.emplk.realestatemanager.ui.filter

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.FilterPropertiesFragmentBinding
import com.emplk.realestatemanager.ui.add.amenity.AmenityListAdapter
import com.emplk.realestatemanager.ui.add.type.PropertyTypeSpinnerAdapter
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
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
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)

        val propertyTypeAdapter = PropertyTypeSpinnerAdapter()
        binding.filterPropertyTypeActv.setAdapter(propertyTypeAdapter)

        val amenityAdapter = AmenityListAdapter()
        binding.filterPropertyAmenitiesRecyclerView?.adapter = amenityAdapter

        binding.filterPropertyCancelBtn.setOnClickListener {
            dismiss()
        }

        binding.filterPropertyResetBtn?.setOnClickListener {
            viewModel.onResetFilters()
        }

        viewModel.viewEvent.observeEvent(viewLifecycleOwner) {
            dismiss()
        }

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            propertyTypeAdapter.setData(viewState.propertyTypes)
            amenityAdapter.submitList(viewState.amenities)

            binding.filterPropertyTypeActv.setOnItemClickListener { _, _, position, _ ->
                propertyTypeAdapter.getItem(position)?.let {
                    viewModel.onPropertyTypeSelected(it.name)
                }
            }

            binding.filterPropertyTypeActv.setText(viewState.propertyType, false)

            binding.filterPropertyPriceRangeTv.text = viewState.priceRange.toCharSequence(requireContext())
            binding.filterPropertyMinPriceEditText.doAfterTextChanged {
                viewModel.onMinPriceChanged(it?.toString() ?: "")
            }

            binding.filterPropertyMaxPriceEditText.doAfterTextChanged {
                viewModel.onMaxPriceChanged(it?.toString() ?: "")
            }

            binding.filterPropertySurfaceRangeTv.text = viewState.surfaceRange.toCharSequence(requireContext())
            binding.filterPropertyMinSurfaceEditText.doAfterTextChanged {
                viewModel.onMinSurfaceChanged(it?.toString() ?: "")
            }

            binding.filterPropertyMaxSurfaceEditText.doAfterTextChanged {
                viewModel.onMaxSurfaceChanged(it?.toString() ?: "")
            }

            binding.filterPropertyEntryDateChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
                checkedIds.forEach { checkedId ->
                    when (checkedId) {
                        R.id.filter_property_entry_less_than_1_year_chip -> viewModel.onEntryDateStatusChanged(
                            EntryDateState.LESS_THAN_1_YEAR
                        )

                        R.id.filter_property_entry_less_than_6_months_chip -> viewModel.onEntryDateStatusChanged(
                            EntryDateState.LESS_THAN_6_MONTHS
                        )

                        R.id.filter_property_entry_date_less_than_3_months_chip -> viewModel.onEntryDateStatusChanged(
                            EntryDateState.LESS_THAN_3_MONTHS
                        )

                        R.id.filter_property_entry_date_less_than_1_month_chip -> viewModel.onEntryDateStatusChanged(
                            EntryDateState.LESS_THAN_1_MONTH
                        )

                        R.id.filter_property_entry_date_less_than_1_week_chip -> viewModel.onEntryDateStatusChanged(
                            EntryDateState.LESS_THAN_1_WEEK
                        )

                        R.id.filter_property_entry_date_all_chip -> viewModel.onEntryDateStatusChanged(
                            EntryDateState.ALL
                        )

                        else -> viewModel.onEntryDateStatusChanged(EntryDateState.ALL)
                    }
                }
            }

            binding.filterPropertyFilterBtn.isEnabled = viewState.isFilterButtonEnabled
            binding.filterPropertyFilterBtn.text = viewState.filterButtonText.toCharSequence(requireContext())
            binding.filterPropertyFilterBtn.setOnClickListener {
                viewState.onFilterClicked()
            }
        }

        binding.filterPropertyEntrySaleStateToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.filter_property_for_sale_btn -> viewModel.onPropertySaleStateChanged(
                        PropertySaleState.FOR_SALE
                    )

                    R.id.filter_property_sold_btn -> viewModel.onPropertySaleStateChanged(
                        PropertySaleState.SOLD
                    )

                    R.id.filter_property_all_btn -> viewModel.onPropertySaleStateChanged(
                        PropertySaleState.ALL
                    )

                    else -> viewModel.onPropertySaleStateChanged(PropertySaleState.ALL)
                }
            }
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