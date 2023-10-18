package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import java.math.BigDecimal

/**
 * This class represents the form state of a property.
 * It's not used outside of the UI/Domain layer.
 */
data class FormDraftStateEntity(
    val id: Long = 0L,
    val propertyType: String? = null,
    val address: String? = null,
    val isAddressValid: Boolean = false,
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