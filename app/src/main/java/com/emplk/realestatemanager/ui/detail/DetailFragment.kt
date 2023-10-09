package com.emplk.realestatemanager.ui.detail

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.databinding.DetailFragmentBinding
import com.emplk.realestatemanager.ui.add.amenity.AmenityListAdapter
import com.emplk.realestatemanager.ui.detail.picture_banner.PictureBannerListAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pictureBannerAdapter = PictureBannerListAdapter()
        binding.detailPhotoCarouselRv.adapter = pictureBannerAdapter

        val amenityAdapter = AmenityListAdapter()
        binding.detailAmenitiesRecyclerViewFlexbox.adapter = amenityAdapter

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is DetailViewState.LoadingState -> {
                    binding.root.isVisible = false
                    binding.detailProgressBar.isVisible = true
                }

                is DetailViewState.PropertyDetail -> {
                    binding.detailPhotoCarouselRv.apply {
                        set3DItem(true)
                        setInfinite(true)
                        setAlpha(true)
                    }
                    pictureBannerAdapter.submitList(viewState.pictures)
                    binding.detailProgressBar.isVisible = false
                    binding.root.isVisible = true

                    amenityAdapter.submitList(viewState.amenities)

                    binding.detailEditFab.setOnClickListener {
                        viewModel.onEditClicked()
                    }

                    if (viewState.isSold) {
                        binding.detailSoldDateTv.visibility = View.VISIBLE
                        binding.detailSoldDateTv.text = viewState.saleDate?.toCharSequence(requireContext())
                    } else {
                        binding.detailSoldDateTv.visibility = View.GONE
                    }

                    binding.detailTypeTv.text = viewState.propertyType
                    binding.detailPriceTv.text = viewState.price
                    if (viewState.isSold) {
                        // strike through price
                        binding.detailPriceTv.paintFlags = binding.detailPriceTv.paintFlags or
                                Paint.STRIKE_THRU_TEXT_FLAG
                    }
                    binding.detailLastUpdatedCurrencyRateTv.isVisible = viewState.isCurrencyLastUpdatedCurrencyRateVisible

                    if (viewState.isCurrencyLastUpdatedCurrencyRateVisible) {
                        binding.detailLastUpdatedCurrencyRateTv.text =
                            viewState.lastUpdatedCurrencyRateDate.toCharSequence(requireContext())
                    }
                    binding.detailDescriptionTv.text = viewState.description
                    binding.detailLocationTv.text = viewState.address.toCharSequence(requireContext())
                    binding.detailSurfaceTv.text = viewState.surface.toCharSequence(requireContext())
                    binding.detailAgentNameTv.text = viewState.agentName.toCharSequence(requireContext())
                    binding.detailEntryDateTv.text = viewState.entryDate.toCharSequence(requireContext())
                    binding.detailRoomTv.text = viewState.rooms.toCharSequence(requireContext())
                    binding.detailBathroomTv.text = viewState.bathrooms.toCharSequence(requireContext())
                    binding.detailBedroomTv.text = viewState.bedrooms.toCharSequence(requireContext())
                    viewState.mapMiniature.load(binding.detailMapIv)
                        .error(R.drawable.baseline_villa_24)
                        .into(binding.detailMapIv)
                }
            }
        }
    }
}