package com.emplk.realestatemanager.domain.locale_formatting.currency

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.emplk.realestatemanager.R

enum class CurrencyType(@DrawableRes val drawableRes: Int, @StringRes val stringRes: Int) {
    DOLLAR(R.drawable.baseline_dollar_24, R.string.price_in_dollar),
    EURO(R.drawable.baseline_euro_24, R.string.price_in_euro),
}
