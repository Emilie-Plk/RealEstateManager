package com.emplk.realestatemanager.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.property.GetPropertyByItsIdAsFlowUseCase
import com.emplk.realestatemanager.domain.property_draft.AddPropertyFormEntity
import com.emplk.realestatemanager.ui.add.PropertyFormViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EditPropertyViewModel @Inject constructor(
    private val getPropertyByItsIdAsFlowUseCase: GetPropertyByItsIdAsFlowUseCase,
) : ViewModel() {

    private val formMutableStateFlow = MutableStateFlow(AddPropertyFormEntity())

    private val viewState : LiveData<PropertyFormViewState> = liveData {

    }

}