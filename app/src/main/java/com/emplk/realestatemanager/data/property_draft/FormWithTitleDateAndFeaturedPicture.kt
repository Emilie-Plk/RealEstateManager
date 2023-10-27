package com.emplk.realestatemanager.data.property_draft

import androidx.room.Embedded
import androidx.room.Relation
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewDto

data class FormWithTitleDateAndFeaturedPicture(
    @Embedded
    val formWithTitleAndLastEditionDate: FormWithTitleAndLastEditionDate,
    @Relation(
        entity = PicturePreviewDto::class,
        parentColumn = "id",
        entityColumn = "property_draft_id"
    ) val featuredPicture: PicturePreviewDto?,
)