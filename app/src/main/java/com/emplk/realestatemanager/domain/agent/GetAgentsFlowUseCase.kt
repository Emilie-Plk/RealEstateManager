package com.emplk.realestatemanager.domain.agent

import javax.inject.Inject

class GetAgentsFlowUseCase @Inject constructor(
    private val realEstateAgentRepository: RealEstateAgentRepository
) {
    fun invoke() = realEstateAgentRepository.getAgentNameMapFlow()
}