package com.k.deeplinkingtesting

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ActivityTracker : Application.ActivityLifecycleCallbacks {

    private var currentActivity: Activity? = null

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        if (currentActivity == activity) currentActivity = null
    }

    override fun onActivityStopped(activity: Activity) {
        if (currentActivity == activity) currentActivity = null
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}