package com.emplk.realestatemanager.domain.property

import androidx.room.Embedded
import androidx.room.Relation
import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.location.LocationEntity
import com.emplk.realestatemanager.domain.pictures.PictureEntity

data class PropertyWithDetailsEntity(
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
    val amenities: List<AmenityEntity> = emptyList(),
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val location: LocationEntity,
)