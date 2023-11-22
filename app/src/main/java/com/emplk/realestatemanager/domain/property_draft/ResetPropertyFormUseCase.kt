package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.address.ResetSelectedAddressStateUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.PicturePreviewIdRepository
import javax.inject.Inject

class ResetPropertyFormUseCase @Inject constructor(
    private val deletePropertyFormUseCase: DeletePropertyFormUseCase,
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
    private val resetSelectedAddressStateUseCase: ResetSelectedAddressStateUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val resetFormParamsUseCase: ResetFormParamsUseCase,
) {
    suspend fun invoke(id: Long?) {
        id?.let { deletePropertyFormUseCase.invoke(it) }
        resetSelectedAddressStateUseCase.invoke()
        picturePreviewIdRepository.deleteAll()
        resetFormParamsUseCase.invoke()
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }
}