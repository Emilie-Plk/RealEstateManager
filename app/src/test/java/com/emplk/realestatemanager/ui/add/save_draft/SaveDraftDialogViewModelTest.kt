package com.emplk.realestatemanager.ui.add.save_draft

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emplk.realestatemanager.data.property_draft.FormTypeAndTitleEntity
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.draft.ClearPropertyFormNavigationUseCase
import com.emplk.realestatemanager.domain.navigation.draft.SaveDraftNavigationUseCase
import com.emplk.realestatemanager.domain.property_draft.GetFormTypeAndTitleAsFlowUseCase
import com.emplk.realestatemanager.domain.property_draft.ResetFormParamsUseCase
import com.emplk.realestatemanager.domain.property_draft.SetFormTitleUseCase
import com.emplk.realestatemanager.ui.add.FormType
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SaveDraftDialogViewModelTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val setNavigationTypeUseCase: SetNavigationTypeUseCase = mockk()
    private val saveDraftNavigationUseCase: SaveDraftNavigationUseCase = mockk()
    private val setFormTitleUseCase: SetFormTitleUseCase = mockk()
    private val getFormTypeAndTitleAsFlowUseCase: GetFormTypeAndTitleAsFlowUseCase = mockk()
    private val resetFormParamsUseCase: ResetFormParamsUseCase = mockk()
    private val clearPropertyFormNavigationUseCase: ClearPropertyFormNavigationUseCase = mockk()

    private lateinit var viewModel: SaveDraftDialogViewModel

    @Before
    fun setUp() {
        justRun { setNavigationTypeUseCase.invoke(any()) }
        justRun { saveDraftNavigationUseCase.invoke() }
        justRun { setFormTitleUseCase.invoke(any(), any()) }
        coEvery { getFormTypeAndTitleAsFlowUseCase.invoke() } returns flowOf(
            FormTypeAndTitleEntity(
                formType = FormType.ADD,
                title = "Test property"
            )
        )
        justRun { resetFormParamsUseCase.invoke() }
        justRun { clearPropertyFormNavigationUseCase.invoke() }

        viewModel = SaveDraftDialogViewModel(
            setNavigationTypeUseCase,
            saveDraftNavigationUseCase,
            setFormTitleUseCase,
            getFormTypeAndTitleAsFlowUseCase,
            resetFormParamsUseCase,
            clearPropertyFormNavigationUseCase
        )
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {

    }
}