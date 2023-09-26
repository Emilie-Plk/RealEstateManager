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
            propertyFormId = propertyFormId,
            uri = picturePreviewEntity.uri,
            description = picturePreviewEntity.description,
            isFeatured = picturePreviewEntity.isFeatured,
        )

    fun mapToPicturePreviewEntities(picturePreviewFormDtos: List<PicturePreviewFormDto>): List<PicturePreviewEntity> =
        picturePreviewFormDtos.map { mapToPicturePreviewEntity(it) }

    fun mapToPicturePreviewDtos(pictures: List<PicturePreviewEntity>, id: Long): List<PicturePreviewFormDto> =
        pictures.map { mapToPicturePreviewDto(it, id) }
}