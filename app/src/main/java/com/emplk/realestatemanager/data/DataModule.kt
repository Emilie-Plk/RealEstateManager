package com.emplk.realestatemanager.data

import android.app.Application
import com.emplk.realestatemanager.data.property.PropertyDao
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        application: Application,
        gson: Gson,
    ): AppDatabase = AppDatabase.create(application, gson)

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun providePropertyDao(appDatabase: AppDatabase): PropertyDao = appDatabase.getPropertyDao()
}