package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetCurrentPropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
) {
    fun invoke(): Flow<PropertyEntity> =
        getCurrentPropertyIdFlowUseCase.invoke().filterNotNull().flatMapLatest { id ->
            propertyRepository.getPropertyByIdAsFlow(id)
        }.filterNotNull()
}