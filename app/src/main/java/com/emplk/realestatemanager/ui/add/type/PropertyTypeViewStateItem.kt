package com.emplk.realestatemanager.ui.add.type

import com.emplk.realestatemanager.ui.utils.NativeText

data class PropertyTypeViewStateItem(
    val id: Long,
    val name: NativeText,
    val databaseName: String
)