package com.emplk.realestatemanager.domain.property

import androidx.room.Embedded
import androidx.room.Relation
import com.emplk.realestatemanager.domain.location.LocationEntity
import com.emplk.realestatemanager.domain.pictures.PictureEntity

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