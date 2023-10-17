package com.emplk.realestatemanager.domain.property_draft

import java.math.BigDecimal
import javax.inject.Inject

class AddTemporaryPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val mapPropertyToDraftUseCase: MapPropertyToDraftUseCase,
) {
    suspend fun invoke(id: Long?): Long =
        if (id == null) {
            formDraftRepository.addPropertyFormWithDetails(
                FormDraftEntity(
                    id = 0L,
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
        } else {
            formDraftRepository.addPropertyFormWithDetails(mapPropertyToDraftUseCase.invoke(id))
        }
            ?: throw IllegalStateException("Form id is null")
}
