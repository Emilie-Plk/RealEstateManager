package com.emplk.realestatemanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emplk.realestatemanager.domain.add_property.PropertyEntity


@Database(
    entities = [
        PropertyEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

}