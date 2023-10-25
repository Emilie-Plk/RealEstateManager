package com.emplk.realestatemanager.domain.property_draft

import kotlinx.coroutines.flow.Flow

interface PropertyFormParamsRepository {
    fun setFormTitle(title: String?)
    fun getFormTitle(): Flow<String>
    fun resetFormTitle()
}
