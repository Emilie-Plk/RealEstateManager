package com.emplk.realestatemanager.ui.drafts

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
) : ViewModel() {

    val viewStates: LiveData<List<DraftViewState>> = liveData {
        emit(
            buildList {
                getAllDraftsWithTitleAndDateUseCase.invoke().forEach { form ->
                    add(
                        DraftViewState.DraftItem(
                            id = form.id,
                            title = mapTitleToNativeText(form.title),
                            lastEditionDate = mapLastEditionDateToNativeText(form.lastEditionDate),
                            lastEditionDateLocaleDateTime = form.lastEditionDate,
                            featuredPicture = form.featuredPicture?.let { NativePhoto.Uri(it) }
                                ?: NativePhoto.Resource(R.drawable.baseline_image_24),
                            featuredPictureDescription = form.featuredPictureDescription?.let { NativeText.Simple(it) },
                            onClick = EquatableCallback {
                                setCurrentPropertyIdUseCase.invoke(form.id)
                                setNavigationTypeUseCase.invoke(NavigationFragmentType.EDIT_FRAGMENT)
                            }
                        )
                    )
                }
                add(
                    DraftViewState.AddNewDraftItem(
                        text = NativeText.Resource(R.string.draft_add_new_draft),
                        icon = NativePhoto.Resource(R.drawable.baseline_add_home_24),
                        onClick = EquatableCallback {
                            setNavigationTypeUseCase.invoke(NavigationFragmentType.ADD_FRAGMENT)
                        }
                    )
                )
            }.sortedWith(
                compareBy<DraftViewState> { it.type != DraftViewState.Type.ADD_NEW_DRAFT }
                    .thenByDescending { draftViewState ->
                        when (draftViewState) {
                            is DraftViewState.DraftItem -> draftViewState.lastEditionDateLocaleDateTime
                                ?: LocalDateTime.MAX

                            else -> null
                        }
                    },
            )
        )
    }

    private fun mapTitleToNativeText(title: String?): NativeText = if (title != null) NativeText.Simple(title)
    else NativeText.Resource(R.string.draft_no_title)

    private fun mapLastEditionDateToNativeText(lastEditionDate: LocalDateTime?): NativeText =
        if (lastEditionDate != null) NativeText.Argument(
            R.string.draft_date_last_edition,
            lastEditionDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
        )
        else NativeText.Resource(R.string.draft_date_error)
}