package com.emplk.realestatemanager.domain.permission

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HasLocationPermissionFlowUseCase @Inject constructor(
    private val permissionRepository: PermissionRepository
) {
    fun invoke(): Flow<Boolean?> = permissionRepository.getLocationPermission()
}