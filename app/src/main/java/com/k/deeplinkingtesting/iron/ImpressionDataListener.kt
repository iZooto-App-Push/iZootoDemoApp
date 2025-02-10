package com.k.deeplinkingtesting.iron

import com.ironsource.mediationsdk.impressionData.ImpressionData
import com.ironsource.mediationsdk.impressionData.ImpressionDataListener
import com.k.deeplinkingtesting.iron.IronSourceActivity.Companion.logCallbackName

class ImpressionDataListener :
    ImpressionDataListener {
    private val TAG = ImpressionDataListener::class.java.name

    /**
    Called when the ad was displayed successfully and the impression data was recorded
    @param impressionData The recorded impression data
     */
    override fun onImpressionSuccess(impressionData: ImpressionData) {
        logCallbackName(TAG, "")
    }
}