package com.k.deeplinkingtesting

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.izooto.NotificationHelperListener
import com.izooto.Payload
import com.izooto.iZooto
import com.outbrain.OBSDK.Outbrain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AppController : Application(), LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private var loadTime: Long = 0
    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        try {
            val backgroundScope = CoroutineScope(Dispatchers.IO)
            registerActivityLifecycleCallbacks(this)
            backgroundScope.launch {
                MobileAds.initialize(this@AppController) {}
            }
            appOpenAdManager = AppOpenAdManager()
            appOpenAdManager.loadAd(this)

        } catch (ex: Exception) {
            Log.e("AppController", "Ads execution failure " + ex.message)
        }


        Outbrain.register(this, "DATAB2HQ71I65P5JML02NJDEE");
        Outbrain.setTestMode(true); // Skipping all billing, statistics, information gathering, and all other action mechanisms.
        Outbrain.testLocation("en");







        iZooto.initialize(this)
            .setTokenReceivedListener { token: String? -> Log.e("Token", token!!) }
            .setLandingURLListener { landingUrl: String? ->
                Log.e("landing URL", landingUrl!!)
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .setNotificationReceiveListener(object : NotificationHelperListener {
                override fun onNotificationReceived(payload: Payload) {
                    Log.e("Received payload", payload.message)

                }
                override fun onNotificationOpened(data: String) {
                    Log.e("Open Notification", data)
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            })
            .build()

        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_default)


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        currentActivity?.let {
            appOpenAdManager.showAdIfAvailable(it)
        }
    }

    inner class AppOpenAdManager {
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        /** Request an ad. */
        fun loadAd(context: Context) {
            // Do not load ad if there is an unused ad or one is already loading.
            try {
                if (isLoadingAd || isAdAvailable()) {
                    return
                }

                isLoadingAd = true
                val request = AdRequest.Builder().build()
                AppOpenAd.load(
                    context, "ca-app-pub-3940256099942544/9257395921", request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    object : AppOpenAd.AppOpenAdLoadCallback() {
                        override fun onAdLoaded(ad: AppOpenAd) {
                            Log.d("ABC", "Ad was loaded.")
                            appOpenAd = ad
                            isLoadingAd = false
//                        loadTime = Date().time
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            // Called when an app open ad has failed to load.
                            Log.d("ABC", loadAdError.message)
                            isLoadingAd = false;
                        }
                    })
            } catch (ex: Exception) {
                Log.e("AppController", "Ads execution failure " + ex.message)
            }
        }

        fun showAdIfAvailable(activity: Activity) {
            showAdIfAvailable(
                activity,
                object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {
//                        Log.d("ABC", "Ad show completed.")
                        // Empty because the user will go back to the activity that shows the ad.
                    }
                })
        }

        private fun showAdIfAvailable(
            activity: Activity,
            onShowAdCompleteListener: OnShowAdCompleteListener
        ) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d("ABC", "The app open ad is already showing.")
                return
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d("ABC", "The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity)
                return
            }

            appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Called when full screen content is dismissed.
                    // Set the reference to null so isAdAvailable() returns false.
                    Log.d("ABC", "Ad dismissed fullscreen content.")
                    appOpenAd = null
                    isShowingAd = false

                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when fullscreen content failed to show.
                    // Set the reference to null so isAdAvailable() returns false.
                    Log.d("ABC", adError.message)
                    appOpenAd = null
                    isShowingAd = false

                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    Log.d("ABC", "Ad showed fullscreen content.")
                }
            }
            isShowingAd = true
            appOpenAd?.show(activity)
        }


        /** Check if ad exists and can be shown. */
        private fun isAdAvailable(): Boolean {
            return appOpenAd != null
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.e("Open Notification", "Storing data")
    }


    override fun onActivityStarted(activity: Activity) {
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        if (currentActivity == activity) {
            currentActivity = null
        }
    }

    override fun onActivityStopped(activity: Activity) {
        if (currentActivity == activity) {
            currentActivity = null
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.e("Open Notification", "Storing data1")

    }


    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity == activity) {
            currentActivity = null
        }
    }
}





