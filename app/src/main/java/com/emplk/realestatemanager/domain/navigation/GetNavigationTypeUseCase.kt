package com.emplk.realestatemanager.domain.navigation

import android.util.Log
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNavigationTypeUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository,
) {
    fun invoke(): Flow<NavigationFragmentType>  {
        Log.d("COUCOU", "we enter the GetNavigationTypeUseCase.invoke()")
     return   navigationRepository.getNavigationFragmentType()
    }
}