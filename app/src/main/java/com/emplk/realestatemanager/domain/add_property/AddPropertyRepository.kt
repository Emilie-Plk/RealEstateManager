package com.emplk.realestatemanager.domain.add_property

import com.emplk.realestatemanager.domain.amenity.AmenityType

interface AddPropertyRepository {
    fun setPropertyType(propertyType: String)
    fun setPropertyAddress(propertyPrice: String)
    fun setPropertyPrice(propertyPrice: String)
    fun setPropertySurface(propertySurface: String)
    fun setRoomsNumber(roomsNumber: String)
    fun setBedroomsNumber(bedroomsNumber: String)
    fun setBathroomsNumber(bathroomsNumber: String)
    fun setPropertyDescription(propertyDescription: String)
    fun setPropertyAmenities(propertyAmenities: List<AmenityType>)
    fun setPropertyPictureUris(propertyPictureUris: List<String>)
    fun setPropertyAgent(propertyAgent: String)
}
