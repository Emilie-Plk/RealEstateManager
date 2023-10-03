package com.emplk.realestatemanager.data.current_property

import com.emplk.realestatemanager.domain.current_property.CurrentPropertyRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CurrentPropertyRepositoryImpl @Inject constructor() : CurrentPropertyRepository {

    private var currentPropertyIdMutableStateFlow = MutableStateFlow(-1L)

    override fun getCurrentPropertyIdAsFlow(): Flow<Long> = currentPropertyIdMutableStateFlow

    override fun setCurrentPropertyId(id: Long) {
        currentPropertyIdMutableStateFlow.tryEmit(id)
    }
}