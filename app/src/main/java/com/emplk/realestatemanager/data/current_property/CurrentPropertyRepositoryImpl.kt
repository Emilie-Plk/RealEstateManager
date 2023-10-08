package com.emplk.realestatemanager.data.current_property

import com.emplk.realestatemanager.domain.current_property.CurrentPropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class CurrentPropertyRepositoryImpl @Inject constructor() : CurrentPropertyRepository {

    private var currentPropertyIdMutableSharedFlow = MutableSharedFlow<Long?>(replay = 1)

    override fun getCurrentPropertyIdAsFlow(): Flow<Long> = currentPropertyIdMutableSharedFlow.filterNotNull()

    override fun setCurrentPropertyId(id: Long) {
        currentPropertyIdMutableSharedFlow.tryEmit(id)
    }
}