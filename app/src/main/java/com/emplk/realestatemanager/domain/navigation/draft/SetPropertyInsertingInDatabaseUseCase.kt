package com.emplk.realestatemanager.domain.navigation.draft

import javax.inject.Inject

class SetPropertyInsertingInDatabaseUseCase @Inject constructor(private val formRepository: FormRepository) {
    fun invoke(isPropertyAddedInDB: Boolean) {
        formRepository.setPropertyAddedInDatabase(isPropertyAddedInDB)
    }
}