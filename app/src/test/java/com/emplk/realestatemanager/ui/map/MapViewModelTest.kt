package com.emplk.realestatemanager.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.map.GetAllPropertiesLatLongUseCase
import com.emplk.realestatemanager.domain.property.location.PropertyLatLongEntity
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.utils.TestCoroutineRule
import com.emplk.utils.observeForTesting
import com.google.android.gms.maps.model.LatLng
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MapViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getAllPropertiesLatLongUseCase: GetAllPropertiesLatLongUseCase = mockk()
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase = mockk()

    private lateinit var mapViewModel: MapViewModel

    @Before
    fun setUp() {
        coEvery { getAllPropertiesLatLongUseCase.invoke() } returns testPropertiesLatLongEntities
        every { setCurrentPropertyIdUseCase.invoke(any()) } returns Unit
        mapViewModel = MapViewModel(
            getAllPropertiesLatLongUseCase,
            setCurrentPropertyIdUseCase,
        )
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // Given
        mapViewModel.viewState.observeForTesting(this) {
            assertThat(it.value).isEqualTo(testMarkersViewStates)
        }
        // When
        // Then
    }

    @Test
    fun `viewEvent initial case`() = testCoroutineRule.runTest {
        // When

        mapViewModel.viewEvent.observeForTesting(this) {
            // Then
            assertThat(it.value).isEqualTo(null)
        }
    }

    @Test
    fun `viewEvent onMarkerClicked case`() = testCoroutineRule.runTest {
        // Given
        mapViewModel.viewState.observeForTesting(this) { viewState ->
            mapViewModel.viewEvent.observeForTesting(this) { event ->
                // When
                viewState.value!!.first().onMarkerClicked.invoke(1L)
                runCurrent()

                // Then
                assertThat(event.value).isEqualTo(Event(MapEvent.OnMarkerClicked))
            }
        }
    }

    private val testPropertiesLatLongEntities = buildList {
        add(
            PropertyLatLongEntity(
                propertyId = 1L,
                latLng = LatLng(0.0, 0.0),
            )
        )

        add(
            PropertyLatLongEntity(
                propertyId = 2L,
                latLng = LatLng(1.0, 1.0)
            )
        )

        add(
            PropertyLatLongEntity(
                propertyId = 3L,
                latLng = LatLng(2.0, 2.0)
            )
        )
    }

    private val testMarkersViewStates = buildList {
        add(
            MarkerViewState(
                propertyId = 1L,
                latLng = LatLng(0.0, 0.0),
                onMarkerClicked = EquatableCallbackWithParam { },
            )
        )

        add(
            MarkerViewState(
                propertyId = 2L,
                latLng = LatLng(1.0, 1.0),
                onMarkerClicked = EquatableCallbackWithParam { },
            )
        )

        add(
            MarkerViewState(
                propertyId = 3L,
                latLng = LatLng(2.0, 2.0),
                onMarkerClicked = EquatableCallbackWithParam { },
            )
        )
    }
}
