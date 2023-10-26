package com.emplk.realestatemanager.data.property

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert
    suspend fun insert(propertyDto: PropertyDto): Long

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyWithDetails?>

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :propertyId")
    suspend fun getPropertyById(propertyId: Long): PropertyWithDetails

    @Transaction
    @Query("SELECT * FROM properties")
    fun getPropertiesWithDetailsAsFlow(): Flow<List<PropertyWithDetails>>

    @Update
    suspend fun update(propertyDto: PropertyDto): Int
}