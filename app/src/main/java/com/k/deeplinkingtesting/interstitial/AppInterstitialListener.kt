package com.k.deeplinkingtesting.interstitial

import com.google.android.gms.ads.AdValue

interface AppInterstitialListener {

    fun onAdClicked() {
    }

    fun onAdImpression() {
    }

    fun onPaidEventReceived(valueMicros: AdValue) {
    }

    fun onAdShowedFullScreenContent() {
    }

    fun onAdDismissedFullScreenContent() {
    }

    fun onAdNotLoadedYet() {
    }

    fun onComplete()

    fun onError(var1: Int, var2: String?) {
    }

    fun onWarning(var1: Int, var2: String?) {
    }
}