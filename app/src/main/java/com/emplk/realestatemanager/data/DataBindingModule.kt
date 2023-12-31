package com.emplk.realestatemanager.data

import com.emplk.realestatemanager.data.agent.RealEstateAgentRepositoryImpl
import com.emplk.realestatemanager.data.autocomplete.PredictionRepositoryAutocomplete
import com.emplk.realestatemanager.data.connectivity.GpsConnectivityRepositoryBroadcastReceiver
import com.emplk.realestatemanager.data.connectivity.InternetConnectivityRepositoryBroadcastReceiver
import com.emplk.realestatemanager.data.content_resolver.PictureFileRepositoryContentResolver
import com.emplk.realestatemanager.data.currency_rate.CurrencyRateRepositoryFixer
import com.emplk.realestatemanager.data.current_property.CurrentPropertyRepositoryImpl
import com.emplk.realestatemanager.data.filter.PropertiesFilterRepositoryImpl
import com.emplk.realestatemanager.data.geocoding.GeocodingRepositoryGoogle
import com.emplk.realestatemanager.data.geolocation.GeolocationRepositoryFusedLocationProvider
import com.emplk.realestatemanager.data.loan_simulator.LoanSimulatorRepositoryImpl
import com.emplk.realestatemanager.data.locale_formatting.HumanReadableRepositoryImpl
import com.emplk.realestatemanager.data.navigation.FormRepositoryImpl
import com.emplk.realestatemanager.data.navigation.NavigationRepositoryImpl
import com.emplk.realestatemanager.data.permission.PermissionRepositoryImpl
import com.emplk.realestatemanager.data.property.PropertyRepositoryRoom
import com.emplk.realestatemanager.data.property.amenity.AmenityTypeRepositoryImpl
import com.emplk.realestatemanager.data.property.location.LocationRepositoryRoom
import com.emplk.realestatemanager.data.property.picture.PictureRepositoryRoom
import com.emplk.realestatemanager.data.property_draft.FormDraftRepositoryRoom
import com.emplk.realestatemanager.data.property_draft.address.PredictionAddressStateRepositoryImpl
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewRepositoryRoom
import com.emplk.realestatemanager.data.property_draft.picture_preview.id.PicturePreviewIdRepositoryImpl
import com.emplk.realestatemanager.data.property_type.PropertyTypeRepositoryImpl
import com.emplk.realestatemanager.data.screen_width.ScreenWidthTypeRepositoryImpl
import com.emplk.realestatemanager.domain.agent.RealEstateAgentRepository
import com.emplk.realestatemanager.domain.autocomplete.PredictionRepository
import com.emplk.realestatemanager.domain.connectivity.GpsConnectivityRepository
import com.emplk.realestatemanager.domain.connectivity.InternetConnectivityRepository
import com.emplk.realestatemanager.domain.content_resolver.PictureFileRepository
import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateRepository
import com.emplk.realestatemanager.domain.current_property.CurrentPropertyRepository
import com.emplk.realestatemanager.domain.filter.PropertiesFilterRepository
import com.emplk.realestatemanager.domain.geocoding.GeocodingRepository
import com.emplk.realestatemanager.domain.geolocation.GeolocationRepository
import com.emplk.realestatemanager.domain.loan_simulator.LoanSimulatorRepository
import com.emplk.realestatemanager.domain.locale_formatting.HumanReadableRepository
import com.emplk.realestatemanager.domain.navigation.NavigationRepository
import com.emplk.realestatemanager.domain.navigation.draft.FormRepository
import com.emplk.realestatemanager.domain.permission.PermissionRepository
import com.emplk.realestatemanager.domain.property.PropertyRepository
import com.emplk.realestatemanager.domain.property.amenity.type.AmenityTypeRepository
import com.emplk.realestatemanager.domain.property.location.LocationRepository
import com.emplk.realestatemanager.domain.property.pictures.PictureRepository
import com.emplk.realestatemanager.domain.property_draft.FormDraftRepository
import com.emplk.realestatemanager.domain.property_draft.address.PredictionAddressStateRepository
import com.emplk.realestatemanager.domain.property_draft.picture_preview.PicturePreviewRepository
import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.PicturePreviewIdRepository
import com.emplk.realestatemanager.domain.property_type.PropertyTypeRepository
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
    abstract fun bindScreenWidthRepository(implementation: ScreenWidthTypeRepositoryImpl): ScreenWidthTypeRepository

    @Singleton
    @Binds
    abstract fun bindNavigationRepository(implementation: NavigationRepositoryImpl): NavigationRepository

    @Singleton
    @Binds
    abstract fun bindNavigationDraftRepository(implementation: FormRepositoryImpl): FormRepository

    @Singleton
    @Binds
    abstract fun bindCurrentPropertyRepository(implementation: CurrentPropertyRepositoryImpl): CurrentPropertyRepository


    @Singleton
    @Binds
    abstract fun bindAgentRepository(implementation: RealEstateAgentRepositoryImpl): RealEstateAgentRepository

    @Singleton
    @Binds
    abstract fun bindPropertyTypeRepository(implementation: PropertyTypeRepositoryImpl): PropertyTypeRepository

    @Singleton
    @Binds
    abstract fun bindPropertyFormRepository(implementation: FormDraftRepositoryRoom): FormDraftRepository

    @Singleton
    @Binds
    abstract fun bindPicturePreviewRepository(implementation: PicturePreviewRepositoryRoom): PicturePreviewRepository

    @Singleton
    @Binds
    abstract fun bindAmenityTypeRepository(implementation: AmenityTypeRepositoryImpl): AmenityTypeRepository


    @Singleton
    @Binds
    abstract fun bindPredictionRepository(implementation: PredictionRepositoryAutocomplete): PredictionRepository

    @Singleton
    @Binds
    abstract fun bindGeoCodingRepository(implementation: GeocodingRepositoryGoogle): GeocodingRepository

    @Singleton
    @Binds
    abstract fun bindPicturePreviewIdRepository(implementation: PicturePreviewIdRepositoryImpl): PicturePreviewIdRepository

    @Singleton
    @Binds
    abstract fun bindInternetConnectivityRepository(implementation: InternetConnectivityRepositoryBroadcastReceiver): InternetConnectivityRepository

    @Singleton
    @Binds
    abstract fun bingGpsConnectivityRepository(implementation: GpsConnectivityRepositoryBroadcastReceiver): GpsConnectivityRepository

    @Singleton
    @Binds
    abstract fun bindPictureFileRepository(implementation: PictureFileRepositoryContentResolver): PictureFileRepository

    @Singleton
    @Binds
    abstract fun bindCurrencyRepository(implementation: HumanReadableRepositoryImpl): HumanReadableRepository

    @Singleton
    @Binds
    abstract fun bindCurrencyRateRepository(implementation: CurrencyRateRepositoryFixer): CurrencyRateRepository

    @Singleton
    @Binds
    abstract fun bindSelectedAddressStateRepository(implementation: PredictionAddressStateRepositoryImpl): PredictionAddressStateRepository

    @Singleton
    @Binds
    abstract fun bindLoanSimulatorRepository(implementation: LoanSimulatorRepositoryImpl): LoanSimulatorRepository

    @Singleton
    @Binds
    abstract fun bindPropertiesFilteredRepository(implementation: PropertiesFilterRepositoryImpl): PropertiesFilterRepository

    @Singleton
    @Binds
    abstract fun bindGeolocationRepository(implementation: GeolocationRepositoryFusedLocationProvider): GeolocationRepository

    @Singleton
    @Binds
    abstract fun bindPermissionRepository(implementation: PermissionRepositoryImpl): PermissionRepository
}