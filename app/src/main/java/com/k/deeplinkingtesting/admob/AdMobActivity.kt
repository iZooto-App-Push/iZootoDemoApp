package com.k.deeplinkingtesting.admob

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowMetrics
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
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
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.applovin.ApplovinAdFormatManager


class AdMobActivity : AppCompatActivity()
 {

    private var nativeAd: NativeAd? = null
    private var mainLayout : LinearLayout? = null
    private var scrollView : ScrollView? = null
    private lateinit var applovin_banner_id: FrameLayout
     private lateinit var adFormatManager: ApplovinAdFormatManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_mob)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "News Feed"
        scrollView = findViewById(R.id.scrollView)
        mainLayout = findViewById(R.id.mainLayout)
        applovin_banner_id = findViewById(R.id.applovin_banner_id)
       // iZooto.enablePulse(this,scrollView,mainLayout,true)
        adFormatManager = ApplovinAdFormatManager(this)
        adFormatManager.loadBannerAd(applovin_banner_id)



         commonInit();


//        val adContainer = findViewById<LinearLayout>(R.id.ad_container)
//        val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, getScreenWidthInDp())
//
//        val adManagerAdView1 = AdManagerAdView(this).apply {
//            adUnitId = "ca-app-pub-9298860897894361/3941078262" // Replace with your actual ad unit ID
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

     private fun commonInit() {



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