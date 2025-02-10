package com.k.deeplinkingtesting.iron

import android.view.View
import com.ironsource.mediationsdk.ads.nativead.LevelPlayNativeAd
import com.ironsource.mediationsdk.ads.nativead.LevelPlayNativeAdListener
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.k.deeplinkingtesting.iron.IronSourceActivity.Companion.logCallbackName

class NativeAdListener(private val listener: ActivityListener): LevelPlayNativeAdListener {
    private val TAG = NativeAdListener::class.java.name
    override fun onAdClicked(nativeAd: LevelPlayNativeAd?, adInfo: AdInfo?) {
        logCallbackName(TAG, "adInfo = $adInfo")
    }

    override fun onAdImpression(nativeAd: LevelPlayNativeAd?, adInfo: AdInfo?) {
        logCallbackName(TAG, "{$adInfo, $nativeAd}")
        listener.setEnablementForButton(ButtonIdentifiers.LOAD_NATIVE_BUTTON_IDENTIFIER, false)
    }

    override fun onAdLoadFailed(nativeAd: LevelPlayNativeAd?, error: IronSourceError?) {
        logCallbackName(TAG, "error = $error")
    }

    override fun onAdLoaded(nativeAd: LevelPlayNativeAd?, adInfo: AdInfo?) {
        logCallbackName(TAG, "adInfo = $adInfo")
        listener.setNativeViewVisibility(View.VISIBLE)
        listener.setEnablementForButton(ButtonIdentifiers.LOAD_NATIVE_BUTTON_IDENTIFIER, true)
    }
}