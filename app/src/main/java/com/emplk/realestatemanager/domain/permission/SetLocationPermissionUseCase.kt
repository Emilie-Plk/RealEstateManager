package com.emplk.realestatemanager.domain.permission

import javax.inject.Inject

class SetLocationPermissionUseCase @Inject constructor(
    private val permissionRepository: PermissionRepository
) {
    fun invoke(permission: Boolean) = permissionRepository.setLocationPermission(permission)
}