package com.emplk.realestatemanager.domain.agent

import javax.inject.Inject

class GetAgentsMapUseCase @Inject constructor(
    private val realEstateAgentRepository: RealEstateAgentRepository
) {
    fun invoke(): Map<Long, String> = realEstateAgentRepository.getAgentNameMap()
}