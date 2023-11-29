package com.emplk.realestatemanager.data.connectivity

import android.app.Application
import android.content.BroadcastReceiver
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.emplk.utils.TestCoroutineRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.delay
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InternetConnectivityRepositoryBroadcastReceiverTest {

    companion object {
        // Android versions to test to check if is version higher or equal than Build.VERSION_CODES.M
        // from API 21 to API 31
        private val ANDROID_VERSIONS = listOf(
            Build.VERSION_CODES.M,
            Build.VERSION_CODES.N,
            Build.VERSION_CODES.N_MR1,
            Build.VERSION_CODES.O,
            Build.VERSION_CODES.O_MR1,
            Build.VERSION_CODES.P,
            Build.VERSION_CODES.Q,
            Build.VERSION_CODES.R,
            Build.VERSION_CODES.S,
        )
    }

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
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        internetConnectivityRepositoryBroadcastReceiver.isInternetEnabledAsFlow().test {
            val result = awaitItem()
            cancelAndIgnoreRemainingEvents()

            // Then
            assertTrue(result)
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

        // When... and Then
        internetConnectivityRepositoryBroadcastReceiver.isInternetEnabledAsFlow().test {
            val firstCapturedEmission = awaitItem()
            println("First emission: $firstCapturedEmission")


            broadcastReceiverSlot.captured.onReceive(mockk(), mockk())

            val secondCapturedEmission = awaitItem()
            println("Second emission: $secondCapturedEmission")
            awaitComplete()

            assertTrue(firstCapturedEmission)
            assertFalse(secondCapturedEmission)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
