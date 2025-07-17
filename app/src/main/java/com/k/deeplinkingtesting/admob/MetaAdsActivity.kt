package com.k.deeplinkingtesting.admob

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowMetrics
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.adsbynimbus.NimbusAdManager
import com.adsbynimbus.NimbusError
import com.adsbynimbus.openrtb.enumerations.Position
import com.adsbynimbus.openrtb.request.Format
import com.adsbynimbus.render.AdController
import com.adsbynimbus.request.NimbusRequest
import com.adsbynimbus.request.NimbusResponse

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.k.deeplinkingtesting.R


class MetaAdsActivity : AppCompatActivity() {

    private var nativeAd: NativeAd? = null
    private var mainLayout: LinearLayout? = null
    private var scrollView: ScrollView? = null
    private lateinit var adManagerAdView: AdManagerAdView
    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var rewarded_ads: Button
    private lateinit var interstitial_ads: Button
    private val adUnitId = "ca-app-pub-3940256099942544/5224354917" // Test ID
    private var rewardedAd: RewardedAd? = null
    private var adLayout: LinearLayout? = null
    private var footerView: LinearLayout? = null
    val refreshIntervalSeconds: Int = 30

    private val nimbusAdManager = NimbusAdManager()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_mob)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Meta Ads Network"
        loadRewardedAd(this)

        // adManagerAdView = findViewById(R.id.adManagerView)
        adLayout = findViewById(R.id.mainView)
        footerView = findViewById(R.id.footerView)

        rewarded_ads = findViewById(R.id.rewarded_ads)
        rewarded_ads.setOnClickListener {
            showRewardedAd(this)
        }
        interstitial_ads = findViewById(R.id.interstitial_ads)
        interstitial_ads.setOnClickListener {
            showInterstitialAds(this)
        }


// meta ads

        adLayout?.let {
            nimbusAdManager.showAd(
                NimbusRequest.forBannerAd("3905158796364465_3905166553030356", Format.BANNER_320_50,
                    0).apply {
                }, it,
                object : NimbusAdManager.Listener {
                    override fun onAdResponse(nimbusResponse: NimbusResponse) {
                        Log.e("AdResponse","onAdResponse"+nimbusResponse.toString())
                    }

                    override fun onAdRendered(controller: AdController) {
                        Log.e("AdResponse","controller"+controller.view)

                    }

                    override fun onError(error: NimbusError) {
                        Log.e("AdResponse","error"+error.toString())
                    }
                })

        }


        //footer view

        footerView?.let {
            nimbusAdManager.showAd(
                NimbusRequest.forBannerAd("test_banner", Format.BANNER_320_50,
                    Position.FOOTER),refreshIntervalSeconds, it,
                object : NimbusAdManager.Listener {
                    override fun onAdResponse(nimbusResponse: NimbusResponse) {
                        Log.e("AdResponse","Footer onAdResponse"+nimbusResponse.toString())
                    }

                    override fun onAdRendered(controller: AdController) {
                        Log.e("AdResponse","Footer controller"+controller.view)

                    }

                    override fun onError(error: NimbusError) {
                        Log.e("AdResponse","Footer error"+error.toString())
                    }
                })

        }










//        try {
//
//            val adRequest = AdRequest.Builder().build()
//            adManagerAdView.loadAd(adRequest)
//            adManagerAdView.adListener = object : AdListener() {
//                override fun onAdLoaded() {
//                    Log.d("iZooto Demo App - Home Screen", "Ad loaded successfully:")
//                }
//
//                override fun onAdFailedToLoad(adError: com.google.android.gms.ads.LoadAdError) {
//                    Log.e("iZooto  Demo App - Home Screen", "Failed to load ad: ${adError.message}")
//                }
//            }
//        }
//        catch (e:Exception){
//            Log.e("Exception ex",e.toString())
//        }
//
        // Set listeners for ad loading success or failure
//        adManagerAdView.adListener = object : com.google.android.gms.ads.AdListener() {
//            override fun onAdLoaded() {
//                Log.e("iZooto Demo Banner", "successfully")
//            }
//
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                Log.e("iZooto Demo Banner", "" + adError.message)
//
//            }
//        }


        // loadInterAds()
//        val adContainer = findViewById<LinearLayout>(R.id.ad_container)
//        val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, getScreenWidthInDp())
//
//        val adManagerAdView1 = AdManagerAdView(this).apply {
//            adUnitId = "/23206713921/izooto_demo/com.k.deeplinkingtesting_banner" // Replace with your actual ad unit ID
//            setAdSize(adSize) // Explicitly set the ad size
//        }
//
//        adContainer.addView(adManagerAdView1)
//
//        val adRequest1 = AdManagerAdRequest.Builder().build()
//        adManagerAdView1.loadAd(adRequest1)
//
//        adManagerAdView1.adListener = object : com.google.android.gms.ads.AdListener() {
//            override fun onAdLoaded() {
//                Log.d("AdManager", "Ad loaded successfully")
//            }
//
//            override fun onAdFailedToLoad(adError: com.google.android.gms.ads.LoadAdError) {
//                Log.e("AdManager", "Failed to load ad: ${adError.message}")
//
//            }
//        }
    }

    private fun showInterstitialAds(activity: MetaAdsActivity) {
        nimbusAdManager?.showBlockingAd(NimbusRequest.forRewardedVideo("3905158796364465_3929140407299637"), this, object : NimbusAdManager.Listener {
            override fun onAdResponse(nimbusResponse: NimbusResponse) {
                Log.e("showInterstitialAds","NimbusResponse"+nimbusResponse.toString())            }

            override fun onAdRendered(controller: AdController) {
                Log.e("showInterstitialAds","AdController"+controller.toString())            }

            override fun onError(error: NimbusError) {
                Log.e("showInterstitialAds","error"+error.toString())            }
        })




    }


    private fun loadInterAds() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            getString(R.string.gam_inter),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    mInterstitialAd = ad
                    Log.e("iZooto Demo App Interstitial Ads", "Successfully loaded")

                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    Log.e("iZooto Demo App", adError.message)
                }
            })
    }


    // Helper function to get screen width in dp

    private fun getScreenWidthInDp(): Int {
        val displayMetrics = resources.displayMetrics
        return (displayMetrics.widthPixels / displayMetrics.density).toInt()
    }

    private fun loadNativeAd() {
        try {
            val adLoader = AdLoader.Builder(this, AdUnitConfig.nativeAdUnitId)
                .forNativeAd { ad: NativeAd ->
                    // Show the ad
                    if (isDestroyed) {
                        ad.destroy()
                        return@forNativeAd
                    }
                    nativeAd = ad
                    // populateNativeAdView(ad, native_ad_view)
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        // Handle the error
                        println("Ad failed to load: ${adError.message}")
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        println("Ad  to load:Clicked")

                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        println("Ad  to load:impression")

                    }

                    override fun onAdOpened() {
                        super.onAdOpened()
                        println("Ad  to load:opened")

                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        println("Ad  to load:closed")

                    }
                })
                .withNativeAdOptions(NativeAdOptions.Builder().build())
                .build()

            adLoader.loadAd(AdRequest.Builder().build())
        } catch (ex: Exception) {
            Log.e("Tag", ex.toString())
        }
    }

    // Get the ad size with screen width.
    private val adSize: AdSize
        get() {
            val displayMetrics = resources.displayMetrics
            val adWidthPixels =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val windowMetrics: WindowMetrics = this.windowManager.currentWindowMetrics
                    windowMetrics.bounds.width()
                } else {
                    displayMetrics.widthPixels
                }
            val density = displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (mInterstitialAd != null) {
                    mInterstitialAd?.setFullScreenContentCallback(object :
                        FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            mInterstitialAd = null
                            // Go back after the ad is dismissed
                            onBackPressed()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            mInterstitialAd = null
                            onBackPressed()
                        }
                    })
                    mInterstitialAd?.show(this)
                } else {
                    onBackPressed()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    // Handle lifecycle methods to properly pause and resume the ad
    override fun onPause() {
        super.onPause()
        // adManagerAdView.pause()
    }

    override fun onResume() {
        super.onResume()
        // adManagerAdView.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        // adManagerAdView.destroy()
    }


    fun showRewardedAd(activity: Activity) {
        nimbusAdManager?.showBlockingAd(NimbusRequest.forRewardedVideo("1").apply {
        }, this, object : NimbusAdManager.Listener {
            override fun onAdResponse(nimbusResponse: NimbusResponse) {
                Log.e("showInterstitialAds","NimbusResponse"+nimbusResponse.toString())            }

            override fun onAdRendered(controller: AdController) {
                Log.e("showInterstitialAds","AdController"+controller.toString())            }

            override fun onError(error: NimbusError) {
                Log.e("showInterstitialAds","error"+error.toString())            }
        })












//        rewardedAd?.show(activity) { rewardItem: RewardItem ->
//            val rewardAmount = rewardItem.amount
//            val rewardType = rewardItem.type
//          //  Toast.makeText(activity, "Reward: $rewardAmount $rewardType", Toast.LENGTH_SHORT).show()
//            Log.e("YieldMonk ","Rewarded Ads is  loaded")
//
//            // 👉 Unlock the reward (e.g., give coins, unlock level, etc.)
//        } ?: run {
//            Log.e("YieldMonk ","Rewarded Ads is not loaded")
//          //  Toast.makeText(activity, "Ad not loaded yet", Toast.LENGTH_SHORT).show()
//        }
    }

    fun loadRewardedAd(context: Context) {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(context, "adUnitId", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
                Log.d("AdMob", "Rewarded ad loaded.")
                rewardedAd?.setFullScreenContentCallback(object : FullScreenContentCallback() {
                    override fun onAdShowedFullScreenContent() {
                        Log.d("AdMob", "Rewarded ad is shown.")
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e("AdMob", "Failed to show rewarded ad: ${adError.message}")
                        rewardedAd = null
                    }

                    override fun onAdDismissedFullScreenContent() {
                        Log.d("AdMob", "Rewarded ad dismissed.")
                        rewardedAd = null
                        loadRewardedAd(context) // Preload next ad
                    }

                    override fun onAdImpression() {
                        Log.d("AdMob", "Rewarded ad impression logged.")
                    }

                    override fun onAdClicked() {
                        Log.d("AdMob", "Rewarded ad clicked.")
                    }
                })
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.e("AdMob", "Failed to load rewarded ad: ${error.message}")
                rewardedAd = null
            }


        })
    }
}
