package com.emplk.realestatemanager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.emplk.realestatemanager.domain.screen_width.SetScreenWidthTypeUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), Application.ActivityLifecycleCallbacks {


    @Inject
    lateinit var setScreenWidthTypeFlowUseCase: SetScreenWidthTypeUseCase

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        setScreenWidthTypeFlowUseCase.invoke(resources.getBoolean(R.bool.isTablet))
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }


    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}