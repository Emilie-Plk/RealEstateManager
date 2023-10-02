package com.emplk.realestatemanager.ui.map.bottom_sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.MapBottomSheetFragmentBinding
import com.emplk.realestatemanager.ui.edit.EditPropertyFragment
import com.emplk.realestatemanager.ui.main.MainActivity
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.NativePhoto.Companion.load
import com.emplk.realestatemanager.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapBottomSheetFragment : BottomSheetDialogFragment(R.layout.map_bottom_sheet_fragment) {
    companion object {
        const val TAG = "MapBottomSheetFragment"
        const val EDIT_PROPERTY_TAG = "EDIT_PROPERTY_TAG"
        const val DETAIL_PROPERTY_TAG = "DETAIL_PROPERTY_TAG"
        const val KEY_PROPERTY_ID = "KEY_PROPERTY_ID"
        fun newInstance() = MapBottomSheetFragment()
    }

    private val viewModel: MapBottomSheetViewModel by viewModels()
    private val binding by viewBinding { MapBottomSheetFragmentBinding.bind(it) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val standardBottomSheetBehavior = BottomSheetBehavior.from(binding.root.parent as View)
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            binding.mapBottomSheetPropertyTypeTv.text = viewState.type
            binding.mapBottomSheetPropertyPriceTv.text = viewState.price
            binding.mapBottomSheetPropertySurfaceTv.text = viewState.surface
            viewState.featuredPicture
                .load(binding.mapBottomSheetPropertyImageView)
                .centerCrop()
                .error(R.drawable.baseline_villa_24)
                .into(binding.mapBottomSheetPropertyImageView)
            binding.mapBottomSheetPropertyEditBtn.setOnClickListener {
                viewState.onEditClick.invoke(viewState.propertyId)
            }

            binding.mapBottomSheetPropertyDetailBtn.setOnClickListener {
                viewState.onDetailClick.invoke(viewState.propertyId)
            }
        }

        viewModel.viewEvent.observeEvent(viewLifecycleOwner) { viewEvent ->
            when (viewEvent) {
                is MapBottomSheetEvent.OnDetailClick -> {
                    MainActivity.navigate(
                        requireActivity()
                    ).apply {
                        putExtra(KEY_PROPERTY_ID, viewEvent.propertyId)
                    }
                }

                is MapBottomSheetEvent.OnEditClick ->
                    childFragmentManager.commit {
                        dismiss()
                        replace(
                            R.id.blank_frameLayout_container,
                            EditPropertyFragment.newInstance()
                        )
                    }
            }
        }
    }
}