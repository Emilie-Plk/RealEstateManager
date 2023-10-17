package com.emplk.realestatemanager.data.property_draft

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import java.math.BigDecimal

@Dao
interface FormDraftDao {
    @Insert
    suspend fun insert(baseFormDraftDto: BaseFormDraftDto): Long

    @Transaction
    @Query("SELECT * FROM base_form_drafts WHERE id = :propertyFormId LIMIT 1")
    suspend fun getPropertyFormById(propertyFormId: Long): FormDraftWithDetails

    @Transaction
    @Query("SELECT * FROM base_form_drafts LIMIT 1")
    suspend fun getExistingPropertyForm(): FormDraftWithDetails?

    @Query("SELECT id FROM base_form_drafts LIMIT 1")
    suspend fun getExistingPropertyFormId(): Long?  // TODO: to change

    @Query("SELECT id FROM base_form_drafts WHERE id NOT IN (SELECT id FROM properties) LIMIT 1")
    suspend fun getAddFormId(): Long?

    @Query("SELECT EXISTS(SELECT * FROM base_form_drafts WHERE id = :propertyFormId)")
    suspend fun doesPropertyDraftExist(propertyFormId: Long): Boolean

    @Query("SELECT EXISTS(SELECT * FROM base_form_drafts WHERE id = :propertyFormId AND id = :propertyFormId IN (SELECT id FROM properties))")
    suspend fun doesPropertyExistInBothTables(propertyFormId: Long): Boolean

    @Query(
        "UPDATE base_form_drafts SET type = :newType," +
                " price = :newPrice," +
                " surface = :newSurface," +
                " address = :newAddress," +
                " rooms = :newRooms," +
                " bedrooms = :newBedrooms," +
                " bathrooms = :newBathrooms," +
                " description = :newDescription," +
                " agentName = :newAgentName WHERE id = :propertyFormId"
    )
    suspend fun update(
        newType: String?,
        newPrice: BigDecimal,
        newSurface: String?,
        newAddress: String?,
        newRooms: Int?,
        newBedrooms: Int?,
        newBathrooms: Int?,
        newDescription: String?,
        newAgentName: String?,
        propertyFormId: Long
    ): Int

    @Query("UPDATE base_form_drafts SET is_address_valid = :isAddressValid WHERE id = :propertyFormId")
    suspend fun updateIsAddressValid(propertyFormId: Long, isAddressValid: Boolean)

    @Query("DELETE FROM base_form_drafts WHERE id = :propertyFormId")
    suspend fun delete(propertyFormId: Long): Int?
}