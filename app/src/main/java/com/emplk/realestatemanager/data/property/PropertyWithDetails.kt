package com.emplk.realestatemanager.data.property

import androidx.room.Embedded
import androidx.room.Relation
import com.emplk.realestatemanager.data.amenity.AmenityDto
import com.emplk.realestatemanager.data.location.LocationDto
import com.emplk.realestatemanager.data.picture.PictureDto

data class PropertyWithDetails(
    @Embedded
    val property: PropertyDto,
    @Relation(
        entity = PictureDto::class,
        parentColumn = "id",
        entityColumn = "property_id"
    ) val pictures: List<PictureDto>,
    @Relation(
        entity = LocationDto::class,
        parentColumn = "id",
        entityColumn = "property_id"
    ) val location: LocationDto,
    @Relation(
        entity = AmenityDto::class,
        parentColumn = "id",
        entityColumn = "property_id"
    ) val amenities: List<AmenityDto> = emptyList(),
)