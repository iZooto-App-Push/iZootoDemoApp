package com.k.deeplinkingtesting.admob

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.k.deeplinkingtesting.AdFormatManager
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.RewardedAdManager
import com.k.deeplinkingtesting.applovin.ApplovinAdFormatManager






import com.yandex.mobile.ads.interstitial.InterstitialAdLoader


class AdMobActivity : AppCompatActivity() {
    private var mainLayout: LinearLayout? = null
    private var scrollView: ScrollView? = null
    private lateinit var applovin_banner_id: FrameLayout
    private lateinit var adFormatManager: ApplovinAdFormatManager
    private lateinit var rewardedAdManager: RewardedAdManager
    private var facebook_ads_container: LinearLayout? = null
    private var adView: AdView? = null
    private val adUnitId = "R-M-XXXXXX" // Replace with your Yandex Ad Unit ID
    var adFormatManager1: AdFormatManager? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_mob)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "News Feed"
        scrollView = findViewById(R.id.scrollView)
        mainLayout = findViewById(R.id.mainLayout)
        facebook_ads_container = findViewById(R.id.facebook_ads_container)
        applovin_banner_id = findViewById(R.id.applovin_banner_id)
        // iZooto.enablePulse(this,scrollView,mainLayout,true)
        adFormatManager = ApplovinAdFormatManager(this)
        adFormatManager.loadBannerAd(applovin_banner_id)

        rewardedAdManager = RewardedAdManager(this)
        rewardedAdManager.loadAd()
      //  loadfacebookAds(facebook_ads_container)
        loadBannerAds("")

        adFormatManager1 = AdFormatManager(this) // Initialize the object
        adFormatManager1?.loadInterstitialAd() // Use safe call (?.) to prevent null pointer exceptions



        findViewById<Button>(R.id.rewarded_ads).setOnClickListener {
            rewardedAdManager.showAd { rewardItem ->
                Toast.makeText(
                    this,
                    "Reward Earned: ${rewardItem.amount} ${rewardItem.type}",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

    }

    private fun loadfacebookAds(facebook_ads_container: LinearLayout?) {
        // Create and Load AdView
        adView = AdView(this, "VID_HD_9_16_39S_APP_INSTALL#3905158796364465_3905166553030356", AdSize.RECTANGLE_HEIGHT_250)

        // Find the Ad Container
        // val adContainer = findViewById<LinearLayout>(R.id.facebook_ads_container)
        facebook_ads_container?.addView(adView)


        // Request an ad and set listener
        // Use fully qualified name for Facebook AdListener
        val adListener = object : com.facebook.ads.AdListener {
            override fun onError(ad: com.facebook.ads.Ad?, adError: com.facebook.ads.AdError) {
                Log.e("FAN_ADS", "Ad failed to load: ${adError.errorMessage}")
            }

            override fun onAdLoaded(ad: com.facebook.ads.Ad?) {
                Log.d("FAN_ADS", "Ad successfully loaded!")
            }

            override fun onAdClicked(ad: com.facebook.ads.Ad?) {
                Log.d("FAN_ADS", "Ad clicked!")
            }

            override fun onLoggingImpression(ad: com.facebook.ads.Ad?) {
                Log.d("FAN_ADS", "Ad impression logged!")
            }
        }

// Load the Ad with AdListener
        adView?.loadAd(
            adView?.buildLoadAdConfig()
                ?.withAdListener(adListener) // Attach the listener
                ?.build()
        )
    }
    private fun loadBannerAds(bannerAdsUnitID: String) {
        val defaultAdUnit = "/23206713921/izooto_demo/com.k.deeplinkingtesting_banner"// // GAM Test Ad Unit ID\n"//"/23206713921/izooto_demo/com.k.deeplinkingtesting_banner"
        val bannerAdUnit = if (bannerAdsUnitID.isNotEmpty()) bannerAdsUnitID else defaultAdUnit
        val adSize = com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, getScreenWidthInDp())
        val adManagerAdView = AdManagerAdView(this).apply {
            adUnitId = bannerAdUnit
            setAdSize(adSize)
        }
        facebook_ads_container?.removeAllViews() // Ensure only one ad is shown
        facebook_ads_container?.addView(adManagerAdView)
        val adRequest = AdManagerAdRequest.Builder().build()
        adManagerAdView.loadAd(adRequest)
        var hasRetried = false
        adManagerAdView.adListener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdLoaded() {
                // val responseInfo = adManagerAdView.responseInfo
                //val mediationAdapterClassName = responseInfo?.mediationAdapterClassName


                Log.d("AdManager", "Ad loaded from: $")

            }
            override fun onAdFailedToLoad(adError: com.google.android.gms.ads.LoadAdError) {
                Log.e("AdManager", "Failed to load ad: ${adError.message}")
                if (!hasRetried) {
                    hasRetried = true
                    //  fetchRemoteConfig()
                    // loadBannerAds("ca-app-pub-9298860897894361/3941078262")
                }
                // loadBannerAds("ca-app-pub-9298860897894361/3941078262")

            }
        }
    }
    private fun getScreenWidthInDp(): Int {
        val displayMetrics = resources.displayMetrics
        return (displayMetrics.widthPixels / displayMetrics.density).toInt()
    }
    // Helper function to get screen width in dp


    // Get the ad size with screen width.


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




