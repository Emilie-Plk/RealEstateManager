package com.emplk.realestatemanager.ui.map.bottom_sheet

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.MapBottomSheetFragmentBinding
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.ui.blank.BlankActivity
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

        fun newInstance() = MapBottomSheetFragment()
    }

    private val viewModel: MapBottomSheetViewModel by viewModels()
    private val binding by viewBinding { MapBottomSheetFragmentBinding.bind(it) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val standardBottomSheetBehavior = BottomSheetBehavior.from(binding.mapBottomSheetPropertyDetailLayout as View)
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            binding.mapBottomSheetPropertyTypeTv.text = viewState.type
            binding.mapBottomSheetPropertyPriceTv.text = viewState.price

            viewState.featuredPicture
                .load(binding.mapBottomSheetPropertyImageView)
                .transform(CenterCrop())
                .error(R.drawable.baseline_villa_24)
                .into(binding.mapBottomSheetPropertyImageView)

            binding.mapBottomSheetPropertyEditBtn.setOnClickListener {
                viewState.onEditClick.invoke(EDIT_PROPERTY_TAG)
            }
            binding.mapBottomSheetPropertyDetailBtn.setOnClickListener {
                viewState.onDetailClick.invoke(DETAIL_PROPERTY_TAG)
            }
            binding.mapBottomSheetPropertyDescriptionTv.text = viewState.description
            binding.mapBottomSheetPropertySurfaceTv.text = viewState.surface
            binding.mapBottomSheetPropertyRoomsTv.text = viewState.rooms.toCharSequence(requireContext())
            binding.mapBottomSheetPropertyBedroomsTv.text = viewState.bedrooms.toCharSequence(requireContext())
            binding.mapBottomSheetPropertyBathroomsTv.text = viewState.bathrooms.toCharSequence(requireContext())
            binding.mapBottomSheetPropertyProgressBar.isVisible = viewState.isProgressBarVisible
            binding.root.isVisible = !viewState.isProgressBarVisible
        }

        viewModel.viewEvent.observeEvent(viewLifecycleOwner) { viewEvent ->
            when (viewEvent) {
                is MapBottomSheetEvent.Edit -> {
                    startActivity(
                        BlankActivity.navigate(
                            requireContext(),
                            NavigationFragmentType.EDIT_FRAGMENT.name,
                        )
                    )
                    dismiss()
                }

                is MapBottomSheetEvent.Detail -> {
                    startActivity(MainActivity.navigate(requireContext(), NavigationFragmentType.DETAIL_FRAGMENT.name))
                    requireActivity().finish()
                }
            }
        }
    }
}

