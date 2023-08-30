package com.emplk.realestatemanager.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.DetailFragmentBinding
import com.emplk.realestatemanager.ui.utils.NativePhoto.Companion.load
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.detail_fragment) {

    private val binding by viewBinding { DetailFragmentBinding.bind(it) }
    private val viewModel by viewModels<DetailViewModel>()

    companion object {
        const val EXTRA_ESTATE_ID = "EXTRA_ESTATE_ID"
        fun newInstance(id: Long): DetailFragment {
            val args = Bundle()
            args.putLong(EXTRA_ESTATE_ID, id)
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner) { detailViewState ->
            detailViewState.featuredPicture
                .load(binding.detailFeaturedImage)
                .error(R.drawable.baseline_villa_24)
                .into(binding.detailFeaturedImage)

            binding.detailTypeTv.text = detailViewState.type
            binding.detailPriceTv.text = detailViewState.price.toCharSequence(requireContext())
            binding.detailLocationTv.text = detailViewState.address.toCharSequence(requireContext())
            binding.detailSurfaceTv.text = detailViewState.surface.toCharSequence(requireContext())
            binding.detailAgentNameTv.text = detailViewState.agentName.toCharSequence(requireContext())
            binding.detailEntryDateTv.text = detailViewState.entryDate.toCharSequence(requireContext())
            binding.detailRoomTv.text = detailViewState.rooms.toCharSequence(requireContext())
            binding.detailBathroomTv.text = detailViewState.bathrooms.toCharSequence(requireContext())
            binding.detailBedroomTv.text = detailViewState.bedrooms.toCharSequence(requireContext())

            val amenitiesMap = mapOf(
                binding.detailAmenitiesConcierge to detailViewState.amenityConcierge,
                binding.detailAmenitiesHospitalTv to detailViewState.amenityHospital,
                binding.detailAmenitiesLibraryTv to detailViewState.amenityLibrary,
                binding.detailAmenitiesParkTv to detailViewState.amenityPark,
                binding.detailAmenitiesPublicTransportationTv to detailViewState.amenityPublicTransportation,
                binding.detailAmenitiesRestaurantTv to detailViewState.amenityRestaurant,
                binding.detailAmenitiesSchoolTv to detailViewState.amenitySchool,
                binding.detailAmenitiesShoppingMallTv to detailViewState.amenityShoppingMall,
            )

            amenitiesMap.forEach { (tv, isAvailable) ->
                tv.visibility = if (isAvailable) View.VISIBLE else View.GONE
            }
        }
    }
}