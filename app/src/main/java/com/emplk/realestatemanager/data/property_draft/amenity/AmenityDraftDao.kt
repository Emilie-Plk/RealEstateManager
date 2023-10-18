package com.emplk.realestatemanager.data.property_draft.amenity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AmenityDraftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(amenityDraftDto: AmenityDraftDto): Long

    @Insert
    suspend fun insertAll(amenityDraftDtos: List<AmenityDraftDto>): List<Long?>

    @Query("SELECT * FROM amenities_draft")
    fun getAllAsFlow(): Flow<List<AmenityDraftDto>>

    @Query("SELECT id FROM amenities_draft WHERE property_draft_id = :propertyFormId")
    suspend fun getAllIds(propertyFormId: Long): List<Long>

    @Query("DELETE FROM amenities_draft WHERE id = :amenityFormId")
    suspend fun delete(amenityFormId: Long): Int

    @Query("DELETE FROM amenities_draft WHERE property_draft_id = :propertyFormId")
    suspend fun deleteAll(propertyFormId: Long): Int?
}