package com.emplk.realestatemanager.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.geolocation.GeolocationState
import com.emplk.realestatemanager.domain.geolocation.GetCurrentLocationUseCase
import com.emplk.realestatemanager.domain.map.GetAllPropertiesLatLongUseCase
import com.emplk.realestatemanager.domain.permission.SetLocationPermissionUseCase
import com.emplk.realestatemanager.domain.property.location.PropertyLatLongAndSoldStatusEntity
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativeText
import com.emplk.utils.TestCoroutineRule
import com.emplk.utils.observeForTesting
import com.google.android.gms.maps.model.LatLng
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MapViewModelTest {

    companion object {
        private val FALLBACK_LOCATION_UPPER_EAST_SIDE = LatLng(40.7736, -73.9566)
        private val TEST_USER_LOCATION = LatLng(1.2, 3.4)
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getAllPropertiesLatLongUseCase: GetAllPropertiesLatLongUseCase = mockk()
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase = mockk()
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase = mockk()
    private val setLocationPermissionUseCase: SetLocationPermissionUseCase = mockk()

    private lateinit var viewModel: MapViewModel

    @Before
    fun setUp() {
        coEvery { getAllPropertiesLatLongUseCase.invoke() } returns flowOf(testPropertiesLatLongEntities)
        justRun { setCurrentPropertyIdUseCase.invoke(any()) }
        coEvery { getCurrentLocationUseCase.invoke() } returns flowOf(
            GeolocationState.Success(
                TEST_USER_LOCATION.latitude, TEST_USER_LOCATION.longitude
            )
        )
        viewModel = MapViewModel(
            getAllPropertiesLatLongUseCase,
            setCurrentPropertyIdUseCase,
            getCurrentLocationUseCase,
            setLocationPermissionUseCase,
        )
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        viewModel.viewState.observeForTesting(this) {
            assertThat(it.value).isEqualTo(testMarkerViewState)
        }
    }

    @Test
    fun `viewEvent initial case`() = testCoroutineRule.runTest {
        // When

        viewModel.viewEvent.observeForTesting(this) {
            // Then
            assertThat(it.value).isEqualTo(null)
        }
    }

    @Test
    fun `viewEvent onMarkerClicked case`() = testCoroutineRule.runTest {
        // Given
        viewModel.viewState.observeForTesting(this) { viewState ->
            viewModel.viewEvent.observeForTesting(this) { event ->
                // When
                viewState.value!!.propertyMarkers.first().onMarkerClicked.invoke(1L)
                runCurrent()

                // Then
                verify { setCurrentPropertyIdUseCase.invoke(1L) } // TODO: change that
                assertThat(event.value).isEqualTo(Event(MapEvent.OnMarkerClicked))
            }
        }
    }

    @Test
    fun `when NoLocationAvailable - userCurrentLocation should be null`() = testCoroutineRule.runTest {
        // Given
        coEvery { getCurrentLocationUseCase.invoke() } returns flowOf(
            GeolocationState.Error(null)
        )
        // When
        viewModel.viewState.observeForTesting(this) { viewState ->

            // Then
            assertThat(viewState.value!!.userCurrentLocation).isEqualTo(null)
        }
    }

    @Test
    fun `when NoLocationWithMissingPermission - userCurrentLocation should be null`() = testCoroutineRule.runTest {
        // Given
        coEvery { getCurrentLocationUseCase.invoke() } returns flowOf(
            GeolocationState.Error(NativeText.Simple("Missing permissions"))
        )
        // When
        viewModel.viewState.observeForTesting(this) { viewState ->
            runCurrent()
            // Then
            assertThat(viewState.value!!.userCurrentLocation).isEqualTo(null)
        }
    }

    @Test
    fun `when NoLocationWithMissingPermission - location is found and permission granted - userCurrentLocation should not be null`() =
        testCoroutineRule.runTest {
            // Given
            every { getCurrentLocationUseCase.invoke() } returns flowOf(
                GeolocationState.Error(NativeText.Simple("Missing permissions"))
            )

            // ...and...
            every { getCurrentLocationUseCase.invoke() } returns flowOf(
                GeolocationState.Success(
                    TEST_USER_LOCATION.latitude, TEST_USER_LOCATION.longitude
                )
            )

            getCurrentLocationUseCase.invoke().test {
                val capturedGeolocationState = awaitItem()
                assertThat(capturedGeolocationState).isEqualTo(
                    GeolocationState.Success(
                        TEST_USER_LOCATION.latitude, TEST_USER_LOCATION.longitude
                    )
                )
                awaitComplete()
            }

            // When
            viewModel.viewState.observeForTesting(this)
            { viewState ->

                // Then
                assertThat(viewState.value!!.userCurrentLocation).isEqualTo(TEST_USER_LOCATION)
                coVerify(exactly = 2) { getCurrentLocationUseCase.invoke() }
                confirmVerified(getCurrentLocationUseCase)
            }
        }

    @Test
    fun `if no properties latLong - should not display property markers`() = testCoroutineRule.runTest {
        // Given
        coEvery { getAllPropertiesLatLongUseCase.invoke() } returns flowOf(emptyList())

        // When
        viewModel.viewState.observeForTesting(this) { viewState ->

            // Then
            assertThat(viewState.value!!.propertyMarkers).isEqualTo(emptyList())
            coVerify(exactly = 1) {
                getAllPropertiesLatLongUseCase.invoke()
                getAllPropertiesLatLongUseCase.invoke()
            }
            confirmVerified(getAllPropertiesLatLongUseCase)
        }
    }

    private val testPropertiesLatLongEntities = buildList {
        add(
            PropertyLatLongAndSoldStatusEntity(
                propertyId = 1L,
                latLng = LatLng(0.0, 0.0),
                isSold = false,
            )
        )

        add(
            PropertyLatLongAndSoldStatusEntity(
                propertyId = 2L,
                latLng = LatLng(1.0, 1.0),
                isSold = false,
            )
        )

        add(
            PropertyLatLongAndSoldStatusEntity(
                propertyId = 3L,
                latLng = LatLng(2.0, 2.0),
                isSold = true
            )
        )
    }

    private val testMarkerViewState = MarkerViewState(
        userCurrentLocation = TEST_USER_LOCATION,
        fallbackLocationGoogleHq = FALLBACK_LOCATION_UPPER_EAST_SIDE,
        propertyMarkers = testPropertiesLatLongEntities.map { propertyLatLong ->
            PropertyMarkerViewState(
                propertyId = propertyLatLong.propertyId,
                latLng = propertyLatLong.latLng,
                isSold = propertyLatLong.isSold,
                onMarkerClicked = EquatableCallbackWithParam {},
            )
        }
    )
}
