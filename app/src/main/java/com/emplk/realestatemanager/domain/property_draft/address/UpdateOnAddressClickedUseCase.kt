package com.emplk.realestatemanager.domain.property_draft.address

import com.emplk.realestatemanager.domain.property_draft.GetCurrentPropertyDraftIdUseCase
import com.emplk.realestatemanager.domain.property_draft.PropertyFormRepository
import javax.inject.Inject

class UpdateOnAddressClickedUseCase @Inject constructor(
    private val selectedAddressStateRepository: SelectedAddressStateRepository,
    private val propertyFormRepository: PropertyFormRepository,
    private val getCurrentPropertyDraftIdUseCase: GetCurrentPropertyDraftIdUseCase,
) {
    suspend fun invoke(isAddressSelected: Boolean) {
        selectedAddressStateRepository.setIsPredictionSelectedByUser(isAddressSelected)
        getCurrentPropertyDraftIdUseCase.invoke()?.let { propertyFormId ->
            propertyFormRepository.updateIsAddressValid(propertyFormId, isAddressSelected)
        }
    }
}
