package com.emplk.realestatemanager.domain.property_draft

import java.math.BigDecimal
import javax.inject.Inject

class AddPropertyFormWithDetailsUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val mapPropertyToDraftUseCase: MapPropertyToDraftUseCase,
) {
    suspend fun invoke(id: Long?): Long =
        if (id == null || id == 0L) {
            // case insert new draft in db (ADD)
            formDraftRepository.addFormWithDetails(
                FormDraftEntity(
                    id = 0L,
                    type = null,
                    title = null,
                    price = BigDecimal.ZERO,
                    surface = BigDecimal.ZERO,
                    rooms = 0,
                    bedrooms = 0,
                    bathrooms = 0,
                    description = null,
                    amenities = emptyList(),
                    address = null,
                    isAddressValid = false,
                    agentName = null,
                    entryDate = null,
                    saleDate = null,
                    lastEditionDate = null,
                )
            )
        } else {
            // case insert new draft in db (EDIT)
            formDraftRepository.addFormWithDetails(mapPropertyToDraftUseCase.invoke(id))
        }
}
