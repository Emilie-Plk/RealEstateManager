package com.emplk.realestatemanager.data.connectivity

import android.app.Application
import android.net.ConnectivityManager
import android.os.Build
import com.emplk.utils.TestCoroutineRule
import io.mockk.mockk
import org.junit.Rule

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

    private val application: Application = mockk()
    private val connectivityManager: ConnectivityManager = mockk()
    private var currentVersionCode: Int = Build.VERSION_CODES.M

    private val internetConnectivityRepositoryBroadcastReceiver = InternetConnectivityRepositoryBroadcastReceiver(
        application,
        connectivityManager,
        currentVersionCode
    )

    /*  @Before
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
              awaitComplete()

              broadcastReceiverSlot.captured.onReceive(mockk(), mockk())

              val secondResult = awaitItem()
              cancelAndIgnoreRemainingEvents()

              // Then
              assertThat(firstResult).isTrue()
              assertThat(secondResult).isFalse()
          }
      }*/
}
