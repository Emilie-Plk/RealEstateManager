package com.emplk.realestatemanager.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapsFragment : SupportMapFragment(), OnMapReadyCallback {

    companion object {
        fun newInstance(): Fragment = MapsFragment()

    }

    private val viewModel: MapViewModel by viewModels()


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            viewModel.hasPermissionBeenGranted(
                permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                        permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
            )
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        getMapAsync(this)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            googleMap.clear()

            val cameraPosition = CameraPosition.Builder()
                .target(viewState.userCurrentLocation ?: viewState.fallbackLocationGoogleHq)
                .zoom(12f)
                .build()

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            // User marker
            viewState.userCurrentLocation?.let { userCurrentLocation ->
                val userMarkerOptions = MarkerOptions()
                    .position(userCurrentLocation)
                    .title("Hello!")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

                @SuppressLint("MissingPermission")
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true
                googleMap.setOnMyLocationButtonClickListener {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userCurrentLocation, 12f))
                    true
                }

                googleMap.addMarker(userMarkerOptions)

                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLng(userCurrentLocation)
                )
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12f))
            }

            // Property markers
            val customMarkerIcon = vectorToBitmap(requireContext(), R.drawable.baseline_house_pin_circle_24)
            viewState.propertyMarkers.forEach { markerViewState ->
                val marker = MarkerOptions()
                    .position(markerViewState.latLng)
                    .title(markerViewState.propertyId.toString())
                    .icon(customMarkerIcon)

                val propertyMarker = googleMap.addMarker(marker)
                propertyMarker?.tag = markerViewState.propertyId

                googleMap.setOnMarkerClickListener {
                    Log.d("COUCOU", "Marker clicked: ${it.tag}")
                    if (it.tag == null) return@setOnMarkerClickListener false
                    markerViewState.onMarkerClicked.invoke(it.tag as Long)
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLng(it.position)
                    )
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
                    true
                }
            }
        }

        viewModel.viewEvent.observeEvent(this) { event ->
            when (event) {
                is MapEvent.OnMarkerClicked ->
                    MapBottomSheetFragment.newInstance().show(childFragmentManager, MapBottomSheetFragment.TAG)

                is MapEvent.Toast -> if (event.message != null) {
                    Toast.makeText(requireContext(), event.message.toCharSequence(requireContext()), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun requestPermissions() {
        when {
            // Permissions already granted
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) ==
                    PackageManager.PERMISSION_GRANTED ->
                viewModel.hasPermissionBeenGranted(true)

            // Permissions have been denied once - show rationale
            shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                    shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION) ->
                showRequestPermissionRationale()

            // Request permission
            else ->
                requestPermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
        }
    }

    private fun showRequestPermissionRationale() {
        // rationale should be shown only once
        val show = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.permission_rationale_title))
            .setMessage(getString(R.string.permission_rationale_message))
            .setPositiveButton(getString(R.string.permission_rationale_ok_btn)) { dialogInterface, _ ->
                // re-request permission
                requestPermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                dialogInterface.dismiss()
            }
            .show()
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