package com.emplk.realestatemanager.domain.currency_rate

import com.emplk.realestatemanager.domain.locale_formatting.GetLocaleUseCase
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import javax.inject.Inject

class GetLastUpdatedCurrencyRateDateUseCase @Inject constructor(
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private val getLocaleUseCase: GetLocaleUseCase,
) {
    companion object {
        private val FRANCE = Locale.FRANCE
    }

    suspend fun invoke(): String? {
        if (getLocaleUseCase.invoke() == FRANCE) {
            return when (val currencyRateWrapper = getCurrencyRateUseCase.invoke()) {
                is CurrencyRateWrapper.Success -> {
                    currencyRateWrapper.currencyRateEntity.lastUpdatedDate.format(
                        DateTimeFormatter.ofLocalizedDateTime(
                            FormatStyle.SHORT
                        )
                    )
                }

                is CurrencyRateWrapper.Error -> {
                    null
                }
            }
        } else {
            return null
        }
    }

}
