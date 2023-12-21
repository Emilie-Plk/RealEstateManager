package com.emplk.realestatemanager.domain.agent

import com.emplk.realestatemanager.data.agent.RealEstateAgent
import javax.inject.Inject

class GetRealEstateAgentsUseCase @Inject constructor(
    private val realEstateAgentRepository: RealEstateAgentRepository
) {
    fun invoke(): List<RealEstateAgent> = realEstateAgentRepository.getRealEstateAgents()
}