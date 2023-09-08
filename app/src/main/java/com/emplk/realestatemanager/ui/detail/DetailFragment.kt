package com.emplk.realestatemanager.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.DetailFragmentBinding
import com.emplk.realestatemanager.ui.edit.EditPropertyFragment
import com.emplk.realestatemanager.ui.main.MainActivity
import com.emplk.realestatemanager.ui.property_list.PropertiesFragment
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.emplk.realestatemanager.ui.utils.NativePhoto.Companion.load
import com.emplk.realestatemanager.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.detail_fragment) {

    private val binding by viewBinding { DetailFragmentBinding.bind(it) }
    private val viewModel by viewModels<DetailViewModel>()

    companion object {
        fun newInstance() = DetailFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner) { detailViewState ->
            when (detailViewState) {
                is DetailViewState.LoadingState -> {
                    binding.root.isVisible = false
                    binding.detailProgressBar.isVisible = true
                }

                is DetailViewState.PropertyDetail -> {
                    binding.detailProgressBar.isVisible = false
                    binding.root.isVisible = true
                    detailViewState.featuredPicture
                        .load(binding.detailFeaturedImage)
                        .error(R.drawable.baseline_villa_24)
                        .into(binding.detailFeaturedImage)

                    binding.detailEditFab.setOnClickListener {
                        Log.d("COUCOU", "onEditClicked: setonclicklistener")
                        viewModel.onEditClicked()
                    }

                    if (detailViewState.isSold) {
                        binding.detailSoldDateTv.visibility = View.VISIBLE
                        binding.detailSoldDateTv.text = detailViewState.saleDate?.toCharSequence(requireContext())
                    } else {
                        binding.detailSoldDateTv.visibility = View.GONE
                    }

                    binding.detailTypeTv.text = detailViewState.propertyType
                    binding.detailPriceTv.text = detailViewState.price.toCharSequence(requireContext())
                    binding.detailDescriptionTv.text = detailViewState.description
                    binding.detailLocationTv.text = detailViewState.address.toCharSequence(requireContext())
                    binding.detailSurfaceTv.text = detailViewState.surface.toCharSequence(requireContext())
                    binding.detailAgentNameTv.text = detailViewState.agentName.toCharSequence(requireContext())
                    binding.detailEntryDateTv.text = detailViewState.entryDate.toCharSequence(requireContext())
                    binding.detailRoomTv.text = detailViewState.rooms.toCharSequence(requireContext())
                    binding.detailBathroomTv.text = detailViewState.bathrooms.toCharSequence(requireContext())
                    binding.detailBedroomTv.text = detailViewState.bedrooms.toCharSequence(requireContext())

                    mapOf(
                        binding.detailAmenitiesConciergeTv to detailViewState.amenityConcierge,
                        binding.detailAmenitiesHospitalTv to detailViewState.amenityHospital,
                        binding.detailAmenitiesLibraryTv to detailViewState.amenityLibrary,
                        binding.detailAmenitiesParkTv to detailViewState.amenityPark,
                        binding.detailAmenitiesPublicTransportationTv to detailViewState.amenityPublicTransportation,
                        binding.detailAmenitiesRestaurantTv to detailViewState.amenityRestaurant,
                        binding.detailAmenitiesSchoolTv to detailViewState.amenitySchool,
                        binding.detailAmenitiesShoppingMallTv to detailViewState.amenityShoppingMall,
                    ).forEach { (tv, isAvailable) ->
                        tv.visibility = if (isAvailable) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }
}