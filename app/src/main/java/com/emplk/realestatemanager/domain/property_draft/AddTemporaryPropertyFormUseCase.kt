package com.emplk.realestatemanager.domain.property_draft

import java.math.BigDecimal
import javax.inject.Inject

class AddTemporaryPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository
) {
    suspend fun invoke(id: Long?): Long = formDraftRepository.addPropertyFormWithDetails(
        FormDraftEntity(
            id = id,
            type = "",
            price = BigDecimal.ZERO,
            surface = "",
            rooms = 0,
            bedrooms = 0,
            bathrooms = 0,
            description = "",
            address = "",
            isAddressValid = false,
            agentName = "",
        )
    )
}
