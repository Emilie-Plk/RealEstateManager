package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.data.property.PropertyDtoEntity
import com.emplk.realestatemanager.domain.location.AddLocationUseCase
import com.emplk.realestatemanager.data.location.LocationDtoEntity
import com.emplk.realestatemanager.domain.pictures.AddPictureUseCase
import com.emplk.realestatemanager.data.picture.PictureDtoEntity
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
        property: PropertyDtoEntity,
        pictures: List<PictureDtoEntity>,
        location: LocationDtoEntity
    ) {
        coroutineScope {
            val deferredPropertyId = async {
                addPropertyUseCase.invoke(property)
            }

            val generatedPropertyId = deferredPropertyId.await()

            val childrenJobs = pictures.map { picture ->
                launch {
                    addPictureUseCase.invoke(picture.copy(propertyId = generatedPropertyId))
                }
            } + launch {
                addLocationUseCase.invoke(location.copy(propertyId = generatedPropertyId))
            }
            childrenJobs.joinAll()
        }
    }
}