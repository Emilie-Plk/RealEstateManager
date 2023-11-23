package com.emplk.realestatemanager.data.permission

import com.emplk.realestatemanager.domain.permission.PermissionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class PermissionRepositoryImpl @Inject constructor(
) : PermissionRepository {
    private val hasLocationPermissionMutableStateFlow: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)

    override fun getLocationPermission(): Flow<Boolean?> = hasLocationPermissionMutableStateFlow

    override fun setLocationPermission(permission: Boolean) {
        hasLocationPermissionMutableStateFlow.value = permission
    }
}