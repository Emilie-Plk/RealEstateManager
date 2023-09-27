package com.emplk.realestatemanager.domain.geocoding

import javax.inject.Inject

class GetAddressLatLongUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository
) {
    suspend fun invoke(address: String): GeocodingWrapper = geocodingRepository.getLatLong(address)
}