package com.emplk.realestatemanager.data.connectivity

import android.app.Application
import android.content.BroadcastReceiver
import android.location.LocationManager
import app.cash.turbine.test
import com.emplk.utils.TestCoroutineRule
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GpsConnectivityRepositoryBroadcastReceiverTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val application: Application = mockk()
    private val locationManager: LocationManager = mockk()

    private val repository = GpsConnectivityRepositoryBroadcastReceiver(
        application,
        locationManager
    )

    @Before
    fun setUp() {
        every { application.registerReceiver(any(), any()) } returns mockk()
        justRun { application.unregisterReceiver(any()) }
        every { locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) } returns true
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // Given
        val gpsConnectivityRepositoryBroadcastReceiver = GpsConnectivityRepositoryBroadcastReceiver(
            application,
            locationManager
        )

        // When
        gpsConnectivityRepositoryBroadcastReceiver.isGpsEnabledAsFlow().test {
            // Then
            assertEquals(true, awaitItem())

            verify { application.registerReceiver(any(), any()) }

            cancel()
            verify { application.unregisterReceiver(any()) }
        }
    }

    @Test
    fun `gps enabled - then disabled`() = testCoroutineRule.runTest {
        // Given
        val broadcastReceiverSlot = slot<BroadcastReceiver>()

        every { application.registerReceiver(capture(broadcastReceiverSlot), any()) } returns mockk()

        every { locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) } returnsMany listOf(
            true,
            false
        )

        repository.isGpsEnabledAsFlow().test {
            // When 1
            val firstCapturedEmission = awaitItem()
            // Then 2
            assertEquals(true, firstCapturedEmission)

            broadcastReceiverSlot.captured.onReceive(mockk(), mockk())


            // When 2
            val secondCapturedEmission = awaitItem()
            // Then 2
            assertEquals(false, secondCapturedEmission)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `gps enabled callbackFlow called thrice with same value won't re-trigger broadcast receiver`() =
        testCoroutineRule.runTest {
            // Given
            val broadcastReceiverSlot = slot<BroadcastReceiver>()

            every { application.registerReceiver(capture(broadcastReceiverSlot), any()) } returns mockk()

            every { locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) } returnsMany listOf(
                true,
                true,
                true
            )

            repository.isGpsEnabledAsFlow().test {
                // When 1
                val firstCapturedEmission = awaitItem()
                // Then 2
                assertEquals(true, firstCapturedEmission)

                broadcastReceiverSlot.captured.onReceive(mockk(), mockk())

                verify(exactly = 1) { application.registerReceiver(any(), any()) }

                cancelAndIgnoreRemainingEvents()
            }
        }
}