package com.emplk.realestatemanager.domain.property_draft.address

import com.emplk.realestatemanager.domain.property_draft.GetCurrentDraftIdUseCase
import com.emplk.realestatemanager.domain.property_draft.FormDraftRepository
import javax.inject.Inject

class UpdateOnAddressClickedUseCase @Inject constructor(
    private val selectedAddressStateRepository: SelectedAddressStateRepository,
    private val formDraftRepository: FormDraftRepository,
    private val getCurrentDraftIdUseCase: GetCurrentDraftIdUseCase,
) {
    suspend fun invoke(isAddressSelected: Boolean) {
        selectedAddressStateRepository.setIsPredictionSelectedByUser(isAddressSelected)
        getCurrentDraftIdUseCase.invoke()?.let { propertyFormId ->
            formDraftRepository.updateIsAddressValid(propertyFormId, isAddressSelected)
        }
    }
}
