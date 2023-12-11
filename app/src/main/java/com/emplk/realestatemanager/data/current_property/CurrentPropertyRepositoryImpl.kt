package com.emplk.realestatemanager.data.current_property

import com.emplk.realestatemanager.domain.current_property.CurrentPropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CurrentPropertyRepositoryImpl @Inject constructor() : CurrentPropertyRepository {

    private var currentPropertyIdMutableSharedFlow: MutableStateFlow<Long?> = MutableStateFlow(null)

    override fun getCurrentPropertyIdAsFlow(): Flow<Long?> = currentPropertyIdMutableSharedFlow

    override fun setCurrentPropertyId(id: Long) {
        currentPropertyIdMutableSharedFlow.tryEmit(id)
    }

    override fun resetCurrentPropertyId() {
        currentPropertyIdMutableSharedFlow.tryEmit(null)
    }
}