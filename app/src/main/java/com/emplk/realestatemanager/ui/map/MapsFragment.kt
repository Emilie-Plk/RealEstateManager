package com.emplk.realestatemanager.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emplk.realestatemanager.ui.map.bottom_sheet.MapBottomSheetFragment
import com.emplk.realestatemanager.ui.utils.Event.Companion.observeEvent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
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
        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
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

            // Add new markers and set the click listener for each
            viewState.forEach { markerViewState ->
                val marker = MarkerOptions()
                    .position(markerViewState.latLng)
                    .title(markerViewState.propertyId.toString())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                val googleMarker = googleMap.addMarker(marker)
                googleMarker?.tag = markerViewState.propertyId
                googleMap.setOnMarkerClickListener {
                    markerViewState.onMarkerClicked.invoke(markerViewState.propertyId)
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
}