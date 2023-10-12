package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property_draft.address.ResetSelectedAddressStateUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.PicturePreviewIdRepository
import javax.inject.Inject

class ClearPropertyFormUseCase @Inject constructor(
    private val deleteTemporaryPropertyFormUseCase: DeleteTemporaryPropertyFormUseCase,
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
    private val resetSelectedAddressStateUseCase: ResetSelectedAddressStateUseCase,
) {
    suspend fun invoke() {
        deleteTemporaryPropertyFormUseCase.invoke()
        resetSelectedAddressStateUseCase
        picturePreviewIdRepository.deleteAll()
    }
}