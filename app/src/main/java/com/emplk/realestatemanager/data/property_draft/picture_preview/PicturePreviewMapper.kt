package com.emplk.realestatemanager.data.property_draft.picture_preview

import com.emplk.realestatemanager.domain.property_draft.picture_preview.PicturePreviewEntity
import javax.inject.Inject

class PicturePreviewMapper @Inject constructor() {

    fun mapToPicturePreviewEntity(picturePreviewDto: PicturePreviewDto): PicturePreviewEntity =
        PicturePreviewEntity(
            id = picturePreviewDto.id,
            uri = picturePreviewDto.uri,
            description = picturePreviewDto.description,
            isFeatured = picturePreviewDto.isFeatured,
        )

    fun mapToPicturePreviewDto(
        picturePreviewEntity: PicturePreviewEntity,
        propertyFormId: Long
    ): PicturePreviewDto =
        PicturePreviewDto(
            id = picturePreviewEntity.id,
            propertyFormId = propertyFormId,
            uri = picturePreviewEntity.uri,
            description = picturePreviewEntity.description,
            isFeatured = picturePreviewEntity.isFeatured,
        )

    fun mapToPicturePreviewEntities(picturePreviewDtos: List<PicturePreviewDto>): List<PicturePreviewEntity> =
        picturePreviewDtos.map { mapToPicturePreviewEntity(it) }

    fun mapToPicturePreviewDtos(pictures: List<PicturePreviewEntity>, id: Long): List<PicturePreviewDto> =
        pictures.map { mapToPicturePreviewDto(it, id) }
}