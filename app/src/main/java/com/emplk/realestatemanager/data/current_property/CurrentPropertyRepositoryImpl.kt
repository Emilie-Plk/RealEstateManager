package com.emplk.realestatemanager.data.current_property

import com.emplk.realestatemanager.domain.current_property.CurrentPropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class CurrentPropertyRepositoryImpl @Inject constructor() : CurrentPropertyRepository {

    private var currentPropertyIdMutableStateFlow = MutableStateFlow(-1L)

    override fun getCurrentPropertyIdAsFlow(): Flow<Long> = currentPropertyIdMutableStateFlow.asStateFlow()

    override fun setCurrentPropertyId(id: Long) {
        currentPropertyIdMutableStateFlow.value = id
    }
}