package com.emplk.realestatemanager.data

import android.app.Application
import android.content.res.Resources
import androidx.room.PrimaryKey
import androidx.work.WorkManager
import com.emplk.realestatemanager.data.amenity.AmenityDao
import com.emplk.realestatemanager.data.location.LocationDao
import com.emplk.realestatemanager.data.picture.PictureDao
import com.emplk.realestatemanager.data.property.PropertyDao
import com.emplk.realestatemanager.data.property_form.PropertyFormDao
import com.emplk.realestatemanager.data.property_form.amenity.AmenityFormDao
import com.emplk.realestatemanager.data.property_form.location.LocationFormDao
import com.emplk.realestatemanager.data.property_form.picture_preview.PicturePreviewDao
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
        workManager: WorkManager,
        gson: Gson,
    ): AppDatabase = AppDatabase.create(application, workManager, gson)

    @Singleton
    @Provides
    fun provideResources(application: Application): Resources = application.resources

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    @Singleton
    @Provides
    fun provideGoogleApiRetrofit(okHttpClient: okhttp3.OkHttpClient, gson: Gson): Retrofit =
        retrofit2.Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Singleton
    @Provides
    fun provideGoogleOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): okhttp3.OkHttpClient =
        okhttp3.OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()

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
    fun provideAmenityDao(appDatabase: AppDatabase): AmenityDao = appDatabase.getAmenityDao()


    @Singleton
    @Provides
    fun providePropertyFormDao(appDatabase: AppDatabase): PropertyFormDao = appDatabase.getPropertyFormDao()

    @Singleton
    @Provides
    fun providePicturePreviewDao(appDatabase: AppDatabase): PicturePreviewDao = appDatabase.getPicturePreviewDao()

    @Singleton
    @Provides
    fun provideLocationFormDao(appDatabase: AppDatabase): LocationFormDao = appDatabase.getLocationFormDao()

    @Singleton
    @Provides
    fun provideAmenityFormDao(appDatabase: AppDatabase): AmenityFormDao = appDatabase.getAmenityFormDao()

    // endregion DAOs
}