package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionViewState
import java.math.BigDecimal

data class AddPropertyFormEntity(
    val propertyType: String? = null,
    val address: String? = null,
    val addressPredictions: List<PredictionViewState> = emptyList(),
    val price: BigDecimal = BigDecimal.ZERO,
    val surface: String? = null,
    val description: String? = null,
    val nbRooms: Int = 0,
    val nbBathrooms: Int = 0,
    val nbBedrooms: Int = 0,
    val agent: String? = null,
    val amenities: List<AmenityEntity> = emptyList(),
    val pictureIds: List<Long> = emptyList(),
    val featuredPictureId: Long? = null,
)