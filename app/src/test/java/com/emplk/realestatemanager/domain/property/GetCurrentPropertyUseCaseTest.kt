package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.utils.TestCoroutineRule
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class GetCurrentPropertyUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val propertyRepository: PropertyRepository = mockk()
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase = mockk()

    private val getCurrentPropertyUseCase = GetCurrentPropertyUseCase(
        propertyRepository,
        getCurrentPropertyIdFlowUseCase,
    )

    @Before
    fun setUp() {
    }
}