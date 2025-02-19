package com.k.deeplinkingtesting.rewarded

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnPaidEventListener
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.k.deeplinkingtesting.appopen.OnAdsCallbackListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class AppRewarded {

    private val tag = "AppRewarded"
    private var mRewarded: RewardedAd? = null

    internal fun showRewardedAd(
        context: Activity,
        adUnitId: String,
        listener: OnAdsCallbackListener
    ){
        // Remove any previous ad
        mRewarded?.fullScreenContentCallback = null
        mRewarded = null
        val adRequest = AdManagerAdRequest.Builder().build()
        RewardedAd.load(context,adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(tag, adError.toString())
                mRewarded = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d(tag, "Ad was loaded.")
                mRewarded = ad
                callback(context, listener)
            }
        })
    }

    fun callback(context: Activity, listener: OnAdsCallbackListener) {
        if (mRewarded != null) {
            mRewarded?.let { ad ->
                ad.show(context) { rewardItem ->
                    // Handle the reward.
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    listener.onUserEarnedReward(rewardType, rewardAmount)
                    Log.d(tag, "User earned the reward.")
                }
            } ?: run {
                Log.d(tag, "The rewarded ad wasn't ready yet.")
            }
            listener.onComplete()
        }else{
            listener.onAdNotLoadedYet()
        }


        mRewarded?.onPaidEventListener = OnPaidEventListener {
            listener.onPaidEventReceived(it)
            val currencyCode = it.currencyCode
            if (currencyCode == "USD") {
                val valueMicros = it.valueMicros + 1000// dollars to micro
                val usdToInrRate = 83.0 // Example rate; use the current rate
                val valueInr = valueMicros * usdToInrRate
                // Log.d(tag, "Total cost mile--> ₹$valueInr, currencyCode: -> $currencyCode")
            }
        }

        mRewarded?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(tag, "Ad was clicked.")
                listener.onAdClicked()
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(tag, "Ad dismissed fullscreen content.")
                listener.onAdDismissedFullScreenContent()
                mRewarded = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                Log.e(tag, "Ad failed to show fullscreen content.")
                listener.onError(p0.code, p0.message)
                mRewarded = null
            }


            override fun onAdImpression() {
                //impressionCount++
                // Called when an impression is recorded for an ad.
                Log.d(tag, "Ad recorded an impression.")
                listener.onAdImpression()
                incrementImpressionCount(context)

            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(tag, "Ad showed fullscreen content.")
                listener.onAdShowedFullScreenContent()
            }
        }

    }

    private fun fetchUsdToInrRate(): Double {
        val url = "https://api.exchangerate-api.com/v4/latest/USD"  // Example API URL
        val client = OkHttpClient()

        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw java.io.IOException("Unexpected code $response")

        val jsonResponse = JSONObject(response.body?.string() ?: "")
        return jsonResponse.getJSONObject("rates").getDouble("INR")
    }

    private fun convertUsdToInr(usdAmount: Double): Double {
        val conversionRate = fetchUsdToInrRate()
        return usdAmount * conversionRate
    }


    private fun incrementImpressionCount(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val impressionsCounter = context.getSharedPreferences("AdPrefs", Context.MODE_PRIVATE)
            val currentCount = impressionsCounter.getInt("ImpressionCount", 0)
            val impressionCount = currentCount + 1
            impressionsCounter.edit().putInt("ImpressionCount", impressionCount).apply()

            if (impressionCount == 6) {
                // Perform network operation in a coroutine
                val usdAmount = 2.5  // Amount in USD
                var inrAmount: Double? = null

                try {
                    inrAmount = convertUsdToInr(usdAmount * impressionCount * 10000 / 1000)
                    withContext(Dispatchers.Main) {
                        // Update UI on the main thread
                        Log.e("inr","Advertiser is offer per CPC the USD dollar is: $usdAmount USD to INR. $inrAmount")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to fetch conversion rate: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
                Log.d("inr", "Advertiser pays $usdAmount USD")
                Log.d("inr", "Total impressions are: ${impressionCount * 10000}")
                Log.d("inr", "Total revenue-> : $inrAmount")
                impressionsCounter.edit().putInt("ImpressionCount", 0).apply()
            }

        }
    }


}