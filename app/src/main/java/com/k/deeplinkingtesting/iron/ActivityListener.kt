package com.k.deeplinkingtesting.iron

import com.ironsource.mediationsdk.model.Placement
import com.unity3d.mediation.rewarded.LevelPlayReward

interface ActivityListener {
    fun setEnablementForButton(buttonIdentifier: ButtonIdentifiers, enable: Boolean)
    fun setBannerViewVisibility(visibility: Int)
    fun setNativeViewVisibility(visibility: Int)
    fun setPlacementInfo(placementInfo: Placement)
    fun setPlacementInfo(placementInfo: LevelPlayReward)
    fun showRewardDialog()
    fun createInterstitialAd()
    fun createBannerAd()
    fun createRewardedAd()
    fun createNativeAd()
}