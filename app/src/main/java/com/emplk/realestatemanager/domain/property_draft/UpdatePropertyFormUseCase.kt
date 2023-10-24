package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertToUsdDependingOnLocaleUseCase
import kotlinx.coroutines.coroutineScope
import java.math.BigDecimal
import javax.inject.Inject

class UpdatePropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val convertSurfaceToSquareFeetDependingOnLocaleUseCase: ConvertSurfaceToSquareFeetDependingOnLocaleUseCase,
    private val convertToUsdDependingOnLocaleUseCase: ConvertToUsdDependingOnLocaleUseCase,
) {
    suspend fun invoke(form: FormDraftParams) {
        coroutineScope {
            formDraftRepository.update(
                FormDraftEntity(
                    id = form.id,
                    type = form.propertyType,
                    price = convertToUsdDependingOnLocaleUseCase.invoke(form.price),
                    surface = if (form.surface > BigDecimal.ZERO) {
                        convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(form.surface)
                    } else {
                        form.surface
                    },
                    description = form.description,
                    rooms = form.nbRooms,
                    bathrooms = form.nbBathrooms,
                    address = form.address,
                    isAddressValid = form.isAddressValid,
                    bedrooms = form.nbBedrooms,
                    agentName = form.agent,
                    amenities = form.selectedAmenities,
                    isSold = form.isSold,
                    entryDate = form.entryDate,
                    saleDate = form.soldDate,
                    lastEditionDate = form.lastEditionDate,
                )
            )
        }
    }
}