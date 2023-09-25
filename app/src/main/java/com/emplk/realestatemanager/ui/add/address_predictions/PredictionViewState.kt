package com.emplk.realestatemanager.ui.add.address_predictions

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam

sealed class PredictionViewState(val type: Type) {

    enum class Type {
        PREDICTION,
        EMPTY_STATE,
    }

    data class Prediction(
        val address: String,
        val placeId: String,
        val onClickEvent: EquatableCallbackWithParam<String>,
    ) : PredictionViewState(Type.PREDICTION)

    object EmptyState : PredictionViewState(Type.EMPTY_STATE)
}

