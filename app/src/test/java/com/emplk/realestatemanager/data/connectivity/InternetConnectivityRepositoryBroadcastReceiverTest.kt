package com.emplk.realestatemanager.data.connectivity

import android.app.Application
import android.content.BroadcastReceiver
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.emplk.utils.TestCoroutineRule
import io.mockk.Called
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InternetConnectivityRepositoryBroadcastReceiverTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val application: Application = mockk()
    private val connectivityManager: ConnectivityManager = mockk()
    private var currentVersionCode: Int = Build.VERSION_CODES.M

    private val internetConnectivityRepositoryBroadcastReceiver = InternetConnectivityRepositoryBroadcastReceiver(
        application,
        connectivityManager,
        currentVersionCode
    )

    @Before
    fun setup() {
        currentVersionCode = Build.VERSION_CODES.M

        every { connectivityManager.activeNetwork } returns mockk {
            every { connectivityManager.getNetworkCapabilities(this@mockk) } returns mockk {
                every { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
                every { hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
            }
        }

        every { application.registerReceiver(any(), any()) } returns mockk()
        justRun { application.unregisterReceiver(any()) }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        internetConnectivityRepositoryBroadcastReceiver.isInternetEnabledAsFlow().test {
            // When
            val result = awaitItem()

            // Then
            assertTrue(result)
            verify(exactly = 1) {
                connectivityManager.getNetworkCapabilities(any())
                application.registerReceiver(any(), any())
            }


            // When 2
            cancel()

            // Then 2
            verify { application.unregisterReceiver(any()) }
        }
    }

    @Test
    fun `edge case - lost wifi and no cellular data`() = testCoroutineRule.runTest {
        // Given
        val broadcastReceiverSlot = slot<BroadcastReceiver>()

        every { application.registerReceiver(capture(broadcastReceiverSlot), any()) } returns mockk()

        every { connectivityManager.activeNetwork } returnsMany listOf(
            mockk {
                every { connectivityManager.getNetworkCapabilities(this@mockk) } returns mockk {
                    every { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
                    every { hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
                }
            },
            mockk {
                every { connectivityManager.getNetworkCapabilities(this@mockk) } returns mockk {
                    every { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
                    every { hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
                }
            }
        )

        internetConnectivityRepositoryBroadcastReceiver.isInternetEnabledAsFlow().test {
            // When 1
            val firstCapturedEmission = awaitItem()

            // Then 1
            assertTrue(firstCapturedEmission)

            broadcastReceiverSlot.captured.onReceive(mockk(), mockk())

            // When 2
            val secondCapturedEmission = awaitItem()

            // Then 2
            assertFalse(secondCapturedEmission)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `with Android version Lollipop 21`() = testCoroutineRule.runTest {
        // Given
        currentVersionCode = Build.VERSION_CODES.LOLLIPOP

        internetConnectivityRepositoryBroadcastReceiver.isInternetEnabledAsFlow().test {
            // When
            val result = awaitItem()

            // Then
            assertTrue(result)
            verify(exactly = 1) {
                connectivityManager.activeNetwork
            }
            verify { connectivityManager.getNetworkCapabilities(any())?.wasNot(Called) }

            verify(exactly = 1) { application.registerReceiver(any(), any()) }

            // When 2
            cancel()

            // Then 2
            verify { application.unregisterReceiver(any()) }
        }
    }
}
