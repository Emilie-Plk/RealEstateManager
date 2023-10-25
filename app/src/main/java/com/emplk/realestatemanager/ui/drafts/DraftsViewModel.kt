package com.emplk.realestatemanager.ui.drafts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.property_draft.GetAllDraftsWithTitleAndDateUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@HiltViewModel
class DraftsViewModel @Inject constructor(
    private val getAllDraftsWithTitleAndDateUseCase: GetAllDraftsWithTitleAndDateUseCase,
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase,
) : ViewModel() {

    private val onAddDraftClickedMutableSharedFlow = MutableSharedFlow<Unit>()


    val eventLiveData: LiveData<Event<DraftsEvent>> = liveData {

    }

    val viewStates: LiveData<DraftViewState> = liveData {
        emit(
            DraftViewState(
                drafts = getAllDraftsWithTitleAndDateUseCase.invoke().map {
                    DraftViewStateItem(
                        id = it.id,
                        title = it.title,
                        lastEditionDate = it.lastEditionDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                        onClick = EquatableCallback { setCurrentPropertyIdUseCase.invoke(it.id) }
                    )
                },
                onAddNewDraftClicked = EquatableCallback { onAddDraftClickedMutableSharedFlow.tryEmit(Unit) },
            )
        )
    }
}