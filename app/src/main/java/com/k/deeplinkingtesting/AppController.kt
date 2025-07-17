package com.k.deeplinkingtesting

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.adsbynimbus.Nimbus
import com.adsbynimbus.request.FANDemandProvider
import com.adsbynimbus.request.UnityDemandProvider
import com.adsbynimbus.request.VungleDemandProvider
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds

import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.izooto.NotificationHelperListener
import com.izooto.Payload
import com.izooto.TokenReceivedListener

import com.izooto.iZooto


class AppController :  Application(), Application.ActivityLifecycleCallbacks, TokenReceivedListener,
    NotificationHelperListener {
    lateinit var gameId: String

    private var currentActivity: Activity? = null
    private var appOpenAd: AppOpenAd? = null
    private var hasAppOpenAdShownOnce = false
    private var isAdLoading = false

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this, OnInitializationCompleteListener {
            Log.d("ABC", "init")
        })
        iZooto.initialize(this)
            .setTokenReceivedListener(this)
            .setNotificationReceiveListener(this)
            .build()
        Nimbus.initialize(this,  "dev-publisher","DEV-af79-4612-87a9-aa70c17e8dc6")
        Nimbus.testMode = true
        FANDemandProvider.initialize(applicationContext, "3905158796364465")
        FANDemandProvider.forceTestAd = true
        //

        // liftoff monetise
        VungleDemandProvider.initialize(
            appId ="67a314485c0575584f324bf8",
        )

        //Unity
        UnityDemandProvider.initialize(applicationContext, "500240238")
// molaco

//        Moloco.initialize(
//            MolocoInitParams(
//                appContext = applicationContext,
//                appKey = "",
//                mediationInfo = MediationInfo("none")
//            )
//        )








        /* The Timber.DebugTree proxies all calls to Timber.log to the logcat console */
       // Timber.plant(Timber.DebugTree())

        /* Attaches a logger for SDK events that are sent to Timber */
       // Nimbus.addLogger { level, message -> Timber.log(level, message) }

       // registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity

        if (!hasAppOpenAdShownOnce && !isAdLoading) {
           // loadAndShowAppOpenAd()
        }
    }

    private fun loadAndShowAppOpenAd() {
        isAdLoading = true

        AppOpenAd.load(
            this,
            "",
            AdRequest.Builder().build(),
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isAdLoading = false
                    hasAppOpenAdShownOnce = true
                    //  setAdCallbacks(ad)

                    ad.setFullScreenContentCallback(object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            appOpenAd = null
                        }

                        override fun onAdFailedToShowFullScreenContent(error: AdError) {
                            appOpenAd = null
                        }
                        override fun onAdShowedFullScreenContent() {
                            Log.d("AppOpen", "Ad showed fullscreen.")
                        }

                        override fun onAdImpression() {
                            Log.d("AppOpen", "Ad impression.")
                        }

                        override fun onAdClicked() {
                            Log.d("AppOpen", "Ad clicked.")
                        }

                    })

                    currentActivity?.let {
                        ad.show(it)
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AppOpen", "Failed to load: ${error.message}")
                    isAdLoading = false
                }
            }
        )
    }


    // Other lifecycle methods (required for interface)
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
    override fun onTokenReceived(token: String?) {
        Log.e("Device Token", token.toString())
    }

    override fun onNotificationReceived(payload: Payload?) {
        Log.e("Payload Received",payload.toString())
    }

    override fun onNotificationOpened(deeplinkData: String?) {
        Log.e("DeepLink Data Received",deeplinkData.toString())

        val intent = Intent(this, GamAdActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}














