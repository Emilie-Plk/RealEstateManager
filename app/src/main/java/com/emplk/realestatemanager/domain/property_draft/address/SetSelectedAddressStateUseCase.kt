package com.emplk.realestatemanager.domain.property_draft.address

import javax.inject.Inject

class SetSelectedAddressStateUseCase @Inject constructor(
    private val getIsPredictionSelectedByUserUseCase: GetIsPredictionSelectedByUserUseCase,
    private val setIsPredictionSelectedByUserUseCase: SetIsPredictionSelectedByUserUseCase,
    private val setCurrentAddressInputUseCase: SetCurrentAddressInputUseCase,

    ) {
    fun invoke(userInput: String?) {
        if (userInput.isNullOrBlank()) {
            setIsPredictionSelectedByUserUseCase.invoke(false)
            setCurrentAddressInputUseCase.invoke(null)
        } else if (getIsPredictionSelectedByUserUseCase.invoke().value == true) {
            setCurrentAddressInputUseCase.invoke(userInput)
        } else {
            setIsPredictionSelectedByUserUseCase.invoke(false)
            setCurrentAddressInputUseCase.invoke(userInput)
        }
    }
}