package com.emplk.realestatemanager.ui.property_list

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency.CurrencyType
import com.emplk.realestatemanager.domain.currency.GetCurrencyTypeFormattingUseCase
import com.emplk.realestatemanager.domain.get_properties.GetPropertiesAsFlowUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(
    private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase,
    private val getCurrencyTypeFormattingUseCase: GetCurrencyTypeFormattingUseCase,
    // TODO NativeText instead !
    private val resources: Resources,
) : ViewModel() {

    val viewState: LiveData<List<PropertyViewState>> = liveData(Dispatchers.IO) {
        val currencyType = getCurrencyTypeFormattingUseCase.invoke()

        getPropertiesAsFlowUseCase.invoke().collect { properties ->
            emit(
                properties.map { propertiesWithPicturesAndLocation ->
                    val photoUrl =
                        propertiesWithPicturesAndLocation.pictures.find { picture -> picture.isThumbnail }?.uri

                    PropertyViewState.Property(
                        id = propertiesWithPicturesAndLocation.property.id,
                        typeOfProperty = propertiesWithPicturesAndLocation.property.type,
                        featuredPicture = if (photoUrl != null) {
                            NativePhoto.Uri(photoUrl)
                        } else {
                            NativePhoto.Resource(R.drawable.baseline_villa_24)
                        },
                        address = propertiesWithPicturesAndLocation.location.address,
                        price = when (currencyType) {
                            CurrencyType.DOLLAR -> resources.getString(
                                R.string.price_in_dollar,
                                propertiesWithPicturesAndLocation.property.price
                            )

                            CurrencyType.EURO -> resources.getString(
                                R.string.price_in_euro,
                                propertiesWithPicturesAndLocation.property.price
                            )
                        },
                        isSold = propertiesWithPicturesAndLocation.property.isSold,
                        onClickEvent = EquatableCallback {
                            PropertyViewEvent.NavigateToDetailActivity(propertiesWithPicturesAndLocation.property.id)
                        }
                    )
                }
            )
        }
    }
}
