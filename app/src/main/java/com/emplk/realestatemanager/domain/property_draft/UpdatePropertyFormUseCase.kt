package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToUsdDependingOnLocaleUseCase
import kotlinx.coroutines.coroutineScope
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime
import javax.inject.Inject

class UpdatePropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val convertSurfaceToSquareFeetDependingOnLocaleUseCase: ConvertSurfaceToSquareFeetDependingOnLocaleUseCase,
    private val convertToUsdDependingOnLocaleUseCase: ConvertToUsdDependingOnLocaleUseCase,
    private val clock: Clock,
) {
    suspend fun invoke(form: FormDraftParams) {
        coroutineScope {
            formDraftRepository.update(
                FormDraftEntity(
                    id = form.id,
                    type = form.propertyType,
                    title = form.draftTitle,
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
                    lastEditionDate = LocalDateTime.now(clock),
                )
            )
        }
    }
}