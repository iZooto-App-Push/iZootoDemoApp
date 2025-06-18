package com.k.deeplinkingtesting.admob

import android.view.ViewGroup
import com.adsbynimbus.*
import com.adsbynimbus.openrtb.request.Format
import com.adsbynimbus.request.*

lateinit var nimbusAdManager: NimbusAdManager

fun bannerRequest(
    container: ViewGroup,
    position: String, 
    listener: NimbusAdManager.Listener,
) {
    val request = NimbusRequest.forBannerAd(
        position = position, 
        format = Format.BANNER_320_50,
    ).apply {
        // Replace adUnitId with your AdMob Banner ID
        withAdMobBanner(adUnitId = "ca-app-pub-xxxxxxxxxxxxxxxx/yyyyyyyyyy")
    }
    nimbusAdManager.showAd(request, container, listener)
}

fun mrecAdMobRequest(
    container: ViewGroup,
    position: String, 
    listener: NimbusAdManager.Listener,
) {
    val request = NimbusRequest.forBannerAd(
        position = position, 
        format = Format.MREC,
    ).apply {
        // Replace adUnitId with your AdMob Banner ID
        withAdMobBanner(adUnitId = "ca-app-pub-xxxxxxxxxxxxxxxx/yyyyyyyyyy")
    }
    nimbusAdManager.showAd(request, container, listener)
}