package com.emplk.realestatemanager.domain.entities

import androidx.room.Embedded
import androidx.room.Relation

data class PropertiesWithPicturesAndLocationEntity(
    @Embedded
    val property: PropertyEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val pictures: List<PictureEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val location: LocationEntity,
)