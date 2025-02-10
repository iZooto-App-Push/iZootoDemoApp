package com.k.deeplinkingtesting.iron

import com.k.deeplinkingtesting.iron.IronSourceActivity.Companion.logCallbackName
import com.unity3d.mediation.LevelPlayAdError
import com.unity3d.mediation.LevelPlayAdInfo
import com.unity3d.mediation.rewarded.LevelPlayReward
import com.unity3d.mediation.rewarded.LevelPlayRewardedAdListener

class RewardedAdListener(private val listener: ActivityListener): LevelPlayRewardedAdListener{
    private val TAG = RewardedAdListener::class.java.name
    override fun onAdDisplayed(adInfo: LevelPlayAdInfo) {
        logCallbackName(TAG, "adInfo = $adInfo")
        listener.setEnablementForButton(ButtonIdentifiers.SHOW_REWARDED_BUTTON_IDENTIFIER, false)
    }

    override fun onAdLoadFailed(error: LevelPlayAdError) {
        logCallbackName(TAG, "error = $error | adInfo = ${error.getErrorCode()}")
    }

    override fun onAdLoaded(adInfo: LevelPlayAdInfo) {
        logCallbackName(TAG, "adInfo = $adInfo")
        listener.setEnablementForButton(ButtonIdentifiers.SHOW_REWARDED_BUTTON_IDENTIFIER, true)
    }

    override fun onAdRewarded(reward: LevelPlayReward, adInfo: LevelPlayAdInfo) {
        logCallbackName(TAG, "placement = $reward | adInfo = $adInfo")
        listener.setPlacementInfo(reward)
    }
}