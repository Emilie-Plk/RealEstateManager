package com.emplk.realestatemanager.data

import com.emplk.realestatemanager.data.currency.LocaleFormattingRepositoryImpl
import com.emplk.realestatemanager.data.location.LocationRepositoryRoom
import com.emplk.realestatemanager.data.picture.PictureRepositoryRoom
import com.emplk.realestatemanager.data.property.PropertyRepositoryRoom
import com.emplk.realestatemanager.data.screen_width.ScreenWidthTypeRepositoryImpl
import com.emplk.realestatemanager.domain.locale_formatting.LocaleFormattingRepository
import com.emplk.realestatemanager.domain.location.LocationRepository
import com.emplk.realestatemanager.domain.pictures.PictureRepository
import com.emplk.realestatemanager.domain.property.PropertyRepository
import com.emplk.realestatemanager.domain.screen_width.ScreenWidthTypeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindingModule {

    @Singleton
    @Binds
    abstract fun bindPropertyRepository(implementation: PropertyRepositoryRoom): PropertyRepository

    @Singleton
    @Binds
    abstract fun bindLocationRepository(implementation: LocationRepositoryRoom): LocationRepository

    @Singleton
    @Binds
    abstract fun bindPictureRepository(implementation: PictureRepositoryRoom): PictureRepository

    @Singleton
    @Binds
    abstract fun bindCurrencyRepository(implementation: LocaleFormattingRepositoryImpl): LocaleFormattingRepository

    @Singleton
    @Binds
    abstract fun bindScreenWidthRepository(implementation: ScreenWidthTypeRepositoryImpl): ScreenWidthTypeRepository
}