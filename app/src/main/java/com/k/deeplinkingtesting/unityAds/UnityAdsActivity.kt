package com.k.deeplinkingtesting.unityAds

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.databinding.ActivityUnityAdsBinding
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAds.initialize
import com.unity3d.ads.UnityAds.load
import com.unity3d.ads.UnityAds.show
import com.unity3d.ads.UnityAdsShowOptions
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize

class UnityAdsActivity :AppCompatActivity(R.layout.activity_unity_ads),
    IUnityAdsInitializationListener {
    private var topBanner: BannerView? = null
    private var bottomBanner: BannerView? = null
    private lateinit var binding: ActivityUnityAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUnityAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Unity Ads:
        initialize(applicationContext, getString(R.string.unity_game_id), true, this)
        findViewById<Button>(R.id.interstitialAd).isEnabled = false
        findViewById<Button>(R.id.rewardedAd).isEnabled = false
        binding.loadTopBanner.isEnabled = false
        binding.loadBottomBanner.isEnabled = false
        binding.hideTopBanner.isEnabled = false
        binding.hideBottomBanner.isEnabled = false

        binding.loadTopBanner.setOnClickListener {
            binding.loadTopBanner.isEnabled = false
            // Create the top banner view object:
            topBanner =
                BannerView(this, getString(R.string.banner_ad_unit_id), UnityBannerSize.getDynamicSize(this)) //UnityBannerSize(320, 50)
            // Set the listener for banner lifecycle events:
            topBanner!!.listener = bannerListener
            loadBannerAd(topBanner!!, binding.topBanner)
        }

//        binding.loadBottomBanner.setOnClickListener {
//            binding.loadBottomBanner.isEnabled = false
//            // Create the bottom banner view object:
//            bottomBanner =
//                BannerView(this, getString(R.string.banner_ad_unit_id_2), UnityBannerSize(320, 50))
//            // Set the listener for banner lifecycle events:
//            bottomBanner!!.listener = bannerListener
//            loadBannerAd(bottomBanner!!, binding.bottomBanner)
//        }

        binding.hideTopBanner.setOnClickListener { // Remove content from the banner view:
            binding.topBanner.removeAllViews()
            // Remove the banner variables:
            // topBannerView = null
            topBanner = null
            binding.loadTopBanner.isEnabled = true
            binding.hideTopBanner.isEnabled = false
        }

        binding.hideBottomBanner.setOnClickListener { // Remove content from the banner view:
            binding.bottomBanner.removeAllViews()
            // Remove the banner variables:
            //bottomBannerView = null
            bottomBanner = null
            binding.loadBottomBanner.isEnabled = true
            binding.hideBottomBanner.isEnabled = false
        }

    }

    private fun loadBannerAd(bannerView: BannerView, bannerLayout: FrameLayout) {
        // Request a banner ad:
        bannerView.load()
        // Associate the banner view object with the banner view:
        bannerLayout.addView(bannerView)
    }

    override fun onInitializationComplete() {
        // Enable the show ad buttons because ads can now be loaded
        binding.loadTopBanner.isEnabled = true
        binding.loadBottomBanner.isEnabled = true
        findViewById<Button>(R.id.interstitialAd).isEnabled = true
        findViewById<Button>(R.id.rewardedAd).isEnabled = true
        findViewById<Button>(R.id.interstitialAd).setOnClickListener {
            //UnityAdsManager(this).displayInterstitialAd(binding)
            binding.interstitialAd.isEnabled = false
        }
        findViewById<Button>(R.id.rewardedAd).setOnClickListener {
            displayRewardedAd()
            binding.rewardedAd.isEnabled = false

        }
        UnityAdsManager(this).disableSSLCertificateValidation()
        Log.d("UnityAdsExample", "Initialization is successful!")
    }

    override fun onInitializationFailed(
        error: UnityAds.UnityAdsInitializationError?,
        message: String?
    ) {
        Log.e("UnityAdsExample", "Initialization failed: $message")
    }


    // Listener for banner events:
    private val bannerListener: BannerView.IListener = object : BannerView.IListener {
        override fun onBannerLoaded(bannerAdView: BannerView) {
            // Called when the banner is loaded.
            Log.v("UnityAdsExample", "onBannerLoaded: " + bannerAdView.placementId)
        }

        override fun onBannerShown(bannerAdView: BannerView?) {
            // Enable the correct button to hide the ad
            if (bannerAdView?.placementId == "Banner_Android" && bannerAdView.isShown) {
                binding.hideTopBanner.isEnabled = true
            }

            if (bannerAdView?.placementId == "banner" && bannerAdView.isShown) {
                binding.hideBottomBanner.isEnabled = true
            }
            Log.v("UnityAdsExample", "onBannerShown: " + bannerAdView?.isShown)

        }

        override fun onBannerFailedToLoad(bannerAdView: BannerView, errorInfo: BannerErrorInfo) {
            Log.e(
                "UnityAdsExample",
                "Unity Ads failed to load banner for " + bannerAdView.placementId + " with error: [" + errorInfo.errorCode + "] " + errorInfo.errorMessage
            )
            // Note that the BannerErrorInfo object can indicate a no fill (refer to the API documentation).
        }

        override fun onBannerClick(bannerAdView: BannerView) {
            // Called when a banner is clicked.
            Log.v("UnityAdsExample", "onBannerClick: " + bannerAdView.placementId)
        }

        override fun onBannerLeftApplication(bannerAdView: BannerView) {
            // Called when the banner links out of the application.
            Log.v("UnityAdsExample", "onBannerLeftApplication: " + bannerAdView.placementId)
        }
    }


    private val loadListener: IUnityAdsLoadListener = object : IUnityAdsLoadListener {
        override fun onUnityAdsAdLoaded(placementId: String) {
            show(
                this@UnityAdsActivity,
                resources.getString(R.string.reward_ad_unit_id),
                UnityAdsShowOptions(),
                showListener
            )
        }

        override fun onUnityAdsFailedToLoad(
            placementId: String,
            error: UnityAds.UnityAdsLoadError,
            message: String
        ) {
            Log.e(
                "UnityAdsExample",
                "Unity Ads failed to load ad for $placementId with error: [$error] $message"
            )
        }
    }

    private val showListener: IUnityAdsShowListener = object : IUnityAdsShowListener {
        override fun onUnityAdsShowFailure(
            placementId: String,
            error: UnityAds.UnityAdsShowError,
            message: String
        ) {
            Log.e(
                "UnityAdsExample",
                "Unity Ads failed to show ad for $placementId with error: [$error] $message"
            )
        }

        override fun onUnityAdsShowStart(placementId: String) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: $placementId")
        }

        override fun onUnityAdsShowClick(placementId: String) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: $placementId")
        }

        override fun onUnityAdsShowComplete(
            placementId: String,
            state: UnityAds.UnityAdsShowCompletionState
        ) {
            if (state == UnityAds.UnityAdsShowCompletionState.COMPLETED) {
                Log.v("UnityAdsExample", "onUnityAdsShowComplete: $placementId")

            } else {
                // Do not reward the user for skipping the ad
            }
            binding.rewardedAd.isEnabled = true
        }
    }


    // Implement a function to load a rewarded ad. The ad will start to show after the ad has been loaded.
    private fun displayRewardedAd() {
        load(resources.getString(R.string.reward_ad_unit_id), loadListener)
    }
}












