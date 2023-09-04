package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.domain.location.AddLocationUseCase
import com.emplk.realestatemanager.domain.location.LocationEntity
import com.emplk.realestatemanager.domain.pictures.AddPictureUseCase
import com.emplk.realestatemanager.domain.pictures.PictureEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddPropertyWithDetailsUseCase @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase,
    private val addPictureUseCase: AddPictureUseCase,
    private val addLocationUseCase: AddLocationUseCase,
) {
    suspend fun invoke(
        propertyEntity: PropertyEntity,
        pictureEntities: List<PictureEntity>,
        locationEntity: LocationEntity
    ) {
        coroutineScope {
            val deferredPropertyId = async {
                addPropertyUseCase.invoke(propertyEntity)
            }

            val generatedPropertyId = deferredPropertyId.await()

            val childrenJobs = pictureEntities.map { picture ->
                launch {
                    addPictureUseCase.invoke(picture.copy(propertyId = generatedPropertyId))
                }
            } + launch {
                addLocationUseCase.invoke(locationEntity.copy(propertyId = generatedPropertyId))
            }
            childrenJobs.joinAll()
        }
    }
}