package com.emplk.realestatemanager.data.property_draft

import androidx.room.Embedded
import androidx.room.Relation
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftDto
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewDto

data class FormDraftWithDetails(
    @Embedded
    val propertyForm: FormDraftDto,
    @Relation(
        entity = PicturePreviewDto::class,
        parentColumn = "id",
        entityColumn = "property_draft_id"
    ) val picturePreviews: List<PicturePreviewDto>,
    @Relation(
        entity = AmenityDraftDto::class,
        parentColumn = "id",
        entityColumn = "property_draft_id"
    ) val amenities: List<AmenityDraftDto>,
)