package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.address.ResetSelectedAddressStateUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.PicturePreviewIdRepository
import javax.inject.Inject

class ClearPropertyFormUseCase @Inject constructor(
    private val deleteTemporaryPropertyFormUseCase: DeleteTemporaryPropertyFormUseCase,
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
    private val resetSelectedAddressStateUseCase: ResetSelectedAddressStateUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) {
    suspend fun invoke(id: Long?) {
        id?.let { deleteTemporaryPropertyFormUseCase.invoke(it) }
        resetSelectedAddressStateUseCase.invoke()
        picturePreviewIdRepository.deleteAll()
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }
}