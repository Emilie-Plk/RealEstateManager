package com.emplk.realestatemanager.data.permission

import app.cash.turbine.test
import com.emplk.utils.TestCoroutineRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class PermissionRepositoryImplTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val permissionRepository = PermissionRepositoryImpl()

    @Test
    fun `getLocationPermission should return null when permission is not set`() = testCoroutineRule.runTest {
        // When
        permissionRepository.getLocationPermission().test {
            // Then
            assertEquals(null, awaitItem())
        }
    }

    @Test
    fun `getLocationPermission should return false when permission is not set`() = testCoroutineRule.runTest {
        // When
        permissionRepository.setLocationPermission(false)
        permissionRepository.getLocationPermission().test {

            // Then
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `getLocationPermission should return true when permission is given`() = testCoroutineRule.runTest {
        // When
        permissionRepository.setLocationPermission(true)

        permissionRepository.getLocationPermission().test {

            // Then
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `getLocationPermission change to false to true`() = testCoroutineRule.runTest {
        permissionRepository.getLocationPermission().test {
            // Given
            assertNull(awaitItem())

            // When... and Then
            permissionRepository.setLocationPermission(false)
            val firstCapturedEmission = awaitItem()
            assertEquals(false, firstCapturedEmission)

            permissionRepository.setLocationPermission(true)
            val secondCapturedEmission = awaitItem()
            assertEquals(true, secondCapturedEmission)
        }
    }
}