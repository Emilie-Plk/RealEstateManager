package com.emplk.realestatemanager.ui.drafts

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
import io.mockk.justRun
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DraftItemViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getAllDraftsWithTitleAndDateUseCase: GetAllDraftsWithTitleAndDateUseCase = mockk()
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase = mockk()
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase = mockk()

    private lateinit var viewModel: DraftsViewModel

    @Before
    fun setUp() {
        coEvery { getAllDraftsWithTitleAndDateUseCase.invoke() } returns formsWithDetails
        justRun { setCurrentPropertyIdUseCase.invoke(any()) }
        justRun { setNavigationTypeUseCase.invoke(any()) }
        viewModel = DraftsViewModel(
            getAllDraftsWithTitleAndDateUseCase,
            setCurrentPropertyIdUseCase,
            setNavigationTypeUseCase,
        )
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        viewModel.viewStates.observeForTesting(this) {
            assertEquals(draftItemViewStates, it.value)
            // First item is the "Add new draft" item
            assertTrue(it.value!!.first() is DraftViewState.AddNewDraftItem)
            assertTrue(it.value!![1] is DraftViewState.DraftItem)
            assertTrue(it.value!![2] is DraftViewState.DraftItem)
            assertTrue(it.value!![3] is DraftViewState.DraftItem)

            assertEquals(2L, (it.value!![1] as DraftViewState.DraftItem).id)
            assertEquals(3L, (it.value!![2] as DraftViewState.DraftItem).id)
            assertEquals(1L, (it.value!![3] as DraftViewState.DraftItem).id)

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
            lastEditionDate = NativeText.Resource(R.string.draft_date_error),
            lastEditionDateLocaleDateTime = null,
            featuredPicture = NativePhoto.Uri("featuredPicture 2"),
            featuredPictureDescription = NativeText.Simple("featuredPictureDescription 2"),
            onClick = EquatableCallback {}
        ),
        DraftViewState.DraftItem(
            id = 3,
            title = NativeText.Simple("Title 3"),
            lastEditionDate = NativeText.Argument(
                R.string.draft_date_last_edition,
                LocalDateTime.of(2023, 3, 3, 3, 3).format(
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                )
            ),
            lastEditionDateLocaleDateTime = LocalDateTime.of(2023, 3, 3, 3, 3),
            featuredPicture = NativePhoto.Resource(R.drawable.baseline_image_24),
            featuredPictureDescription = null,
            onClick = EquatableCallback {}
        ),
        DraftViewState.DraftItem(
            id = 1,
            title = NativeText.Simple("Title 1"),
            lastEditionDate = NativeText.Argument(
                R.string.draft_date_last_edition,
                LocalDateTime.of(2023, 1, 1, 1, 1).format(
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                )

            ),
            lastEditionDateLocaleDateTime = LocalDateTime.of(2023, 1, 1, 1, 1),
            featuredPicture = NativePhoto.Uri("featuredPicture 1"),
            featuredPictureDescription = NativeText.Simple("featuredPictureDescription 1"),
            onClick = EquatableCallback {}
        ),
    )
}