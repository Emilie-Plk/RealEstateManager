package com.emplk.realestatemanager.domain.locale_formatting.currency

import androidx.annotation.DrawableRes
import com.emplk.realestatemanager.R

enum class CurrencyType(@DrawableRes val drawableRes: Int, val symbol: String) {
    DOLLAR(R.drawable.baseline_dollar_24, "$"),
    EURO(R.drawable.baseline_euro_24, "€"),
}
