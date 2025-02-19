package com.k.deeplinkingtesting.appopen

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AppOpenAdManager(
    private val myApplication: Application,
    private var adUnitId: String,
    private var listener: OnAdsCallbackListener
) : Application.ActivityLifecycleCallbacks {

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    private var loadTime: Long = 0
    private var currentActivity: Activity? = null

    init {
        myApplication.registerActivityLifecycleCallbacks(this)
        loadAppOpenAd()  // Start loading the ad when the app starts
    }

    // Method to load App Open Ad
    fun loadAppOpenAd() {
        if (isAdAvailable()) {
            return
        }

        isLoadingAd = true
        val adRequest = AdManagerAdRequest.Builder().build()

        AppOpenAd.load(
            myApplication,
            adUnitId, // Replace with your actual ad unit ID
            adRequest,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    loadTime = System.currentTimeMillis()
                    isLoadingAd = false
                    Log.d("AppOpenManager", "App Open Ad Loaded.")
                    listener.onComplete()
                    // Immediately show the ad once loaded
                 // Wait for 2 seconds
                    showAdIfAvailable()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e("AppOpenManager", "App Open Ad Failed to Load: ${loadAdError.message}")
                    isLoadingAd = false
                    listener.onError(-1, loadAdError.message)
                }
            }
        )
    }

    // Check if ad is available
    private fun isAdAvailable(): Boolean {
        return appOpenAd != null && (System.currentTimeMillis() - loadTime) < 4 * 60 * 60 * 1000 // 4 hours validity
    }

    // Show App Open Ad if available
    fun showAdIfAvailable() {
        if (isAdAvailable()) {
            appOpenAd?.fullScreenContentCallback = object :FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Reload ad after it is dismissed
                    appOpenAd = null
                    loadAppOpenAd()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    Log.e("AppOpenManager", "Ad failed to show.")
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("AppOpenManager", "App Open Ad showed successfully.")
                }
                override fun onAdImpression() {
                    listener.onAdImpression()
                    Log.d("AppOpenManager", "Ad impression recorded.")
                }

                override fun onAdClicked() {
                    Log.d("AppOpenManager", "Ad clicked.")
                }

            }

            currentActivity?.let {
                appOpenAd?.show(it)
            }

        } else {
            Log.d("AppOpenManager", "Ad is not available yet.")
            loadAppOpenAd() // Try loading again if not available
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
        // Only try showing the ad once the activity is resumed
      //   showAdIfAvailable()
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
        currentActivity = null
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}
