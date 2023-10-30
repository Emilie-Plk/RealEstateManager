package com.emplk.realestatemanager.ui.detail

import com.emplk.utils.TestCoroutineRule
import com.emplk.realestatemanager.domain.property.GetCurrentPropertyUseCase
import io.mockk.mockk
import org.junit.Rule


class DetailViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    private val getCurrentPropertyUseCase: GetCurrentPropertyUseCase = mockk()

}