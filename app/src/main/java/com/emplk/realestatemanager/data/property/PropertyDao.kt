package com.emplk.realestatemanager.data.property

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.emplk.realestatemanager.domain.add_property.PropertyEntity

@Dao
interface PropertyDao {

    // CRUD ! Create, Read, Update, Delete
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(propertyEntity: PropertyEntity): Long

    @Query("SELECT * FROM properties WHERE id = :propertyId")
    suspend fun getPropertyById(propertyId: Long): PropertyEntity

    @Update
    suspend fun update(propertyEntity: PropertyEntity): Int

    @Query("DELETE FROM properties WHERE id = :propertyId")
    suspend fun delete(propertyId: Long): Int

    @Query
}