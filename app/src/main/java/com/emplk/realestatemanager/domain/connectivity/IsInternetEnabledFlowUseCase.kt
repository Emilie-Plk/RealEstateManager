package com.emplk.realestatemanager.domain.connectivity

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsInternetEnabledFlowUseCase @Inject constructor(
    private val internetConnectivityRepository: InternetConnectivityRepository,
) {
    fun invoke(): Flow<Boolean> = internetConnectivityRepository.isInternetEnabledAsFlow()
}