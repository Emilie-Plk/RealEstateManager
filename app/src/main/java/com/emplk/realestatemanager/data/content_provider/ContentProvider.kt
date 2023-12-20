package com.emplk.realestatemanager.data.content_provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.util.Log
import com.emplk.realestatemanager.data.property.PropertyDao
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.picture.PictureDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject


class ContentProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY =
            "com.emplk.realestatemanager.data.content_provider.com.emplk.realestatemanager.data.content_provider.ContentProvider"
        private const val TABLE_NAME = "RealEstateManager_database"
        private const val MIME_TYPE_PREFIX = "vnd.android.cursor.dir/vnd."

        private const val PROPERTIES = 1
        private const val PICTURES = 2
        private const val LOCATIONS = 3
        private const val PROPERTY_BY_ID = 4
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, "properties", PROPERTIES)
        addURI(AUTHORITY, "pictures", PICTURES)
        addURI(AUTHORITY, "locations", LOCATIONS)
        addURI(AUTHORITY, "properties/#", PROPERTY_BY_ID)
    }

    @Inject
    lateinit var entryPoint: ContentProviderEntryPoint
    private lateinit var propertyDao: PropertyDao
    private lateinit var pictureDao: PictureDao
    private lateinit var locationDao: LocationDao

    override fun onCreate(): Boolean {
        val appContext = context?.applicationContext ?: throw IllegalStateException("PropertyDao is null")
        val hiltEntryPoint = EntryPointAccessors.fromApplication(appContext, ContentProviderEntryPoint::class.java)
        propertyDao = hiltEntryPoint.getPropertyDao()
        pictureDao = hiltEntryPoint.getPictureDao()
        locationDao = hiltEntryPoint.getLocationDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val match = uriMatcher.match(uri)
        return try {
            when (match) {
                PROPERTIES -> propertyDao.getAllPropertiesWithCursor()
                PICTURES -> pictureDao.getAllPicturesWithCursor()
                LOCATIONS -> locationDao.getAllLocationsWithCursor()
                PROPERTY_BY_ID -> propertyDao.getPropertyByIdWithCursor(propertyId = uri.lastPathSegment!!.toLong())
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }.apply {
                setNotificationUri(context?.contentResolver, uri)
            }
        } catch (e: SQLiteException) {
            Log.e("ContentProvider", "Error querying URI: $uri", e)
            MatrixCursor(arrayOf("error_message")).apply {
                addRow(arrayOf(e.message ?: "Unknown error!"))
            }
        }
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            PROPERTIES -> "$MIME_TYPE_PREFIX$AUTHORITY.$TABLE_NAME"
            PICTURES -> "$MIME_TYPE_PREFIX$AUTHORITY.$TABLE_NAME"
            LOCATIONS -> "$MIME_TYPE_PREFIX$AUTHORITY.$TABLE_NAME"
            PROPERTY_BY_ID -> "$MIME_TYPE_PREFIX$AUTHORITY.$TABLE_NAME"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int =
        0

    @EntryPoint  // runtime dependency graph
    @InstallIn(SingletonComponent::class)
    interface ContentProviderEntryPoint {
        fun getPropertyDao(): PropertyDao
        fun getPictureDao(): PictureDao
        fun getLocationDao(): LocationDao
    }
}

