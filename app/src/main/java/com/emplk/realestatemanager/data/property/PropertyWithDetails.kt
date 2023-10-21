package com.emplk.realestatemanager.data.property

import androidx.room.Embedded
import androidx.room.Relation
import com.emplk.realestatemanager.data.property.location.LocationDto
import com.emplk.realestatemanager.data.property.picture.PictureDto

data class PropertyWithDetails(
    @Embedded
    val property: PropertyDto,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    ) val pictures: List<PictureDto>,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    ) val location: LocationDto?,
)