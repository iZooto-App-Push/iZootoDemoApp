package com.k.deeplinkingtesting.remoteconfigData

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class RewardedAdManager(private val activity: Activity) {

    private val admobRewardedAdUnitId = "ca-app-pub-3940256099942544/5224354917" // ✅ AdMob test ID
    private var rewardedAd: RewardedAd? = null

    fun loadAndShowRewardedAd() {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            activity, admobRewardedAdUnitId, adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    showRewardedAd(ad)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.w("RewardedAdManager", "AdMob load failed: ${loadAdError.message}")
                    loadAdManagerRewardedAd() // fallback
                }
            }
        )
    }

    private fun loadAdManagerRewardedAd() {
        val adManagerUnitId = FirebaseRemoteConfig.getInstance()
            .getString("rewarded_admanager_unit_id")

        if (adManagerUnitId.isNullOrEmpty()) {
            Log.w("RewardedAdManager", "Remote config Ad Manager Rewarded ID is empty")
            return
        }

        val adRequest = AdManagerAdRequest.Builder().build()

        RewardedAd.load(
            activity, adManagerUnitId, adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    showRewardedAd(ad)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e("RewardedAdManager", "Ad Manager Rewarded load failed: ${loadAdError.message}")
                }
            }
        )
    }

    private fun showRewardedAd(ad: RewardedAd) {
        ad.show(activity) { rewardItem ->
            val amount = rewardItem.amount
            val type = rewardItem.type
            Log.i("RewardedAdManager", "User earned reward: $amount $type")
            // Handle reward
        }
    }
}
