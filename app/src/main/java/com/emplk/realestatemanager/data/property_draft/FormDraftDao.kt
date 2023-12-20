package com.emplk.realestatemanager.data.property_draft

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import java.math.BigDecimal
import java.time.LocalDateTime

@Dao
interface FormDraftDao {
    @Insert
    suspend fun insert(formDraftDto: FormDraftDto): Long

    @Transaction
    @Query("SELECT * FROM base_form_drafts WHERE id = :propertyFormId LIMIT 1")
    suspend fun getPropertyFormById(propertyFormId: Long): FormDraftWithDetails

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM base_form_drafts WHERE entry_date IS NULL")
    suspend fun getFormsWithFeaturedPicture(): List<FormWithTitleDateAndFeaturedPicture>

    @Query("SELECT COUNT(*) FROM base_form_drafts WHERE id NOT IN (SELECT id FROM properties)")
    suspend fun getAddPropertyDraftsCount(): Int

    @Query("SELECT EXISTS(SELECT * FROM base_form_drafts WHERE id = :propertyFormId)")
    suspend fun doesDraftExist(propertyFormId: Long): Boolean

    @Query("SELECT EXISTS(SELECT * FROM properties WHERE id = :propertyFormId)")
    suspend fun doesPropertyExist(propertyFormId: Long): Boolean

    @Query(
        "SELECT id FROM base_form_drafts WHERE type IS NULL " +
                "AND title IS NULL " +
                "AND price = '0.0' " +
                "AND surface = '0.0' " +
                "AND address IS NULL " +
                "AND rooms = 0 AND bedrooms = 0 " +
                "AND bathrooms = 0 " +
                "AND description IS NULL " +
                "AND amenity_school = 0 " +
                "AND amenity_park = 0 " +
                "AND amenity_shopping = 0 " +
                "AND amenity_restaurant = 0 " +
                "AND amenity_concierge = 0 " +
                "AND amenity_gym = 0 " +
                "AND amenity_transportation = 0 " +
                "AND amenity_hospital = 0 " +
                "AND amenity_library = 0 " +
                "AND agent_name IS NULL " +
                "AND entry_date IS NULL " +
                "LIMIT 1"
    )
    suspend fun getEmptyDraftId(): Long?

    @Query(
        "UPDATE base_form_drafts SET type = :newType," +
                " title = :newTitle," +
                " price = :newPrice," +
                " surface = :newSurface," +
                " address = :newAddress," +
                " rooms = :newRooms," +
                " bedrooms = :newBedrooms," +
                " bathrooms = :newBathrooms," +
                " description = :newDescription," +
                " amenity_school = :newAmenitySchool," +
                " amenity_park = :newAmenityPark," +
                " amenity_shopping = :newAmenityShopping," +
                " amenity_restaurant = :newAmenityRestaurant," +
                " amenity_concierge = :newAmenityConcierge," +
                " amenity_gym = :newAmenityGym," +
                " amenity_transportation = :newAmenityTransportation," +
                " amenity_hospital = :newAmenityHospital," +
                " amenity_library = :newAmenityLibrary," +
                " agent_name = :newAgentName," +
                " sale_date = :newSaleDate," +
                " entry_date = :newEntryDate," +
                " last_edition_date = :lastEditionDate WHERE id = :propertyFormId"
    )
    suspend fun update(
        newType: String?,
        newTitle: String?,
        newPrice: BigDecimal,
        newSurface: BigDecimal,
        newAddress: String?,
        newRooms: Int?,
        newBedrooms: Int?,
        newBathrooms: Int?,
        newDescription: String?,
        newAmenitySchool: Boolean,
        newAmenityPark: Boolean,
        newAmenityShopping: Boolean,
        newAmenityRestaurant: Boolean,
        newAmenityConcierge: Boolean,
        newAmenityGym: Boolean,
        newAmenityTransportation: Boolean,
        newAmenityHospital: Boolean,
        newAmenityLibrary: Boolean,
        newAgentName: String?,
        newEntryDate: LocalDateTime?,
        newSaleDate: LocalDateTime?,
        lastEditionDate: LocalDateTime?,
        propertyFormId: Long,
    )

    @Query("UPDATE base_form_drafts SET is_address_valid = :isAddressValid WHERE id = :propertyFormId")
    suspend fun updateAddressValidity(propertyFormId: Long, isAddressValid: Boolean)

    @Query("DELETE FROM base_form_drafts WHERE id = :propertyFormId")
    suspend fun delete(propertyFormId: Long): Int?
}