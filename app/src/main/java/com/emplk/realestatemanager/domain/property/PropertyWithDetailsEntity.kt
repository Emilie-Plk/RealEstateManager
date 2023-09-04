package com.emplk.realestatemanager.domain.property

import androidx.room.Embedded
import androidx.room.Relation
import com.emplk.realestatemanager.data.property.PropertyDtoEntity
import com.emplk.realestatemanager.data.amenity.AmenityDtoEntity
import com.emplk.realestatemanager.data.location.LocationDtoEntity
import com.emplk.realestatemanager.data.picture.PictureDtoEntity

data class PropertyWithDetailsEntity(
    @Embedded
    val property: PropertyDtoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val pictures: List<PictureDtoEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val amenities: List<AmenityDtoEntity> = emptyList(),
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val location: LocationDtoEntity,
)