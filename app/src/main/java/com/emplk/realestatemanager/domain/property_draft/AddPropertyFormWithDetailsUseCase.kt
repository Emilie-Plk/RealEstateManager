package com.emplk.realestatemanager.domain.property_draft

import java.math.BigDecimal
import javax.inject.Inject

class AddPropertyFormWithDetailsUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val mapPropertyToDraftUseCase: MapPropertyToDraftUseCase,
) {
    suspend fun invoke(id: Long?): Long =
        if (id == null || id == 0L) {
            formDraftRepository.addFormDraftWithDetails(
                FormDraftEntity(
                    id = 0L,
                    type = "",
                    title = null,
                    price = BigDecimal.ZERO,
                    surface = BigDecimal.ZERO,
                    rooms = 0,
                    bedrooms = 0,
                    bathrooms = 0,
                    description = "",
                    amenities = emptyList(),
                    address = "",
                    isAddressValid = false,
                    agentName = "",
                    entryDate = null,
                    saleDate = null,
                    lastEditionDate = null,
                )
            )
        } else {
            formDraftRepository.addFormDraftWithDetails(mapPropertyToDraftUseCase.invoke(id))
        }
}
