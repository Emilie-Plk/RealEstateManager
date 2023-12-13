package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.connectivity.IsInternetEnabledFlowUseCase
import com.emplk.realestatemanager.domain.current_property.ResetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.geocoding.GeocodingRepository
import com.emplk.realestatemanager.domain.geocoding.GeocodingWrapper
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToUsdDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.map_picture.GenerateMapBaseUrlWithParamsUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.draft.SetPropertyInsertingInDatabaseUseCase
import com.emplk.realestatemanager.domain.property.location.LocationEntity
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property.pictures.PictureRepository
import com.emplk.realestatemanager.domain.property_draft.FormDraftParams
import com.emplk.realestatemanager.domain.property_draft.FormDraftRepository
import com.emplk.realestatemanager.domain.property_draft.ResetPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.UpdatePropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.GetPicturePreviewsUseCase
import com.emplk.realestatemanager.ui.add.FormEvent
import com.emplk.realestatemanager.ui.utils.NativeText
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime
import javax.inject.Inject

class AddOrEditPropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val formDraftRepository: FormDraftRepository,
    private val geocodingRepository: GeocodingRepository,
    private val pictureRepository: PictureRepository,
    private val isInternetEnabledFlowUseCase: IsInternetEnabledFlowUseCase,
    private val generateMapBaseUrlWithParamsUseCase: GenerateMapBaseUrlWithParamsUseCase,
    private val convertToUsdDependingOnLocaleUseCase: ConvertToUsdDependingOnLocaleUseCase,
    private val getPicturePreviewsUseCase: GetPicturePreviewsUseCase,
    private val convertSurfaceToSquareFeetDependingOnLocaleUseCase: ConvertSurfaceToSquareFeetDependingOnLocaleUseCase,
    private val updatePropertyFormUseCase: UpdatePropertyFormUseCase,
    private val setPropertyInsertingInDatabaseUseCase: SetPropertyInsertingInDatabaseUseCase,
    private val resetPropertyFormUseCase: ResetPropertyFormUseCase,
    private val resetCurrentPropertyIdUseCase: ResetCurrentPropertyIdUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val clock: Clock,
) {
    suspend fun invoke(form: FormDraftParams): FormEvent {
        require(
            form.id > 0 &&
                    form.propertyType != null &&
                    form.address != null &&
                    form.price > BigDecimal.ZERO &&
                    form.surface > BigDecimal.ZERO &&
                    form.description != null &&
                    form.nbRooms > 0 &&
                    form.nbBathrooms > 0 &&
                    form.nbBedrooms > 0 &&
                    form.agent != null &&
                    form.selectedAmenities.isNotEmpty() &&
                    form.pictureIds.isNotEmpty()
        ) {
            "Impossible state: form is not valid => form : $form"
        }
        val addOrEditPropertyWrapper: AddOrEditPropertyWrapper = coroutineScope {
            isInternetEnabledFlowUseCase.invoke().firstOrNull()?.let { isInternetEnabled ->
                if (!isInternetEnabled) {
                    return@coroutineScope AddOrEditPropertyWrapper.NoInternet(NativeText.Resource(R.string.no_internet_connection_draft_saved))
                }
            }

            val geocodingWrapperDeferred = async {
                geocodingRepository.getLatLong(form.address)
            }
            val doesPropertyExist = formDraftRepository.doesPropertyExist(form.id)
            if (doesPropertyExist) {
                // region edit existing property
                require(
                    form.entryDate != null
                ) {
                    "Impossible case: entry date should not be null => form : $form"
                }
                setPropertyInsertingInDatabaseUseCase.invoke(true)
                val updateSuccess = propertyRepository.update(
                    PropertyEntity(
                        id = form.id,
                        type = form.propertyType,
                        price = convertToUsdDependingOnLocaleUseCase.invoke(form.price),
                        surface = convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(form.surface),
                        description = form.description,
                        rooms = form.nbRooms,
                        bathrooms = form.nbBathrooms,
                        location = getLocationEntity(form.address, geocodingWrapperDeferred.await())
                            ?: return@coroutineScope AddOrEditPropertyWrapper.NoLatLong(NativeText.Resource(R.string.form_generic_error_message)),
                        bedrooms = form.nbBedrooms,
                        agentName = form.agent,
                        amenities = form.selectedAmenities,
                        pictures = getPicturePreviewsUseCase.invoke(form.id).map {
                            PictureEntity(
                                id = it.id,
                                uri = it.uri,
                                description = it.description,
                                isFeatured = it.id == form.featuredPictureId,
                            )
                        }.also {
                            val pictureIdsToDelete = pictureRepository.getPicturesIds(form.id)
                                .filter { it !in form.pictureIds }

                            pictureIdsToDelete.forEach { pictureId ->
                                pictureRepository.delete(pictureId)
                            }
                        },
                        entryDate = form.entryDate,
                        lastEditionDate = LocalDateTime.now(clock),
                        saleDate = if (!form.isSold) null else form.soldDate ?: LocalDateTime.now(clock),
                    ),
                )
                resetCurrentPropertyIdUseCase.invoke()

                if (updateSuccess) {
                    setPropertyInsertingInDatabaseUseCase.invoke(false)
                    AddOrEditPropertyWrapper.Success(NativeText.Resource(R.string.form_successfully_updated_message))
                } else {
                    setPropertyInsertingInDatabaseUseCase.invoke(false)
                    AddOrEditPropertyWrapper.Error(NativeText.Resource(R.string.form_generic_error_message))
                }
                // endregion
            } else {
                // region add new property
                val now = LocalDateTime.now(clock)
                setPropertyInsertingInDatabaseUseCase.invoke(true)
                val addSuccess = propertyRepository.addPropertyWithDetails(
                    PropertyEntity(
                        id = form.id,
                        type = form.propertyType,
                        price = convertToUsdDependingOnLocaleUseCase.invoke(form.price),
                        surface = convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(form.surface),
                        description = form.description,
                        rooms = form.nbRooms,
                        bathrooms = form.nbBathrooms,
                        location = getLocationEntity(form.address, geocodingWrapperDeferred.await())
                            ?: return@coroutineScope AddOrEditPropertyWrapper.NoLatLong(NativeText.Resource(R.string.form_generic_error_message)),
                        bedrooms = form.nbBedrooms,
                        agentName = form.agent,
                        amenities = form.selectedAmenities,
                        pictures =
                        getPicturePreviewsUseCase.invoke(form.id).map {
                            PictureEntity(
                                id = it.id,
                                uri = it.uri,
                                description = it.description,
                                isFeatured = it.id == form.featuredPictureId,
                            )
                        },
                        entryDate = now,
                        lastEditionDate = null,
                        saleDate = null,
                    ),
                )
                resetCurrentPropertyIdUseCase.invoke()

                if (addSuccess) {
                    setPropertyInsertingInDatabaseUseCase.invoke(false)
                    AddOrEditPropertyWrapper.Success(NativeText.Resource(R.string.form_successfully_created_message))
                } else {
                    setPropertyInsertingInDatabaseUseCase.invoke(false)
                    AddOrEditPropertyWrapper.Error(NativeText.Resource(R.string.form_generic_error_message))
                }
            }
        }
        // endregion
        return when (addOrEditPropertyWrapper) {
            is AddOrEditPropertyWrapper.Success -> {
                resetPropertyFormUseCase.invoke(form.id)
                setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                FormEvent.Toast(addOrEditPropertyWrapper.text)
            }

            is AddOrEditPropertyWrapper.Error -> {
                updatePropertyFormUseCase.invoke(form)
                setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                FormEvent.Toast(addOrEditPropertyWrapper.error)
            }

            is AddOrEditPropertyWrapper.NoInternet -> {
                updatePropertyFormUseCase.invoke(form)
                setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                FormEvent.Toast(addOrEditPropertyWrapper.error)
            }

            is AddOrEditPropertyWrapper.NoLatLong -> {
                FormEvent.Toast(addOrEditPropertyWrapper.error)
            }

            is AddOrEditPropertyWrapper.LocaleError -> FormEvent.Toast(addOrEditPropertyWrapper.error)
        }
    }

    private fun getLocationEntity(
        formAddress: String,
        geocodingWrapper: GeocodingWrapper
    ): LocationEntity? {
        return LocationEntity(
            address = formAddress,
            latLng = when (geocodingWrapper) {
                is GeocodingWrapper.Success -> geocodingWrapper.latLng
                is GeocodingWrapper.Error,
                is GeocodingWrapper.NoResult -> null
            },
            miniatureMapUrl = when (geocodingWrapper) {
                is GeocodingWrapper.Success -> generateMapBaseUrlWithParamsUseCase.invoke(geocodingWrapper.latLng)
                is GeocodingWrapper.Error,
                is GeocodingWrapper.NoResult -> return null
            },
        )
    }
}
