package com.k.deeplinkingtesting.remoteconfigData

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class InterstitialAdHandler(private val context: Context) {

    private var admobInterstitial: InterstitialAd? = null
    private var adManagerInterstitial: AdManagerInterstitialAd? = null
    private lateinit var remoteConfig: FirebaseRemoteConfig

    private var admobUnitId: String = ""
    private var adManagerUnitId: String = ""

    // ✅ Default fallback AdMob ID (Test ID shown here)
    private val defaultAdMobUnitId = "ca-app-pub-3940256099942544/1033173712"

    fun loadFromRemoteConfig(onReady: () -> Unit) {
        remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                admobUnitId = remoteConfig.getString("admob_interstitial_id")
                adManagerUnitId = remoteConfig.getString("admanager_interstitial_id")

                // Fallback to default AdMob if RemoteConfig fails or returns empty
                if (admobUnitId.isBlank()) {
                    admobUnitId = defaultAdMobUnitId
                    Log.d("InterstitialAdHandler", "Using default AdMob interstitial ID")
                }

                onReady()
            }
    }

    fun loadAdMobInterstitial(onAdLoaded: () -> Unit, onAdFailed: () -> Unit) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            admobUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    admobInterstitial = ad
                    onAdLoaded()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("InterstitialAdHandler", "AdMob load failed: ${error.message}")
                    onAdFailed()
                }
            })
    }

    fun loadAdManagerInterstitial(onAdLoaded: () -> Unit) {
        val adRequest = AdManagerAdRequest.Builder().build()
        AdManagerInterstitialAd.load(
            context,
            adManagerUnitId,
            adRequest,
            object : AdManagerInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: AdManagerInterstitialAd) {
                    adManagerInterstitial = ad
                    onAdLoaded()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("InterstitialAdHandler", "AdManager failed: ${error.message}")
                }
            })
    }

    fun showInterstitialIfAvailable(activity: Activity) {
        admobInterstitial?.let {
            it.show(activity)
        } ?: adManagerInterstitial?.let {
            it.show(activity)
        } ?: Log.d("InterstitialAdHandler", "No interstitial ad available to show")
    }
}
