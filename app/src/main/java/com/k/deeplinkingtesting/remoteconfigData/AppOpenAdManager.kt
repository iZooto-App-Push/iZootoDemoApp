package com.k.deeplinkingtesting.remoteconfigData

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class AppOpenAdManager(private val activity: Activity) {

    private val admobAdUnitId = "ca-app-pub-3940256099942544/3419835294" // ✅ Default AdMob test ID

    fun loadAndShowAppOpenAd() {
        val adRequest = AdRequest.Builder().build()

        // Load AdMob App Open Ad
        AppOpenAd.load(
            activity, admobAdUnitId, adRequest,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    ad.show(activity)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    // Fallback to Ad Manager App Open Ad
                    loadAdManagerAppOpenAd()
                }
            }
        )
    }

    private fun loadAdManagerAppOpenAd() {
        val adManagerUnitId = FirebaseRemoteConfig.getInstance()
            .getString("app_open_admanager_unit_id")

        if (adManagerUnitId.isNullOrEmpty()) {
            Log.w("AppOpenAdManager", "Remote config Ad Manager ID is empty")
            return
        }

        val adRequest = AdManagerAdRequest.Builder().build()
        AppOpenAd.load(
            activity, adManagerUnitId, adRequest,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    ad.show(activity)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AppOpenAdManager", "Ad Manager App Open Ad failed: ${error.message}")
                }
            }
        )
    }
}
