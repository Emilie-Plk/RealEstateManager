package com.emplk.realestatemanager.data.property

import com.emplk.realestatemanager.data.property.location.LocationDto
import com.emplk.realestatemanager.data.property.location.LocationMapper
import com.emplk.realestatemanager.data.property.picture.PictureDto
import com.emplk.realestatemanager.data.property.picture.PictureMapper
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import java.math.RoundingMode
import java.time.ZoneId
import javax.inject.Inject

class PropertyMapper @Inject constructor(
    private val locationMapper: LocationMapper,
    private val pictureMapper: PictureMapper,
) {

    fun mapToDto(property: PropertyEntity) = PropertyDto(
        id = property.id,
        type = property.type,
        price = property.price,
        surface = property.surface,
        rooms = property.rooms,
        bedrooms = property.bedrooms,
        bathrooms = property.bathrooms,
        description = property.description,
        amenitySchool = property.amenities.contains(AmenityType.SCHOOL),
        amenityPark = property.amenities.contains(AmenityType.PARK),
        amenityShopping = property.amenities.contains(AmenityType.SHOPPING_MALL),
        amenityRestaurant = property.amenities.contains(AmenityType.RESTAURANT),
        amenityConcierge = property.amenities.contains(AmenityType.CONCIERGE),
        amenityGym = property.amenities.contains(AmenityType.GYM),
        amenityTransportation = property.amenities.contains(AmenityType.PUBLIC_TRANSPORTATION),
        amenityHospital = property.amenities.contains(AmenityType.HOSPITAL),
        amenityLibrary = property.amenities.contains(AmenityType.LIBRARY),
        agentName = property.agentName,
        isSold = property.isSold,
        entryDate = property.entryDate,
        entryDateEpoch = property.entryDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        saleDate = property.saleDate,
        lastEditionDate = property.lastEditionDate,
    )

    fun mapToDomainEntity(
        propertyDto: PropertyDto,
        locationDto: LocationDto,
        pictureDtos: List<PictureDto>,
    ) = PropertyEntity(
        id = propertyDto.id,
        type = propertyDto.type,
        price = propertyDto.price.setScale(0, RoundingMode.HALF_UP),
        surface = propertyDto.surface.setScale(0, RoundingMode.HALF_UP),
        location = locationMapper.mapToDomainEntity(locationDto),
        pictures = pictureMapper.mapToDomainEntities(pictureDtos),
        amenities = mapAmenities(propertyDto),
        rooms = propertyDto.rooms,
        bedrooms = propertyDto.bedrooms,
        bathrooms = propertyDto.bathrooms,
        description = propertyDto.description,
        agentName = propertyDto.agentName,
        isSold = propertyDto.isSold,
        entryDate = propertyDto.entryDate,
        saleDate = propertyDto.saleDate,
        lastEditionDate = propertyDto.lastEditionDate,
    )

    private fun mapAmenities(propertyDto: PropertyDto): List<AmenityType> = buildList {
        if (propertyDto.amenitySchool) add(AmenityType.SCHOOL)
        if (propertyDto.amenityPark) add(AmenityType.PARK)
        if (propertyDto.amenityShopping) add(AmenityType.SHOPPING_MALL)
        if (propertyDto.amenityRestaurant) add(AmenityType.RESTAURANT)
        if (propertyDto.amenityConcierge) add(AmenityType.CONCIERGE)
        if (propertyDto.amenityGym) add(AmenityType.GYM)
        if (propertyDto.amenityTransportation) add(AmenityType.PUBLIC_TRANSPORTATION)
        if (propertyDto.amenityHospital) add(AmenityType.HOSPITAL)
        if (propertyDto.amenityLibrary) add(AmenityType.LIBRARY)
    }
}
