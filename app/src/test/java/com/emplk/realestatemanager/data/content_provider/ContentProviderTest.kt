package com.emplk.realestatemanager.data.content_provider

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.emplk.realestatemanager.data.property.PropertyDao
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.picture.PictureDao
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ContentProviderTest {
    private lateinit var contentProvider: ContentProvider
    private lateinit var propertyDaoTest: PropertyDao
    private lateinit var pictureDaoTest: PictureDao
    private lateinit var locationDaoTest: LocationDao
    private lateinit var contentResolver: ContentResolver
    private val testAuthority = "com.emplk.realestatemanager.data.content_provider.ContentProvider"
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        contentResolver = mockk()

        propertyDaoTest = mockk()
        pictureDaoTest = mockk()
        locationDaoTest = mockk()

        contentProvider = ContentProvider().apply {
            propertyDao = propertyDaoTest
            pictureDao = pictureDaoTest
            locationDao = locationDaoTest
        }

        val fakePropertyCursor: Cursor = mockk()
        every { propertyDaoTest.getAllPropertiesWithCursor() } returns fakePropertyCursor
        justRun {
            fakePropertyCursor.setNotificationUri(any(), any())
        }
    }

    @Test
    fun `query returns Cursor based on URI`() {
        // Given
        val testCursor: Cursor = mockk()
        justRun {
            testCursor.setNotificationUri(
                any(),
                any()
            )
        }
        every { propertyDaoTest.getAllPropertiesWithCursor() } returns testCursor

        // When
        val uri = Uri.parse("content://$testAuthority/properties")
        val resultCursor = contentProvider.query(uri, null, null, null, null)

        // Then
        verify(exactly = 1) { propertyDaoTest.getAllPropertiesWithCursor() }
        verify(exactly = 1) { resultCursor.setNotificationUri(any(), any()) }

        assertEquals(testCursor, resultCursor)
    }
}

