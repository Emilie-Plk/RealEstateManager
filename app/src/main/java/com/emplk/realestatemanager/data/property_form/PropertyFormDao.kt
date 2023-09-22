package com.emplk.realestatemanager.data.property_form

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyFormDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(propertyFormDto: PropertyFormDto): Long

    @Transaction
    @Query("SELECT * FROM property_forms WHERE id = :propertyFormId")
    fun getPropertyFormById(propertyFormId: Long): Flow<PropertyFormWithDetails>

    @Query("SELECT EXISTS(SELECT 1 FROM property_forms)")
    suspend fun exists(): Boolean

    @Query("SELECT id FROM property_forms LIMIT 1")
    suspend fun getExistingPropertyFormId(): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(propertyFormDto: PropertyFormDto): Int

    @Query("DELETE FROM property_forms WHERE id = :propertyFormId")
    suspend fun delete(propertyFormId: Long)
}