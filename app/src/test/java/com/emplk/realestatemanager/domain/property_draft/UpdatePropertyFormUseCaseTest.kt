package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToUsdDependingOnLocaleUseCase
import com.emplk.realestatemanager.fixtures.getTestFormDraftParams
import com.emplk.realestatemanager.fixtures.testFixedClock
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class UpdatePropertyFormUseCaseTest {

    companion object {
        private val form = getTestFormDraftParams(1L)
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val formDraftRepository: FormDraftRepository = mockk()
    private val convertSurfaceToSquareFeetDependingOnLocaleUseCase: ConvertSurfaceToSquareFeetDependingOnLocaleUseCase =
        mockk()
    private val convertToUsdDependingOnLocaleUseCase: ConvertToUsdDependingOnLocaleUseCase = mockk()

    private val updatePropertyFormUseCase = UpdatePropertyFormUseCase(
        formDraftRepository,
        convertSurfaceToSquareFeetDependingOnLocaleUseCase,
        convertToUsdDependingOnLocaleUseCase,
        testFixedClock,
    )

    @Before
    fun setUp() {
        coJustRun {
            formDraftRepository.update(any())
        }
        every { convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(any()) } returns BigDecimal(500)
        coEvery { convertToUsdDependingOnLocaleUseCase.invoke(any()) } returns BigDecimal(1000000)
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        updatePropertyFormUseCase.invoke(form)

        // Then
        coVerify(exactly = 1) {
            formDraftRepository.update(any())
            convertToUsdDependingOnLocaleUseCase.invoke(BigDecimal(1000000))
        }
        verify { convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(BigDecimal(500)) }
    }
}