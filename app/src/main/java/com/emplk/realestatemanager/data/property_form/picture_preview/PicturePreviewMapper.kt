package com.emplk.realestatemanager.data.property_form.picture_preview

import com.emplk.realestatemanager.domain.property_form.picture_preview.PicturePreviewEntity
import javax.inject.Inject

class PicturePreviewMapper @Inject constructor() {

    fun mapToPicturePreviewEntity(picturePreviewFormDto: PicturePreviewFormDto): PicturePreviewEntity =
        PicturePreviewEntity(
            id = picturePreviewFormDto.id,
            uri = picturePreviewFormDto.uri,
            description = picturePreviewFormDto.description,
            isFeatured = picturePreviewFormDto.isFeatured,
        )

    fun mapToPicturePreviewDto(
        picturePreviewEntity: PicturePreviewEntity,
        propertyFormId: Long
    ): PicturePreviewFormDto =
        PicturePreviewFormDto(
            id = picturePreviewEntity.id,
            propertyFormId = propertyFormId,
            uri = picturePreviewEntity.uri,
            description = picturePreviewEntity.description,
            isFeatured = picturePreviewEntity.isFeatured,
        )

}