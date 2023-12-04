package com.emplk.realestatemanager.data.geolocation

import android.location.Location
import com.emplk.utils.TestCoroutineRule
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.asExecutor
import org.junit.Before
import org.junit.Rule

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

        justRun {
            fusedLocationProviderClient.requestLocationUpdates(
                any(),
                testCoroutineRule.ioDispatcher.asExecutor(),
                capture(locationCallbackSlot)
            )
        }

        locationCallbackSlot.captured.onLocationResult(
            LocationResult.create(
                listOf(
                    Location("").apply {
                        latitude = TEST_LATITUDE
                        longitude = TEST_LONGITUDE
                    }
                )
            )
        )

        justRun {
            fusedLocationProviderClient.removeLocationUpdates(
                locationCallbackSlot.captured
            )
        }
    }
}

/*
    @Test
    fun `getCurrentLocationAsFlow() should return a GeolocationState_Success with the current location`() =
   testCoroutineRule.runTest {

     /*   Given
          val expected = GeolocationState.Success(TEST_LATITUDE, TEST_LONGITUDE)

          // When
          geolocationRepositoryFusedLocationProvider.getCurrentLocationAsFlow().test {
              val result = awaitItem()
              assertTrue(result is GeolocationState.Success)
              assert((result as GeolocationState.Success).latitude == expected.latitude)
              assert(result.longitude == expected.longitude)

              cancelAndIgnoreRemainingEvents()
          }
 }
}*/