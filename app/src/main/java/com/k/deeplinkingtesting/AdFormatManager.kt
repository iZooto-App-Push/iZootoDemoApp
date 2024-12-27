package com.k.deeplinkingtesting

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView

import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader

class AdFormatManager(private val context: Activity) {
   // private var nativeAdLoader: MaxNativeAdLoader? = null
   // private var nativeAd: MaxAd? = null
    private var bannerAd: BannerAdView? = null
    private var interstitialAd: InterstitialAd? = null
    private var interstitialAdLoader: InterstitialAdLoader? = null
    private var rewardedAd: RewardedAd? = null
    private var rewardedAdLoader: RewardedAdLoader? = null

     fun loadBannerAd(context: Activity,
                             adSizes: BannerAdSize,
                             binding: BannerAdView,
                             errorMessage: TextView,
                             loading: ProgressBar
    ): BannerAdView {
       // loading.loadingShow()
       // errorMessage.loadingHide()
        return binding.apply {
            setAdSize(adSizes)
            setAdUnitId("R-M-13309303-3")
            Log.d("Yandex", "Using AdUnitId:R-M-13309303-3")
            setBannerAdEventListener(object : BannerAdEventListener {
                override fun onAdLoaded() {
                   // loading.loadingHide()
                    Log.i("Yandex", "onAdLoaded")


                    // If this callback occurs after the activity is destroyed, you
                    // must call destroy and return or you may get a memory leak.
                    // Note `isDestroyed` is a method on Activity.
                    if (context.isDestroyed) {
                        bannerAd?.destroy()
                        return
                    }
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    // Ad failed to load with AdRequestError.
                    Log.e("Yandex", error.toString())
                   // loading.loadingHide()
                   // errorMessage.textShow()
                    // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                }

                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                override fun onLeftApplication() {
                    // Called when user is about to leave application (e.g., to go to the browser), as a result of clicking on the ad.
                }

                override fun onReturnedToApplication() {
                    // Called when user returned to application after click.
                }

                override fun onImpression(impressionData: ImpressionData?) {
                    Log.e("ImpressionData",""+impressionData?.rawData)
                    // Called when an impression is recorded for an ad.
                }
            })
            loadAd(
                AdRequest.Builder()
                    // Methods in the AdRequest.Builder class can be used here to specify individual options settings.
                    .build()
            )
        }
    }

    fun loadInterstitialAd(
        interstitial: Button,
        errorMessage: TextView,
        loading: ProgressBar,
        adsContainer: ArrayList<FrameLayout>
    ) {
        try {
            interstitial.setOnClickListener {
               // loading.loadingShow()
               // errorMessage.textHide()
                    adsContainer[0].visibility = View.GONE
                    adsContainer[1].visibility = View.GONE
                    adsContainer[2].visibility = View.GONE
                    adsContainer[3].visibility = View.GONE
                    showInterstitialAd()

            }
            // Interstitial ads loading should occur after initialization of the SDK.
            // Initialize the SDK as early as possible, for example in Application.onCreate or Activity.onCreate
            interstitialAdLoader = InterstitialAdLoader(context).apply {
                setAdLoadListener(object : InterstitialAdLoadListener {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                      //  loading.loadingHide()
                      //  errorMessage.textHide()
                        this@AdFormatManager.interstitialAd = interstitialAd
                        // The ad was loaded successfully. You can now show the ad.
                    }

                    override fun onAdFailedToLoad(error: AdRequestError) {
                        // Ad failed to load with AdRequestError.
                       // loading.loadingHide()
                       // errorMessage.textShow()
                        // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                    }
                })
            }
            loadInterstitialAd(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun loadInterstitialAd(context: Activity) {
        val adRequestConfiguration = AdRequestConfiguration.Builder(context.getString(R.string.interstitial_ad_unit)).build()
        interstitialAdLoader?.loadAd(adRequestConfiguration)
    }


    private fun showInterstitialAd() {
        interstitialAd?.apply {
            setAdEventListener(object : InterstitialAdEventListener {
                override fun onAdShown() {
                    Log.i("Yandex", "onAdFailedToShow" )
                    // Called when ad is shown.
                }
                override fun onAdFailedToShow(adError: AdError) {
                    // Called when an InterstitialAd failed to show.
                    // Clean resources after Ad dismissed
                    Log.i("Yandex", "onAdFailedToShow -> $adError" )

                    interstitialAd?.setAdEventListener(null)
                    interstitialAd = null

                    // Now you can preload the next interstitial ad.
                    loadInterstitialAd(context)
                }
                override fun onAdDismissed() {
                    // Called when an ad is dismissed.
                    // Clean resources after Ad dismissed
                    interstitialAd?.setAdEventListener(null)
                    interstitialAd = null

                    // Now you can preload the next interstitial ad.
                    loadInterstitialAd(context)
                }
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                }
                override fun onAdImpression(impressionData: ImpressionData?) {
                    // Called when an impression is recorded for an ad.
                    Log.i("Yandex", "onAdImpression -> ${impressionData?.rawData}" )
                }
            })
            show(context)
        }
    }


    fun loadRewardedAd(
        rewarded: Button,
        errorMessage: TextView,
        loading: ProgressBar,
        adsContainer: ArrayList<FrameLayout>
    ) {
        try {
            rewarded.setOnClickListener {
               // loading.loadingShow()
               // errorMessage.textHide()
                    adsContainer[0].visibility = View.GONE
                    adsContainer[1].visibility = View.GONE
                    adsContainer[2].visibility = View.GONE
                    adsContainer[3].visibility = View.GONE
                    showRewardedAd()

            }
            rewardedAdLoader = RewardedAdLoader(context).apply {
                setAdLoadListener(object : RewardedAdLoadListener {
                    override fun onAdLoaded(rewarded: RewardedAd) {
                      //  loading.loadingHide()
                       // errorMessage.textHide()
                        rewardedAd = rewarded
                        // The ad was loaded successfully. You can now show the ad.
                    }

                    override fun onAdFailedToLoad(error: AdRequestError) {
                       // loading.loadingHide()
                       // errorMessage.textShow()
                        // Ad failed to load with AdRequestError.
                        // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                    }
                })
            }
            loadRewardedAd()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadRewardedAd() {
        val adRequestConfiguration = AdRequestConfiguration.Builder(context.getString(R.string.rewarded_ad_unit)).build()
        rewardedAdLoader?.loadAd(adRequestConfiguration)
    }


    private fun showRewardedAd() {
        rewardedAd?.apply {
            setAdEventListener(object : RewardedAdEventListener {
                override fun onAdShown() {
                    // Called when an ad is shown.
                    Log.i("Yandex", "onAdShown" )

                }

                override fun onAdFailedToShow(adError: AdError) {
                    // Called when an RewardedAd failed to show
                    Log.i("Yandex", "onAdFailedToShow")

                }

                override fun onAdDismissed() {
                    // Called when an ad is dismissed.
                    // Clean resources after Ad dismissed
                    Log.i("Yandex", "onAdDismissed")

                    rewardedAd?.setAdEventListener(null)
                    rewardedAd = null

                    // Now you can preload the next rewarded ad.
                    loadRewardedAd()
                }

                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.i("Yandex", "onAdClicked")

                }

                override fun onAdImpression(impressionData: ImpressionData?) {
                    Log.i("Yandex", "onAdImpression -> $impressionData" )
                    // Called when an impression is recorded for an ad.
                }

                override fun onRewarded(reward: Reward) {
                    Log.i("Yandex", "onRewarded -> ${reward.amount}, ${reward.type}" )
                    // Called when the user can be rewarded.
                }
            })
            show(context)
        }
    }


    fun loadNativeAd(
        nativeAdContainer: FrameLayout,
        errorMessage: TextView,
        loading: ProgressBar,
        isTrue: Boolean = false,
        unitId: String

    ) {

//        try {
//           // errorMessage.loadingHide()
//            nativeAdLoader =
//                MaxNativeAdLoader(unitId, context)
//            nativeAdLoader?.setRevenueListener {
//                Log.i("Applovin", "onRevenue ($it)")
//            }
//            nativeAdLoader?.setNativeAdListener(object : MaxNativeAdListener() {
//
//                override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
//                    // Clean up any pre-existing native ad to prevent memory leaks.
//                    if (nativeAd != null) {
//                        nativeAdLoader?.destroy(nativeAd)
//                    }
//                  //  loading.loadingHide()
//                    Log.i("Applovin", "onNativeAdLoaded")
//
//                    // Save ad for cleanup.
//                    nativeAd = ad
//                    // Make sure nativeAdView is not null before adding it
//                    if (nativeAdView != null) {
//                        // Add the native ad view to the container
//                        nativeAdContainer.removeAllViews() // Remove any existing views
//                        nativeAdContainer.addView(nativeAdView)
//                    } else {
//                        Log.e("AppLovin", "Native ad view is null")
//                    }
//                }
//
//                @SuppressLint("SetTextI18n")
//                override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
//                    Log.i("Applovin", "onNativeAdLoadFailed")
//                   // loading.loadingHide()
//                   // errorMessage.textShow()
//                    errorMessage.text = "onNativeAdLoadFailed $adUnitId, $error"
//                    // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay
//                }
//
//                override fun onNativeAdClicked(ad: MaxAd) {
//                    Log.i("Applovin", "onNativeAdClicked")
//                }
//            })
//            if (isTrue) {
//               // nativeAdLoader?.loadAd(createNativeAdView(context))
//            } else {
//                nativeAdLoader?.loadAd()
//            }


       // } catch (e: Exception) {
          //  e.printStackTrace()
       // }

    }


//    private fun createNativeAdView(context: Activity): MaxNativeAdView {
//        val binder: MaxNativeAdViewBinder =
//            MaxNativeAdViewBinder.Builder(R.layout.native_custom_ad_view)
//                .setTitleTextViewId(R.id.title_text_view)
//                .setBodyTextViewId(R.id.body_text_view)
//                .setStarRatingContentViewGroupId(R.id.star_rating_view)
//                .setAdvertiserTextViewId(R.id.advertiser_textView)
//                .setIconImageViewId(R.id.icon_image_view)
//                .setMediaContentViewGroupId(R.id.media_view_container)
//                .setOptionsContentViewGroupId(R.id.ad_options_view)
//                .setCallToActionButtonId(R.id.cta_button)
//                .build()
//        return MaxNativeAdView(binder, context)
//    }

    fun onDestroy() {
        // Destroy the native ad and native ad loader to prevent memory leaks.
//        if (nativeAd != null) {
//            nativeAdLoader?.destroy(nativeAd)
//        }
//        if (nativeAdLoader != null) {
//            nativeAdLoader?.destroy()
//        }
        if (interstitialAdLoader != null) {
            interstitialAdLoader?.setAdLoadListener(null)
            interstitialAdLoader = null
            destroyInterstitialAd()
        }
        if (rewardedAdLoader != null) {
            rewardedAdLoader?.setAdLoadListener(null)
            rewardedAdLoader = null
            destroyRewardedAd()
        }
    }

    private fun destroyInterstitialAd() {
        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
    }

    private fun destroyRewardedAd() {
        rewardedAd?.setAdEventListener(null)
        rewardedAd = null
    }

}