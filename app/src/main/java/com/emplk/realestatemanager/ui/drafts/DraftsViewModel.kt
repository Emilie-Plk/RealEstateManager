package com.emplk.realestatemanager.ui.drafts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.GetAllDraftsWithTitleAndDateUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import dagger.hilt.android.lifecycle.HiltViewModel
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
                drafts = getAllDraftsWithTitleAndDateUseCase.invoke().filterNot { it.title.isNullOrBlank() }.map {
                    DraftViewStateItem(
                        id = it.id,
                        title = it.title!!,
                        lastEditionDate = it.lastEditionDate?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
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
}