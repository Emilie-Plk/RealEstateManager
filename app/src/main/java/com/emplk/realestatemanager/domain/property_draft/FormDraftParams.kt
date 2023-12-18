package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.ui.add.FormType
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * This class represents the form state of a property.
 * It's not used outside of the UI/Domain layer.
 */
data class FormDraftParams(
    val id: Long = 0L,
    val typeDatabaseName: String? = null,
    val draftTitle: String? = null,
    val address: String? = null,
    val isAddressValid: Boolean = false,
    val price: BigDecimal = BigDecimal.ZERO,
    val surface: BigDecimal = BigDecimal.ZERO,
    val description: String? = null,
    val nbRooms: Int = 0,
    val nbBathrooms: Int = 0,
    val nbBedrooms: Int = 0,
    val agent: String? = null,
    val selectedAmenities: List<AmenityType> = emptyList(),
    val pictureIds: List<Long> = emptyList(),
    val featuredPictureId: Long? = null,
    val isSold: Boolean = false,
    val entryDate: LocalDateTime? = null,
    val entryDateEpoch: Long? = null,
    val soldDate: LocalDateTime? = null,
    val lastEditionDate: LocalDateTime? = null,
    val formType: FormType? = null,
)