package com.emplk.realestatemanager.ui.drafts

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.FormWithDetailEntity
import com.emplk.realestatemanager.domain.property_draft.GetAllDraftsWithTitleAndDateUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import com.emplk.utils.TestCoroutineRule
import com.emplk.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.test.runCurrent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class DraftItemViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getAllDraftsWithTitleAndDateUseCase: GetAllDraftsWithTitleAndDateUseCase = mockk()
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase = mockk()
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase = mockk()
    private val resources: Resources = mockk()

    private lateinit var viewModel: DraftsViewModel

    @Before
    fun setUp() {
        coEvery { getAllDraftsWithTitleAndDateUseCase.invoke() } returns formsWithDetails
        justRun { setCurrentPropertyIdUseCase.invoke(any()) }
        justRun { setNavigationTypeUseCase.invoke(any()) }
        every { resources.getString(R.string.draft_add_new_draft) } returns "Add new draft"
        every {
            resources.getString(
                R.string.draft_date_last_edition,
                "01/01/2023 01:01"
            )
        } returns "Last edited on: 01/01/2023 01:01"
        every {
            resources.getString(
                R.string.draft_date_last_edition,
                "03/03/2023 03:03"
            )
        } returns "Last edited on: 03/03/2023 03:03"
        every { resources.getString(R.string.draft_date_last_edition, null) } returns "No date recorded"
        every { resources.getString(R.string.draft_date_error) } returns "No date recorded"

        viewModel = DraftsViewModel(
            getAllDraftsWithTitleAndDateUseCase,
            setCurrentPropertyIdUseCase,
            setNavigationTypeUseCase,
            resources
        )
    }

    // TODO: NINO pourquoi ça marche en isolé mais pas en groupé ???
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        viewModel.viewStates.observeForTesting(this) {
            runCurrent()
            assertEquals(draftItemViewStates, it.value)
            coVerify(exactly = 1) { getAllDraftsWithTitleAndDateUseCase.invoke() }
            confirmVerified(getAllDraftsWithTitleAndDateUseCase)
        }
    }

    private val formsWithDetails: List<FormWithDetailEntity> = listOf(
        FormWithDetailEntity(
            id = 1,
            title = "Title 1",
            lastEditionDate = LocalDateTime.of(2023, 1, 1, 1, 1),
            featuredPicture = "featuredPicture 1",
            featuredPictureDescription = "featuredPictureDescription 1"
        ),
        FormWithDetailEntity(
            id = 2,
            title = "Title 2",
            lastEditionDate = null,
            featuredPicture = "featuredPicture 2",
            featuredPictureDescription = "featuredPictureDescription 2"
        ),
        FormWithDetailEntity(
            id = 3,
            title = "Title 3",
            lastEditionDate = LocalDateTime.of(2023, 3, 3, 3, 3),
            featuredPicture = null,
            featuredPictureDescription = null
        )
    )

    private val draftItemViewStates: List<DraftViewState> = listOf(
        DraftViewState.AddNewDraftItem(
            text = NativeText.Resource(R.string.draft_add_new_draft),
            icon = NativePhoto.Resource(R.drawable.baseline_add_home_24),
            onClick = EquatableCallback {}
        ),
        DraftViewState.DraftItem(
            id = 2,
            title = NativeText.Simple("Title 2"),
            lastEditionDate = "No date recorded",
            featuredPicture = NativePhoto.Uri("featuredPicture 2"),
            featuredPictureDescription = NativeText.Simple("featuredPictureDescription 2"),
            onClick = EquatableCallback {}
        ),
        DraftViewState.DraftItem(
            id = 3,
            title = NativeText.Simple("Title 3"),
            lastEditionDate = "Last edited on: 03/03/2023 03:03",
            featuredPicture = NativePhoto.Resource(R.drawable.baseline_image_24),
            featuredPictureDescription = null,
            onClick = EquatableCallback {}
        ),
        DraftViewState.DraftItem(
            id = 1,
            title = NativeText.Simple("Title 1"),
            lastEditionDate = "Last edited on: 01/01/2023 01:01",
            featuredPicture = NativePhoto.Uri("featuredPicture 1"),
            featuredPictureDescription = NativeText.Simple("featuredPictureDescription 1"),
            onClick = EquatableCallback {}
        ),
    )
}