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
    val viewStates: LiveData<DraftViewState> = liveData {
        emit(
            DraftViewState(
                drafts = getAllDraftsWithTitleAndDateUseCase.invoke().map { // ptet des flows ici
                    DraftViewStateItem(
                        id = it.id,
                        title = mapTitleToNativeText(it.title),
                        lastEditionDate = mapDateToNativeText(it.entryDate, it.lastEditionDate),
                        onClick = EquatableCallback {
                            setCurrentPropertyIdUseCase.invoke(it.id)
                            setNavigationTypeUseCase.invoke(NavigationFragmentType.EDIT_FRAGMENT)
                        }
                    )
                },
                onAddNewDraftClicked = EquatableCallback { setNavigationTypeUseCase.invoke(NavigationFragmentType.ADD_FRAGMENT) },
            )
        )
    }

    private fun mapTitleToNativeText(title: String?): NativeText {
   if (title != null) return NativeText.Simple(title)
        else return NativeText.Resource(R.string.draft_no_title)
    }

    private fun mapDateToNativeText(entryDate: LocalDateTime?, lastEditionDate: LocalDateTime?): NativeText {
      if (lastEditionDate == null && entryDate != null) return NativeText.Argument(
              R.string.draft_date_creation,
              entryDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
          )
        else if (lastEditionDate != null) return NativeText.Argument(
            R.string.draft_date_last_edition,
            lastEditionDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        )
        else return NativeText.Resource(R.string.draft_date_creation)
    }
}