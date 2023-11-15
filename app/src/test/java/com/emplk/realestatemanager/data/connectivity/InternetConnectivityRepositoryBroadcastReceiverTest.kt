package com.emplk.realestatemanager.data.connectivity

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.work.impl.utils.ForceStopRunnable.BroadcastReceiver
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.emplk.utils.TestCoroutineRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InternetConnectivityRepositoryBroadcastReceiverTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val application: Application = mockk()
    private val connectivityManager: ConnectivityManager = mockk()
    private val internetConnectivityRepositoryBroadcastReceiver = InternetConnectivityRepositoryBroadcastReceiver(
        application,
        connectivityManager
    )

    @Before
    fun setup() {
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
            assertThat(result).isTrue()
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

        // When
        internetConnectivityRepositoryBroadcastReceiver.isInternetEnabledAsFlow().test {
            val firstResult = awaitItem()

            broadcastReceiverSlot.captured.onReceive(mockk(), mockk())

            val secondResult = awaitItem()
            cancelAndIgnoreRemainingEvents()

            // Then
            assertThat(firstResult).isTrue()
            assertThat(secondResult).isFalse()
        }
    }
}
