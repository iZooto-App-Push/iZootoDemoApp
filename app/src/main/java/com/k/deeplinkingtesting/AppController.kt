package com.k.deeplinkingtesting

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
//import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.indixital.DeepLinkCallback
import com.indixital.Indixital
import com.izooto.NotificationHelperListener
import com.izooto.Payload
import com.izooto.iZooto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.yandex.mobile.ads.common.MobileAds
import org.json.JSONObject


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
            MobileAds.enableLogging(true)


            FirebaseApp.initializeApp(this);
            indixitalSDK()

            appOpenAdManager = AppOpenAdManager()
            appOpenAdManager.loadAd(this)


        } catch (ex: Exception) {
            Log.e("AppController", "Ads execution failure " + ex.message)
        }

        val TR_SDK_KEY: String = "24da4b0a-80af-4043-bfaf-24cbf277e642"
              // Please pass your SDK key here.



        iZooto.initialize(this)
            .setTokenReceivedListener { token: String? -> Log.e("Token", token!!) }
            .setNotificationReceiveListener(object : NotificationHelperListener {
                override fun onNotificationReceived(payload: Payload) {

                    Log.e("Received payload", ""+payload.defaultNotificationPreview)

                }
                override fun onNotificationOpened(data: String) {
                    Log.e("Open Notification", data)
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            })
            .build()

//        val remoteConfig = Firebase.remoteConfig
//        val configSettings = remoteConfigSettings {
//            minimumFetchIntervalInSeconds = 0
//        }
//        remoteConfig.setConfigSettingsAsync(configSettings)
//        remoteConfig.setDefaultsAsync(R.xml.remote_config_default)

//        AppLovinSdk.getInstance(this).mediationProvider = "max"
//        AppLovinSdk.initializeSdk(this,{ configuration: AppLovinSdkConfiguration ->
//            appOpenManager = ExampleAppOpenManager(applicationContext)
//            Log.d("AppLovin", "SDK Initialized")
//        })

    }

    private fun indixitalSDK() {
        Indixital.Builder(applicationContext)
            .setDevKey("your_dev_key_here")
            .setDeepLinkCallback(object : DeepLinkCallback {
                override fun onDeepLinkCaptured(deepLinkData: JSONObject) {
                    Log.d("Indixtial", "Received deep link data: $deepLinkData")
                    passDeepLinkDataToActivity(deepLinkData)


                }

                override fun onConsumerDeepLinkCaptured(deepLinkData: JSONObject) {
                    Log.d("Indixtial", "Received deep link data: $deepLinkData")
                }
            })

            .build()
    }
    private fun passDeepLinkDataToActivity(deepLinkData: JSONObject) {
        // Create an Intent to launch the new activity
        val intent = Intent(this, MainActivity::class.java)

        // Convert the deepLinkData to a string and pass it as an extra
        intent.putExtra("DL_ATTRS", deepLinkData.toString())

        // Add flags if necessary (optional)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        // Start the activity
        startActivity(intent)
    }




    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {

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





