package com.emplk.realestatemanager.data.picture

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {

    @Insert
    suspend fun insert(pictureDtoEntity: PictureDtoEntity)

    @Query("SELECT uri FROM pictures WHERE property_id = :propertyId")
    fun getPicturesAsFlow(propertyId: Long): Flow<List<String>>

    @Update
    suspend fun update(pictureDtoEntity: PictureDtoEntity): Int

    @Query("DELETE FROM pictures WHERE id = :pictureId")
    suspend fun delete(pictureId: Long): Int
}