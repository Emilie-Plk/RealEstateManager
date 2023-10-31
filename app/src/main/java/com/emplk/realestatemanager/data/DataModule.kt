package com.emplk.realestatemanager.data

import android.app.Application
import android.content.ContentResolver
import android.content.res.Resources
import android.os.Build
import android.util.LruCache
import androidx.work.WorkManager
import com.emplk.realestatemanager.BuildConfig
import com.emplk.realestatemanager.data.api.FixerApi
import com.emplk.realestatemanager.data.api.GoogleApi
import com.emplk.realestatemanager.data.property.PropertyDao
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.picture.PictureDao
import com.emplk.realestatemanager.data.property_draft.FormDraftDao
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewDao
import com.emplk.realestatemanager.domain.autocomplete.PredictionWrapper
import com.emplk.realestatemanager.domain.geocoding.GeocodingWrapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Clock
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideWorkManager(application: Application): WorkManager =
        WorkManager.getInstance(application)

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideAppDatabase(
        application: Application,
    ): AppDatabase = AppDatabase.create(application)

    @Singleton
    @Provides
    fun provideResources(application: Application): Resources = application.resources

    @Suppress("DEPRECATION")
    @Singleton
    @Provides
    fun provideLocale(application: Application): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            application.resources.configuration.locales[0]
        } else {
            application.resources.configuration.locale
        }
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    @Singleton
    @Provides
    fun provideGoogleApiService(@GoogleApiRetrofit retrofit: Retrofit): GoogleApi =
        retrofit.create(GoogleApi::class.java)

    @Singleton
    @Provides
    @GoogleApiRetrofit
    fun provideGoogleApiRetrofit(@GoogleApiOkHttpClient okHttpClient: okhttp3.OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Singleton
    @Provides
    @GoogleApiOkHttpClient
    fun provideGoogleOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): okhttp3.OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .addInterceptor(
                Interceptor { chain: Interceptor.Chain ->
                    chain.proceed(
                        chain.request().let { request ->
                            request
                                .newBuilder()
                                .url(
                                    request.url.newBuilder()
                                        .addQueryParameter("key", BuildConfig.GOOGLE_API_KEY)
                                        .build()
                                )
                                .build()
                        }
                    )
                }
            )
            .build()

    @Singleton
    @Provides
    @FixerApiRetrofit
    fun provideFixerCurrencyApiRetrofit(
        @FixerApiOkHttpClient okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://data.fixer.io/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Singleton
    @Provides
    @FixerApiOkHttpClient
    fun provideFixerOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .addInterceptor(
                Interceptor { chain: Interceptor.Chain ->
                    chain.proceed(
                        chain.request().let { request ->
                            request
                                .newBuilder()
                                .url(
                                    request.url.newBuilder()
                                        .addQueryParameter("access_key", BuildConfig.FIXER_API_KEY)
                                        .build()
                                )
                                .build()
                        }
                    )
                }
            )
            .build()

    @Singleton
    @Provides
    fun provideCurrencyRateApi(@FixerApiRetrofit retrofit: Retrofit): FixerApi =
        retrofit.create(FixerApi::class.java)

    @Singleton
    @Provides
    fun provideContentResolver(application: Application): ContentResolver =
        application.contentResolver

    // region DAOs

    @Singleton
    @Provides
    fun providePropertyDao(appDatabase: AppDatabase): PropertyDao = appDatabase.getPropertyDao()

    @Singleton
    @Provides
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao = appDatabase.getLocationDao()

    @Singleton
    @Provides
    fun providePictureDao(appDatabase: AppDatabase): PictureDao = appDatabase.getPictureDao()


    @Singleton
    @Provides
    fun providePropertyFormDao(appDatabase: AppDatabase): FormDraftDao = appDatabase.getPropertyFormDao()

    @Singleton
    @Provides
    fun providePicturePreviewDao(appDatabase: AppDatabase): PicturePreviewDao = appDatabase.getPicturePreviewDao()

    // endregion DAOs

    @Singleton
    @Provides
    @LruCachePredictions
    fun providePredictionsLruCache(): LruCache<String, PredictionWrapper> = LruCache(200)

    @Singleton
    @Provides
    @LruCacheGeocode
    fun provideGeocodeLruCache(): LruCache<String, GeocodingWrapper> = LruCache(200)

    @Singleton
    @Provides
    fun provideClock(): Clock = Clock.systemDefaultZone()

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LruCachePredictions

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LruCacheGeocode

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GoogleApiRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GoogleApiOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FixerApiRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FixerApiOkHttpClient
}