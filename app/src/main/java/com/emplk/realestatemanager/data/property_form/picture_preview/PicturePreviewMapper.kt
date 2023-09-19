package com.emplk.realestatemanager.data.property_form.picture_preview

import com.emplk.realestatemanager.domain.property_form.picture_preview.PicturePreviewEntity
import javax.inject.Inject

class PicturePreviewMapper @Inject constructor() {

    fun mapToPicturePreviewEntity(picturePreviewDto: PicturePreviewDto): PicturePreviewEntity =
        PicturePreviewEntity(
            id = picturePreviewDto.id,
            uri = picturePreviewDto.uri,
            description = picturePreviewDto.description,
            isFeatured = picturePreviewDto.isFeatured,
        )

    fun mapToPicturePreviewDto(picturePreviewEntity: PicturePreviewEntity): PicturePreviewDto =
        PicturePreviewDto(
            id = picturePreviewEntity.id,
            uri = picturePreviewEntity.uri,
            description = picturePreviewEntity.description,
            isFeatured = picturePreviewEntity.isFeatured,
        )

}