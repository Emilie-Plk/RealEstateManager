package com.emplk.realestatemanager.ui.drafts

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.GetAllDraftsWithTitleAndDateUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@HiltViewModel
class DraftsViewModel @Inject constructor(
    private val getAllDraftsWithTitleAndDateUseCase: GetAllDraftsWithTitleAndDateUseCase,
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val resources: Resources,
) : ViewModel() {
    val viewStates: LiveData<DraftViewState> = liveData {
        emit(
            DraftViewState(
                drafts = getAllDraftsWithTitleAndDateUseCase.invoke().map { form -> // ptet des flows ici
                    DraftViewStateItem(
                        id = form.id,
                        title = mapTitleToNativeText(form.title),
                        lastEditionDate = mapToString(form.lastEditionDate),
                        featuredPicture = form.featuredPicture?.let { NativePhoto.Uri(it) }
                            ?: NativePhoto.Resource(R.drawable.baseline_image_24),
                        featuredPictureDescription = form.featuredPictureDescription?.let { NativeText.Simple(it) },
                        onClick = EquatableCallback {
                            setCurrentPropertyIdUseCase.invoke(form.id)
                            setNavigationTypeUseCase.invoke(NavigationFragmentType.EDIT_FRAGMENT)
                        }
                    )
                }.sortedByDescending { it.lastEditionDate },
                onAddNewDraftClicked = EquatableCallback { setNavigationTypeUseCase.invoke(NavigationFragmentType.ADD_FRAGMENT) },
            )
        )
    }

    private fun mapTitleToNativeText(title: String?): NativeText {
        if (title != null) return NativeText.Simple(title)
        else return NativeText.Resource(R.string.draft_no_title)
    }

    private fun mapToString(lastEditionDate: LocalDateTime?): String =
        if (lastEditionDate != null) resources.getString(
            R.string.draft_date_last_edition,
            lastEditionDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
        )
        else resources.getString(R.string.draft_date_error)
}