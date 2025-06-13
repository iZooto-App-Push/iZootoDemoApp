package com.k.deeplinkingtesting.remoteconfigData

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.k.deeplinkingtesting.R

class NativeAdManager(
    private val context: Context,
    private val adContainer: FrameLayout
) {

    private val admobNativeAdUnitId = "ca-app-pub-3940256099942544/2247696110" // ✅ Default AdMob Native test ID

    fun loadNativeAd() {
        val adLoader = AdLoader.Builder(context, admobNativeAdUnitId)
            .forNativeAd { nativeAd ->
                val adView = LayoutInflater.from(context)
                    .inflate(R.layout.native_ad_layout, null) as NativeAdView
                populateNativeAdView(nativeAd, adView)
                adContainer.removeAllViews()
                adContainer.addView(adView)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.w("NativeAdManager", "AdMob Native ad failed: ${adError.message}")
                    loadAdManagerNativeAd() // fallback
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun loadAdManagerNativeAd() {
        val adManagerUnitId = FirebaseRemoteConfig.getInstance()
            .getString("native_admanager_unit_id")

        if (adManagerUnitId.isNullOrEmpty()) {
            Log.w("NativeAdManager", "Remote config Ad Manager native ID is empty")
            return
        }

        val adLoader = AdLoader.Builder(context, adManagerUnitId)
            .forNativeAd { nativeAd ->
                val adView = LayoutInflater.from(context)
                    .inflate(R.layout.native_ad_layout, null) as NativeAdView
                populateNativeAdView(nativeAd, adView)
                adContainer.removeAllViews()
                adContainer.addView(adView)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("NativeAdManager", "Ad Manager Native ad failed: ${adError.message}")
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdManagerAdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Bind your native ad view elements (mediaView, headlineView, etc.)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        (adView.headlineView as TextView).text = nativeAd.headline
        adView.setNativeAd(nativeAd)
    }
}
