package com.emplk.realestatemanager.data.add_property

import com.emplk.realestatemanager.domain.add_property.AddPropertyRepository
import com.emplk.realestatemanager.domain.amenity.AmenityType
import javax.inject.Inject

class AddPropertyRepositoryImpl @Inject constructor() : AddPropertyRepository {
    override fun setPropertyType(propertyType: String) {
        TODO("Not yet implemented")
    }

    override fun setPropertyAddress(propertyPrice: String) {
        TODO("Not yet implemented")
    }

    override fun setPropertyPrice(propertyPrice: String) {
        TODO("Not yet implemented")
    }

    override fun setPropertySurface(propertySurface: String) {
        TODO("Not yet implemented")
    }

    override fun setRoomsNumber(roomsNumber: String) {
        TODO("Not yet implemented")
    }

    override fun setBedroomsNumber(bedroomsNumber: String) {
        TODO("Not yet implemented")
    }

    override fun setBathroomsNumber(bathroomsNumber: String) {
        TODO("Not yet implemented")
    }

    override fun setPropertyDescription(propertyDescription: String) {
        TODO("Not yet implemented")
    }

    override fun setPropertyAmenities(propertyAmenities: List<AmenityType>) {
        TODO("Not yet implemented")
    }

    override fun setPropertyPictureUris(propertyPictureUris: List<String>) {
        TODO("Not yet implemented")
    }

    override fun setPropertyAgent(propertyAgent: String) {
        TODO("Not yet implemented")
    }
}