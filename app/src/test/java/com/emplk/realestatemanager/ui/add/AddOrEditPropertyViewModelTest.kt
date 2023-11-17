package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.domain.agent.GetAgentsMapUseCase
import com.emplk.realestatemanager.domain.autocomplete.GetCurrentPredictionAddressesFlowWithDebounceUseCase
import com.emplk.realestatemanager.domain.connectivity.IsInternetEnabledFlowUseCase
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.locale_formatting.currency.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.draft.GetClearPropertyFormNavigationEventUseCase
import com.emplk.realestatemanager.domain.navigation.draft.GetDraftNavigationUseCase
import com.emplk.realestatemanager.domain.property.AddOrEditPropertyUseCase
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.ClearPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.GetFormTitleAsFlowUseCase
import com.emplk.realestatemanager.domain.property_draft.InitPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.SetFormTitleUseCase
import com.emplk.realestatemanager.domain.property_draft.SetPropertyFormProgressUseCase
import com.emplk.realestatemanager.domain.property_draft.UpdatePropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.address.SetHasAddressFocusUseCase
import com.emplk.realestatemanager.domain.property_draft.address.SetSelectedAddressStateUseCase
import com.emplk.realestatemanager.domain.property_draft.address.UpdateOnAddressClickedUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.DeletePicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.GetPicturePreviewsAsFlowUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.SavePictureToLocalAppFilesAndToLocalDatabaseUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.UpdatePicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.AddAllPicturePreviewsIdsUseCase
import com.emplk.realestatemanager.domain.property_type.GetPropertyTypeUseCase
import io.mockk.mockk

class AddOrEditPropertyViewModelTest {

    private val addOrEditPropertyUseCase: AddOrEditPropertyUseCase = mockk()
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase = mockk()
    private val clearPropertyFormUseCase: ClearPropertyFormUseCase = mockk()
    private val setPropertyFormProgressUseCase: SetPropertyFormProgressUseCase = mockk()
    private val updatePropertyFormUseCase: UpdatePropertyFormUseCase = mockk()
    private val updatePicturePreviewUseCase: UpdatePicturePreviewUseCase = mockk()
    private val addAllPicturePreviewsIdsUseCase: AddAllPicturePreviewsIdsUseCase = mockk()
    private val savePictureToLocalAppFilesAndToLocalDatabaseUseCase: SavePictureToLocalAppFilesAndToLocalDatabaseUseCase =
        mockk()
    private val initPropertyFormUseCase: InitPropertyFormUseCase = mockk()
    private val getPicturePreviewsAsFlowUseCase: GetPicturePreviewsAsFlowUseCase = mockk()
    private val deletePicturePreviewUseCase: DeletePicturePreviewUseCase = mockk()
    private val getAgentsMapUseCase: GetAgentsMapUseCase = mockk()
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase = mockk()
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase = mockk()
    private val convertToSquareFeetDependingOnLocaleUseCase: ConvertToSquareFeetDependingOnLocaleUseCase = mockk()
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase = mockk()
    private val getFormTitleUseCase: GetFormTitleAsFlowUseCase = mockk()
    private val setFormTitleUseCase: SetFormTitleUseCase = mockk()
    private val setSelectedAddressStateUseCase: SetSelectedAddressStateUseCase = mockk()
    private val updateOnAddressClickedUseCase: UpdateOnAddressClickedUseCase = mockk()
    private val setHasAddressFocusUseCase: SetHasAddressFocusUseCase = mockk()
    private val getPropertyTypeUseCase: GetPropertyTypeUseCase = mockk()
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase = mockk()
    private val getCurrentPredictionAddressesFlowWithDebounceUseCase: GetCurrentPredictionAddressesFlowWithDebounceUseCase =
        mockk()
    private val isInternetEnabledFlowUseCase: IsInternetEnabledFlowUseCase = mockk()
    private val getDraftNavigationUseCase: GetDraftNavigationUseCase = mockk()
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase = mockk()
    private val getClearPropertyFormNavigationEventUseCase: GetClearPropertyFormNavigationEventUseCase = mockk()
}