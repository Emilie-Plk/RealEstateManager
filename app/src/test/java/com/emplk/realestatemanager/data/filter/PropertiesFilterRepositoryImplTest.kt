package com.emplk.realestatemanager.data.filter

import app.cash.turbine.test
import com.emplk.realestatemanager.domain.filter.PropertiesFilterEntity
import com.emplk.utils.TestCoroutineRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class PropertiesFilterRepositoryImplTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val repository = PropertiesFilterRepositoryImpl()

    @Test
    fun `initial case`() =
        testCoroutineRule.runTest {
            repository.getPropertiesFilter().test {
                assertNull(awaitItem())
            }
        }

    @Test
    fun `set properties filter`() =
        testCoroutineRule.runTest {
            repository.getPropertiesFilter().test {
                // Given
                assertNull(awaitItem())

                // When... and Then
                repository.setPropertiesFilter(PropertiesFilterEntity())
                val firstCapturedEmission = awaitItem()
                assertEquals(PropertiesFilterEntity(), firstCapturedEmission)

                repository.setPropertiesFilter(
                    PropertiesFilterEntity(
                        minMaxPrice = Pair(
                            BigDecimal(1),
                            BigDecimal(2)
                        )
                    )
                )
                val secondCapturedEmission = awaitItem()
                assertEquals(
                    PropertiesFilterEntity(minMaxPrice = Pair(BigDecimal(1), BigDecimal(2))),
                    secondCapturedEmission
                )
            }
        }

    // TODO: test resetPropertiesFilter()

    @Test
    fun `reset properties filter`() = testCoroutineRule.runTest {
        repository.getPropertiesFilter().test {
            // Given
            val initialState = awaitItem()
            assertNull(initialState)

            // When
            repository.setPropertiesFilter(
                PropertiesFilterEntity(
                    minMaxPrice = Pair(BigDecimal(1), BigDecimal(2))
                )
            )
            val firstCapturedEmission = awaitItem()
            assertEquals(
                PropertiesFilterEntity(minMaxPrice = Pair(BigDecimal(1), BigDecimal(2))),
                firstCapturedEmission
            )

            repository.resetPropertiesFilter()

            // Then
            val reinitializeState = awaitItem()
            assertNull(reinitializeState)
        }
    }
}