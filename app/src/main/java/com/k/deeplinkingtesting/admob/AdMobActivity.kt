package com.k.deeplinkingtesting.admob

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowMetrics
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.izooto.iZooto
import com.k.deeplinkingtesting.AdConfig
import com.k.deeplinkingtesting.CommonActivity
import com.k.deeplinkingtesting.GAMAdManager
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.appopen.OnAdsCallbackListener


class AdMobActivity : AppCompatActivity()
 {

    private var nativeAd: NativeAd? = null
    private var mainLayout : LinearLayout? = null
    private var scrollView : ScrollView? = null
     private lateinit var adManagerAdView: AdManagerAdView
     private var ad_container_admob: LinearLayout? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_mob)

        rewarded()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "News Feed"
        scrollView = findViewById(R.id.scrollView)
        mainLayout = findViewById(R.id.mainLayout)
        ad_container_admob = findViewById<LinearLayout>(R.id.ad_container)

        iZooto.enablePulse(this,scrollView,mainLayout,true)
        loadBannerAds()

//        val adRequest = AdManagerAdRequest.Builder().build()
////
////        // Load the ad
//        adManagerAdView.loadAd(adRequest)
////
//        // Set listeners for ad loading success or failure
//        adManagerAdView.adListener = object : com.google.android.gms.ads.AdListener() {
//            override fun onAdLoaded() {
//                Log.e("Ad show","successfully")
//                // Ad successfully loaded
//               // Toast.makeText(applicationContext, "Ad Loaded!", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                Log.e("Ad show",""+adError.message)
//
////                // Failed to load the ad
////               // Toast.makeText(
////                    applicationContext,
////                    "Failed to Load Ad: ${adError.message}",
////                    Toast.LENGTH_SHORT
////                ).show()
//            }
//        }


         commonInit();


//        val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, getScreenWidthInDp())
//
//        val adManagerAdView1 = AdManagerAdView(this).apply {
//            adUnitId = resources.getString(R.string.gam_banner) // Replace with your actual ad unit ID
//            setAdSize(adSize) // Explicitly set the ad size
//        }
//
//        adContainer.addView(adManagerAdView1)
//
//        val adRequest1 = AdManagerAdRequest.Builder().build()
//        adManagerAdView1.loadAd(adRequest1)
//       // DATBErrorReporting.getInstance().reportErrorToServer("Ads is failed")
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
     private fun loadBannerAds() {
         val bannerAdUnit = if ( AdConfig.r_bannerAdUnitId.isNotEmpty()) {
             AdConfig.r_bannerAdUnitId
         } else {
             resources.getString(R.string.gam_banner)
         }
         Log.e("Banner AdUnit ID",bannerAdUnit)

         val adSize =
             AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, getScreenWidthInDp())
         adManagerAdView = AdManagerAdView(this).apply {
             adUnitId = bannerAdUnit
             setAdSize(adSize)
         }
         ad_container_admob?.removeAllViews() // Ensure only one ad is shown
         ad_container_admob?.addView(adManagerAdView)
         val adRequest = AdManagerAdRequest.Builder().build()
         adManagerAdView.loadAd(adRequest)
         adManagerAdView.adListener = object : AdListener() {
             override fun onAdLoaded() {
                 Log.d(
                     "TAG",
                     "Banner adapter class name:" + adManagerAdView.responseInfo?.mediationAdapterClassName
                 )
             }

             override fun onAdFailedToLoad(adError: LoadAdError) {
                 Log.e("TAG", "Failed to load ad: ${adError.message}")

             }
         }
     }

     private fun rewarded() {
         val gam_rewarded = if (AdConfig.r_bannerAdUnitId.isNotEmpty()) {
             AdConfig.r_bannerAdUnitId
         } else {
             resources.getString(R.string.gam_rewarded)
         }
         Log.e("GAM Rewarded AdUnit ID",gam_rewarded)



        GAMAdManager.showRewardedAd(this,gam_rewarded, object : OnAdsCallbackListener{
            override fun onComplete() {
                super.onComplete()
                Log.d("CommonActivity", "onComplete.")
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.d("CommonActivity", "onAdImpression.")
            }

            override fun onUserEarnedReward(type: String?, amount: Int) {
                super.onUserEarnedReward(type, amount)
                Log.d("CommonActivity", "Reward->  $amount")

            }

            override fun onError(var1: Int, var2: String?) {
                super.onError(var1, var2)
                Log.e("CommonActivity", "Error: $var1, $var2")
            }
        })
     }

     private fun commonInit() {

         // Set Error Reporting Values
       //  DATBErrorReporting.init(this)

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
        }
        catch (ex:Exception)
        {
            Log.e("Tag",ex.toString())
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
                onBackPressed()
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
}