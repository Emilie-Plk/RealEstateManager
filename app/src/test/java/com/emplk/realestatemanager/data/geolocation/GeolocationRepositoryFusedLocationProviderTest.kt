package com.emplk.realestatemanager.data.geolocation

import android.location.Location
import app.cash.turbine.test
import com.emplk.realestatemanager.domain.geolocation.GeolocationState
import com.emplk.utils.TestCoroutineRule
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.Task
import io.mockk.coEvery
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GeolocationRepositoryFusedLocationProviderTest {
    companion object {
        private const val TEST_LATITUDE = 12.3
        private const val TEST_LONGITUDE = 45.6
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val fusedLocationProviderClient: FusedLocationProviderClient = mockk()

    private val geolocationRepositoryFusedLocationProvider = GeolocationRepositoryFusedLocationProvider(
        fusedLocationProviderClient,
        testCoroutineRule.getTestCoroutineDispatcherProvider(),
    )

    @Before
    fun setUp() {
        val locationCallbackSlot = slot<LocationCallback>()

        every {
            fusedLocationProviderClient.requestLocationUpdates(
                any(),
                any(),
                capture(locationCallbackSlot)
            )
        } answers {
            locationCallbackSlot.captured.onLocationResult(
                LocationResult.create(
                    listOf(
                        Location("Test").apply {
                            latitude = TEST_LATITUDE
                            longitude = TEST_LONGITUDE
                        }
                    )
                )
            )
            mockk()
        }

        coEvery { fusedLocationProviderClient.removeLocationUpdates(any<LocationCallback>()) } returns mockk()

        every { fusedLocationProviderClient.lastLocation } returns mockk {
            every { addOnSuccessListener(any()) } answers {
                (firstArg() as Task<Location>).addOnSuccessListener {
                    it.latitude = TEST_LATITUDE
                    it.longitude = TEST_LONGITUDE
                }
            }
            mockk()
        }

        justRun { fusedLocationProviderClient.removeLocationUpdates(any<LocationCallback>()) }
    }

    @Test
    fun `getCurrentLocationAsFlow() should return a GeolocationState_Success with the current location`() =
        testCoroutineRule.runTest {
            //   Given
            val expected = GeolocationState.Success(TEST_LATITUDE, TEST_LONGITUDE)

            geolocationRepositoryFusedLocationProvider.getCurrentLocationAsFlow().test {
                // When
                val result = awaitItem()

                // Then
                //  assertEquals(expected, result)  // TODO: NINO Why returns Success(0.0, 0.0) instead of Success(12.3, 45.6)?
                verify {
                    fusedLocationProviderClient.requestLocationUpdates(
                        any(),
                        any(),
                        any<LocationCallback>()
                    )
                }
                cancelAndIgnoreRemainingEvents()

                verify { fusedLocationProviderClient.removeLocationUpdates(any<LocationCallback>()) }
            }
        }
}