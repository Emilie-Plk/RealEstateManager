package com.emplk.realestatemanager

import android.app.Activity
import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.emplk.realestatemanager.data.connectivity.InternetConnectivityRepositoryBroadcastReceiver
import com.emplk.realestatemanager.domain.screen_width.SetScreenWidthTypeUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider, Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var setScreenWidthTypeFlowUseCase: SetScreenWidthTypeUseCase

    private var activityCount = 0

    private var isInternetConnectivityReceiverRegistered = false

    @Inject
    lateinit var internetConnectivityRepositoryBroadcastReceiver: InternetConnectivityRepositoryBroadcastReceiver

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()


    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        registerConnectivityBroadcastReceiver()
    }

    @Suppress("DEPRECATION")
    private fun registerConnectivityBroadcastReceiver() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(
            internetConnectivityRepositoryBroadcastReceiver,
            intentFilter
        )
        isInternetConnectivityReceiverRegistered = true
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        activityCount++
    }

    override fun onActivityResumed(activity: Activity) {
        setScreenWidthTypeFlowUseCase.invoke(resources.getBoolean(R.bool.isTablet))
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        activityCount--
        if (activityCount == 0 && isInternetConnectivityReceiverRegistered) {
            unregisterReceiver(internetConnectivityRepositoryBroadcastReceiver)
            isInternetConnectivityReceiverRegistered = false
        }
    }


    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}