package com.emplk.realestatemanager.ui.add.address_predictions

import com.emplk.realestatemanager.ui.utils.EquatableCallback

sealed class PredictionViewState(val type: Type) {

    enum class Type {
        PREDICTION,
        EMPTY_STATE,
    }

    data class Prediction(
        val id: Long,
        val description: String,
        val placeId: String,
        val onClickEvent: EquatableCallback,
    ) : PredictionViewState(Type.PREDICTION)

    object EmptyState : PredictionViewState(Type.EMPTY_STATE)
}

