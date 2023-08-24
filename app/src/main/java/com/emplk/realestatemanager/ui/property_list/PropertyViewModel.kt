package com.emplk.realestatemanager.ui.property_list

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.currency.CurrencyType
import com.emplk.realestatemanager.domain.currency.GetCurrencyTypeFormattingUseCase
import com.emplk.realestatemanager.domain.get_properties.GetPropertiesAsFlowUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(
    private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase,
    private val getCurrencyTypeFormattingUseCase: GetCurrencyTypeFormattingUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val resources: Resources,
) : ViewModel() {

    val viewState: LiveData<List<PropertyViewState>> = liveData(Dispatchers.IO) {
        combine(
            getPropertiesAsFlowUseCase.invoke(),
            getCurrencyTypeFormattingUseCase.invoke()
        ) { properties, currencyType ->
            properties.map {
                PropertyViewState.Property(
                    id = it.id,
                    typeOfProperty = it.type,
                    featuredPicture = it.photos.find { photo -> photo.isThumbnail }?.uri ?: "",
                    address = it.location.address,
                    price = when (currencyType) {
                        CurrencyType.DOLLAR -> resources.getString(R.string.price_in_dollar, it.price)
                        CurrencyType.EURO -> resources.getString(R.string.price_in_euro, it.price)
                    },
                    isSold = it.isSold,
                    onClickEvent = EquatableCallback {
                        PropertyViewEvent.NavigateToDetailActivity(it.id)
                    }
                )
            }
        }.collect { emit(it) }
    }
}
