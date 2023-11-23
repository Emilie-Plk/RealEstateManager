package com.emplk.realestatemanager.domain.permission

import kotlinx.coroutines.flow.Flow

interface PermissionRepository {
fun getLocationPermission() : Flow<Boolean?>
fun setLocationPermission(permission: Boolean)
}
