package com.emplk.realestatemanager.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.ui.map.bottom_sheet.MapBottomSheetFragment
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : SupportMapFragment(), OnMapReadyCallback {

    companion object {
        fun newInstance(): Fragment = MapsFragment()
    }

    private val viewModel: MapViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapAsync(this)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true


        val manhattanCameraPosition = CameraPosition.Builder()
            .target(LatLng(40.7128, -74.0060))  // Sets the center of the map to Paris
            .zoom(12f)            // Sets the zoom level (adjust as needed)
            .build()

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(manhattanCameraPosition))

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            // Clear existing markers
            googleMap.clear()
            val customMarkerIcon = vectorToBitmap(requireContext(), R.drawable.baseline_house_pin_circle_24)

            viewState.forEach { markerViewState ->
                val marker = MarkerOptions()
                    .position(markerViewState.latLng)
                    .title(markerViewState.propertyId.toString())
                    .icon(customMarkerIcon)

                val googleMarker = googleMap.addMarker(marker)
                googleMarker?.tag = markerViewState.propertyId
                googleMap.setOnMarkerClickListener {
                    markerViewState.onMarkerClicked.invoke(it.tag as Long)
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLng(it.position)
                    )
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
                    true
                }
            }
        }


        viewModel.viewEventLiveData.observeEvent(this) { event ->
            when (event) {
                is MapEvent.OnMarkerClicked -> {
                    MapBottomSheetFragment.newInstance().show(childFragmentManager, MapBottomSheetFragment.TAG)
                }
            }
        }
    }

    private fun vectorToBitmap(context: Context, @DrawableRes vectorResourceId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResourceId)
        vectorDrawable?.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)

        val bitmap = Bitmap.createBitmap(
            vectorDrawable?.intrinsicWidth ?: 0,
            vectorDrawable?.intrinsicHeight ?: 0,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable?.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}