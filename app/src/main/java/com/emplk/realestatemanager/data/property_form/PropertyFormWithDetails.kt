package com.emplk.realestatemanager.data.property_form

import androidx.room.Embedded
import androidx.room.Relation
import com.emplk.realestatemanager.data.property_form.amenity.AmenityFormDto
import com.emplk.realestatemanager.data.property_form.picture_preview.PicturePreviewFormDto

data class PropertyFormWithDetails(
    @Embedded
    val propertyForm: PropertyFormDto,
    @Relation(
        entity = PicturePreviewFormDto::class,
        parentColumn = "id",
        entityColumn = "property_form_id"
    ) val picturePreviews: List<PicturePreviewFormDto>,
    @Relation(
        entity = AmenityFormDto::class,
        parentColumn = "id",
        entityColumn = "property_form_id"
    ) val amenities: List<AmenityFormDto>,
)