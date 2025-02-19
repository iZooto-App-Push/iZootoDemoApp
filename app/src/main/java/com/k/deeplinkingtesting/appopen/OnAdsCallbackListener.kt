package com.k.deeplinkingtesting.appopen

import com.google.android.gms.ads.AdValue

interface OnAdsCallbackListener {

    fun onUserEarnedReward(type: String?, amount: Int){}

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

    fun onComplete(){}

    fun onError(var1: Int, var2: String?) {
    }

    fun onWarning(var1: Int, var2: String?) {
    }
}