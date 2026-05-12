package com.k.deeplinkingtesting

import android.app.Application
import android.os.Trace
import com.google.android.gms.ads.MobileAds

object AdsInitializer {

    fun init(app: Application) {
        Trace.beginSection("ads_init")

        MobileAds.initialize(app) {}

        Trace.endSection()
    }
}