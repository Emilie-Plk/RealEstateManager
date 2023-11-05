package com.emplk.realestatemanager.data.connectivity

class InternetConnectivityRepositoryBroadcastReceiverTest {

    /*  @get:Rule
      val testCoroutineRule = TestCoroutineRule()

      private val application: Application = mockk()
      private val connectivityManager: ConnectivityManager = mockk()
      private val internetConnectivityRepositoryBroadcastReceiver =
          InternetConnectivityRepositoryBroadcastReceiver(application)

      // TODO NINO: help lol
      @Before
      fun setup() {
        every { application.getSystemService<ConnectivityManager>() } returns connectivityManager
          every { connectivityManager.activeNetworkInfo } returns mockk {
              every { isConnected } returns true
              every { connectivityManager.activeNetwork } returns mockk()
              every { connectivityManager.getNetworkCapabilities(any()) } returns mockk()
          }
      }

      @Test
      fun `test internet connection status`() = testCoroutineRule.runTest {
         // When
          every { connectivityManager.activeNetwork } returns null
          every { connectivityManager.getNetworkCapabilities(any()) } returns mockk {
              every { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
              every { hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
          }

          // Call the isInternetEnabledAsFlow function and collect the result
          val flow = internetConnectivityRepositoryBroadcastReceiver.isInternetEnabledAsFlow()
          val result = flow.take(2).toList()

          // Assert that the result is as expected
          assertEquals(listOf(false, false), result)
      }*/
}