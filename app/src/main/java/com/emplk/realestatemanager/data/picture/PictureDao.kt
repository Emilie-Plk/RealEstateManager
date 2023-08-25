package com.emplk.realestatemanager.data.picture

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.emplk.realestatemanager.domain.pictures.PictureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {

    @Insert
    suspend fun insert(picture: PictureEntity)

    @Query("SELECT * FROM pictures WHERE property_id = :propertyId")
    fun getPicturesAsFlow(propertyId: Long): Flow<List<PictureEntity>>

    @Update
    suspend fun update(picture: PictureEntity): Int

    @Query("DELETE FROM pictures WHERE id = :pictureId")
    suspend fun delete(pictureId: Long): Int
}