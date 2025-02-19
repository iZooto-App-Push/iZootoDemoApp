package com.k.deeplinkingtesting

import android.app.Activity
import android.app.Application
import com.k.deeplinkingtesting.rewarded.AppRewarded
import com.k.deeplinkingtesting.appopen.AppOpenAdManager
import com.k.deeplinkingtesting.appopen.OnAdsCallbackListener
import com.k.deeplinkingtesting.interstitial.AppInterstitial

class GAMAdManager {
    companion object {

        fun showInterstitialAd(
            context: Activity,
            adUnitId: String,
            appInterstitial: OnAdsCallbackListener
        ) {
            AppInterstitial().showInterstitialAd(context, adUnitId, appInterstitial)

        }

        fun showRewardedAd(
            context: Activity,
            adUnitId: String,
            appInterstitial: OnAdsCallbackListener
        ) {
            AppRewarded().showRewardedAd(context, adUnitId, appInterstitial)
        }


        fun setAppOpenManager(
            context: Application,
            adUnitId: String,
            listener: OnAdsCallbackListener,
        ) {
            AppOpenAdManager(context, adUnitId, listener)
        }

        fun onResumeAppOpen(context: Application,
                        adUnitId: String,
                        listener: OnAdsCallbackListener,){
            AppOpenAdManager(context, adUnitId, listener).showAdIfAvailable()
        }
    }
}