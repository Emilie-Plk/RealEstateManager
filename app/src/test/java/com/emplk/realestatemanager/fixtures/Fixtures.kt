package com.emplk.realestatemanager.fixtures

import com.emplk.realestatemanager.data.agent.RealEstateAgent
import com.emplk.realestatemanager.data.property.PropertyDto
import com.emplk.realestatemanager.data.property.PropertyWithDetails
import com.emplk.realestatemanager.data.property.location.LocationDto
import com.emplk.realestatemanager.data.property.picture.PictureDto
import com.emplk.realestatemanager.data.property_draft.FormDraftDto
import com.emplk.realestatemanager.data.property_type.PropertyType
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.location.LocationEntity
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property_draft.FormDraftEntity
import com.emplk.realestatemanager.domain.property_draft.FormDraftParams
import com.emplk.realestatemanager.domain.property_draft.picture_preview.PicturePreviewEntity
import com.google.android.gms.maps.model.LatLng
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * A fixed clock that always returns 31 Oct 2023 13:22:35 GMT
 */
val testFixedClock: Clock =
    Clock.fixed(Instant.ofEpochMilli(1698758555000L), ZoneOffset.UTC)

// region PropertyDto

fun getTestPropertyDto(id: Long) = PropertyDto(
    id = id,
    type = "House",
    price = BigDecimal(1000000),
    surface = BigDecimal(500),
    rooms = 5,
    bedrooms = 3,
    bathrooms = 2,
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    amenitySchool = true,
    amenityPark = true,
    amenityShopping = true,
    amenityRestaurant = false,
    amenityConcierge = false,
    amenityGym = false,
    amenityTransportation = false,
    amenityHospital = false,
    amenityLibrary = false,
    agentName = "John Doe",
    saleDate = null,
    lastEditionDate = LocalDateTime.of(2023, 1, 1, 12, 0),
    entryDate = LocalDateTime.of(2023, 1, 1, 12, 0),
    entryDateEpoch = 1672570800000,
)


// region PropertyEntity

fun getTestPropertyEntity(id: Long) = PropertyEntity(
    id = id,
    type = "House",
    price = BigDecimal(1000000),
    surface = BigDecimal(500),
    location = LocationEntity(
        address = "1st, Dummy Street, 12345, Dummy City",
        miniatureMapUrl = "https://www.google.com/maps/123456789",
        latLng = LatLng(123.0, 456.0),
    ),
    rooms = 5,
    bedrooms = 3,
    bathrooms = 2,
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    agentName = "John Doe",
    pictures = buildList {
        add(
            PictureEntity(
                id = 1L,
                uri = "https://www.google.com/front_view",
                description = "Front view",
                isFeatured = true,
            )
        )
        add(
            PictureEntity(
                id = 2L,
                uri = "https://www.google.com/garden",
                description = "Garden",
                isFeatured = false,
            )
        )
        add(
            PictureEntity(
                id = 3L,
                uri = "https://www.google.com/swimming_pool",
                description = "Swimming pool",
                isFeatured = false,
            )
        )
    },
    amenities = buildList {
        add(AmenityType.SCHOOL)
        add(AmenityType.PARK)
        add(AmenityType.SHOPPING_MALL)
    },
    saleDate = null,
    lastEditionDate = LocalDateTime.of(2023, 1, 1, 12, 0),
    entryDate = LocalDateTime.of(2023, 1, 1, 12, 0),
)

fun getPropertyEntities(n: Long) = buildList {
    for (i in 1..n) {
        add(getTestPropertyEntity(i))
    }
}
// endregion PropertyEntity

// region PropertyWithDetail
fun getPropertyWithDetail(propertyId: Long) = PropertyWithDetails(
    property = getTestPropertyDto(propertyId),
    location = getTestLocationDto(propertyId),
    pictures = getPictureDtos(propertyId),
)
// endregion PropertyWithDetail


// region Location
fun getTestLocationDto(propertyId: Long) = LocationDto(
    propertyId = propertyId,
    address = "1st, Dummy Street, 12345, Dummy City",
    miniatureMapUrl = "https://www.google.com/maps/123456789",
    latitude = 123.0,
    longitude = 456.0,
)

fun getTestLocationEntity() = LocationEntity(
    address = "1st, Dummy Street, 12345, Dummy City",
    miniatureMapUrl = "https://www.google.com/maps/123456789",
    latLng = LatLng(123.0, 456.0),
)
// endregion Location

// region Picture
fun getPictureDtos(propertyId: Long) = buildList {
    add(
        PictureDto(
            propertyId = propertyId,
            uri = "https://www.google.com/front_view",
            description = "Front view",
            isFeatured = true,
        )
    )
    add(
        PictureDto(
            propertyId = propertyId,
            uri = "https://www.google.com/garden",
            description = "Garden",
            isFeatured = false,
        )
    )
    add(
        PictureDto(
            propertyId = propertyId,
            uri = "https://www.google.com/swimming_pool",
            description = "Swimming pool",
            isFeatured = false,
        )
    )
}

fun getTestPictureEntities() = listOf(
    PictureEntity(
        id = 1L,
        uri = "https://www.google.com/front_view",
        description = "Front view",
        isFeatured = true,
    ),
    PictureEntity(
        id = 2L,
        uri = "https://www.google.com/garden",
        description = "Garden",
        isFeatured = false,
    ),
    PictureEntity(
        id = 3L,
        uri = "https://www.google.com/swimming_pool",
        description = "Swimming pool",
        isFeatured = false,
    ),
)
// endregion Picture


// region mappers
fun mapPropertyEntityToDto(property: PropertyEntity) = PropertyDto(
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
    entryDate = property.entryDate,
    entryDateEpoch = property.entryDate.toEpochSecond(ZoneOffset.UTC),
    saleDate = property.saleDate,
    lastEditionDate = property.lastEditionDate,
)

// endregion mappers

// region FormDraftParams
fun getTestFormDraftParams(id: Long) = FormDraftParams(
    id = id,
    typeDatabaseName = "House",
    draftTitle = "House for sale in Dummy City",
    address = "1st, Dummy Street, 12345, Dummy City",
    isAddressValid = true,
    price = BigDecimal(1000000),
    surface = BigDecimal(500),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    nbRooms = 5,
    nbBathrooms = 2,
    nbBedrooms = 3,
    agent = "John Doe",
    selectedAmenities = buildList {
        add(AmenityType.SCHOOL)
        add(AmenityType.PARK)
        add(AmenityType.SHOPPING_MALL)
    },
    pictureIds = buildList {
        add(1L)
        add(2L)
        add(3L)
    },
    featuredPictureId = 1L,
    isSold = false,
    entryDate = LocalDateTime.of(2023, 1, 1, 12, 0),
    entryDateEpoch = 1698758555,
    soldDate = null,
    lastEditionDate = LocalDateTime.of(2023, 1, 1, 12, 0),
    formType = null,
)
// endregion FormDraftParams

// region FormDraftEntity
fun getTestFormDraftEntity(id: Long) = FormDraftEntity(
    id = id,
    type = "House",
    title = "House for sale in Dummy City",
    address = "1st, Dummy Street, 12345, Dummy City",
    isAddressValid = true,
    price = BigDecimal(1000000),
    surface = BigDecimal(500),
    description = "Test description",
    rooms = 5,
    bathrooms = 2,
    bedrooms = 3,
    agentName = "John Doe",
    amenities = buildList {
        add(AmenityType.SCHOOL)
        add(AmenityType.PARK)
        add(AmenityType.SHOPPING_MALL)
    },
    pictures = getTestPicturePreviewEntities(),
    entryDate = LocalDateTime.of(2023, 1, 1, 12, 0),
    saleDate = null,
    lastEditionDate = LocalDateTime.of(2023, 1, 1, 12, 0),
)
// endregion FormDraftEntity

// region FormDraftDto
fun getTestFormDraftDto(id: Long) = FormDraftDto(
    id = id,
    title = "House for sale in Dummy City",
    type = "House",
    price = BigDecimal(1000000),
    surface = BigDecimal(500),
    address = "1st, Dummy Street, 12345, Dummy City",
    isAddressValid = true,
    rooms = 5,
    bedrooms = 3,
    bathrooms = 2,
    description = "Test description",
    amenitySchool = true,
    amenityPark = true,
    amenityShopping = true,
    amenityRestaurant = false,
    amenityConcierge = false,
    amenityGym = false,
    amenityTransportation = false,
    amenityHospital = false,
    amenityLibrary = false,
    agentName = "John Doe",
    entryDate = LocalDateTime.of(2023, 1, 1, 12, 0),
    saleDate = null,
    lastEditionDate = LocalDateTime.of(2023, 1, 1, 12, 0),
)
// endregion FormDraftDto


fun getTestAgents() = listOf(
    RealEstateAgent.JOHN_DOE,
    RealEstateAgent.JANE_DOE,
    RealEstateAgent.JOHN_SMITH,
    RealEstateAgent.JANE_SMITH,
    RealEstateAgent.JOHN_WAYNE,
    RealEstateAgent.JANE_WAYNE,
)

fun getTestPropertyTypes() = listOf(
    PropertyType.HOUSE,
    PropertyType.FLAT,
    PropertyType.DUPLEX,
    PropertyType.PENTHOUSE,
    PropertyType.VILLA,
    PropertyType.MANOR,
    PropertyType.OTHER,
)

fun getTestPropertyTypesForFilter() = listOf(
    PropertyType.ALL,
    PropertyType.HOUSE,
    PropertyType.FLAT,
    PropertyType.DUPLEX,
    PropertyType.PENTHOUSE,
    PropertyType.VILLA,
    PropertyType.MANOR,
    PropertyType.OTHER,
)

fun getTestAmenities() = listOf(
    AmenityType.SCHOOL,
    AmenityType.PARK,
    AmenityType.SHOPPING_MALL,
    AmenityType.RESTAURANT,
    AmenityType.CONCIERGE,
    AmenityType.GYM,
    AmenityType.PUBLIC_TRANSPORTATION,
    AmenityType.HOSPITAL,
    AmenityType.LIBRARY,
)

fun getTestPicturePreviewEntities(): List<PicturePreviewEntity> = buildList {
    add(
        PicturePreviewEntity(
            id = 1L,
            uri = "https://www.google.com/front_view",
            description = "Front view",
            isFeatured = true,
        )
    )
    add(
        PicturePreviewEntity(
            id = 2L,
            uri = "https://www.google.com/garden",
            description = "Garden",
            isFeatured = false,
        )
    )
    add(
        PicturePreviewEntity(
            id = 3L,
            uri = "https://www.google.com/swimming_pool",
            description = "Swimming pool",
            isFeatured = false,
        )
    )
}

