package com.emplk.realestatemanager.domain.navigation

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class GetNavigationTypeUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository,
) {
    fun invoke(): Flow<NavigationFragmentType>  {
        Log.d("COUCOU NAV", "we enter the GetNavigationTypeUseCase.invoke()")
     return   navigationRepository.getNavigationFragmentType()
    }
}