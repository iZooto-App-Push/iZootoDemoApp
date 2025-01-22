package com.k.deeplinkingtesting

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.k.deeplinkingtesting.databinding.ActivityMainBinding
import com.k.deeplinkingtesting.databinding.ActivityYadexAdsBinding
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import kotlin.math.roundToInt
import kotlin.random.Random

class YadexAdsActivity : AppCompatActivity(R.layout.activity_yadex_ads) {
    private var bannerAd: BannerAdView? = null
    private lateinit var adFormatManager: AdFormatManager
    private lateinit var adsContainerList: ArrayList<FrameLayout>
    private lateinit var binding: ActivityYadexAdsBinding

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYadexAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "YanDex Ads "
      //  supportActionBar?.subtitle = resources.getString(R.string.welcome_to_yandex_demo_app)


        // Add a listener to the subtitle
        val decorView = window.decorView // Get the root view
        decorView.post {
            try {
                // Access the ActionBar container
                val actionBarViewGroup = findActionBarView(window.decorView)

                if (actionBarViewGroup != null) {
                    // Find the subtitle TextView
                    val subtitleTextView =
                        findTextViewWithText(actionBarViewGroup, supportActionBar?.subtitle)
                    subtitleTextView?.setOnClickListener {
                        // Handle subtitle click
                        Toast.makeText(this, "clicked!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        adFormatManager = AdFormatManager(this)
        adsContainerList = ArrayList()
        adsContainerList.add(binding.smallContainerView)
        adsContainerList.add(binding.mediumContainerView)
        adsContainerList.add(binding.bannerAdViewContainer)
        adsContainerList.add(binding.manualContainerView)
        adsContainerList.add(binding.mrecAdViewContainer)
      //  loadBannerAds()
       // applyWaveAnimation(binding.imageView)


        binding.bannerAd.setOnClickListener {
            adsContainerList[0].visibility = View.GONE
            adsContainerList[1].visibility = View.GONE
            adsContainerList[2].visibility = View.VISIBLE
            adsContainerList[3].visibility = View.GONE
            adsContainerList[4].visibility = View.GONE
            if (bannerAd != null) {
                bannerAd?.destroy()
                bannerAd?.removeAllViews()
            }
            val random = Random
            val number = random.nextInt(1, 3)
            bannerAd = if (number == 2) {
                adFormatManager.loadBannerAd(
                    this@YadexAdsActivity,
                    adaptiveInlineBannerSize(
                        this@YadexAdsActivity,
                        binding.bannerAdViewContainer
                    ),
                    binding.banner,
                    binding.textError,
                    binding.progressBar
                )
            } else {
                adFormatManager.loadBannerAd(
                    this@YadexAdsActivity,
                    adaptiveStickyBannerSize(this@YadexAdsActivity, binding.bannerAdViewContainer),
                    binding.banner,
                    binding.textError,
                    binding.progressBar
                )
            }


        }
        binding.interstitialId.setOnClickListener{
            adFormatManager.loadInterstitialAd(
                binding.interstitialId,
                binding.textError,
                binding.progressBar,
                adsContainerList
            )
        }
    }

    private fun loadBannerAds() {
        adsContainerList[0].visibility = View.GONE
        adsContainerList[1].visibility = View.GONE
        adsContainerList[2].visibility = View.VISIBLE
        adsContainerList[3].visibility = View.GONE
        adsContainerList[4].visibility = View.GONE
        if (bannerAd != null) {
            bannerAd?.destroy()
            bannerAd?.removeAllViews()
        }
        val random = Random
        val number = random.nextInt(1, 3)
        bannerAd = if (number == 2) {
            adFormatManager.loadBannerAd(
                this@YadexAdsActivity,
                adaptiveInlineBannerSize(
                    this@YadexAdsActivity,
                    binding.bannerAdViewContainer
                ),
                binding.banner,
                binding.textError,
                binding.progressBar
            )
        } else {
            adFormatManager.loadBannerAd(
                this@YadexAdsActivity,
                adaptiveStickyBannerSize(this@YadexAdsActivity, binding.bannerAdViewContainer),
                binding.banner,
                binding.textError,
                binding.progressBar
            )
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
//        adFormatManager.loadInterstitialAd(
//            binding.interstitialId,
//            binding.textError,
//            binding.progressBar,
//            adsContainerList
//        )
        adFormatManager.loadRewardedAd(
            binding.rewardedAd,
            binding.textError,
            binding.progressBar,
            adsContainerList
        )

        binding.smallNativeAd.setOnClickListener {
           // binding.progressBar.loadingShow()
            adsContainerList[0].visibility = View.VISIBLE
            adsContainerList[1].visibility = View.GONE
            adsContainerList[2].visibility = View.GONE
            adsContainerList[3].visibility = View.GONE
            adFormatManager.loadNativeAd(
                binding.smallContainerView,
                binding.textError, binding.progressBar,
                false,
                resources.getString(R.string.native_small_ad_unit)
            )
        }
        binding.mediumNativeAd.setOnClickListener {
           // binding.progressBar.loadingShow()
            adsContainerList[0].visibility = View.GONE
            adsContainerList[1].visibility = View.VISIBLE
            adsContainerList[2].visibility = View.GONE
            adsContainerList[3].visibility = View.GONE
            adFormatManager.loadNativeAd(
                binding.mediumContainerView,
                binding.textError,
                binding.progressBar,
                false,
                resources.getString(R.string.native_medium_ad_unit)
            )
        }

        binding.manualNativeAd.setOnClickListener {
          //  binding.progressBar.loadingShow()
            adsContainerList[0].visibility = View.GONE
            adsContainerList[1].visibility = View.GONE
            adsContainerList[2].visibility = View.GONE
            adsContainerList[3].visibility = View.VISIBLE

            adFormatManager.loadNativeAd(
                binding.manualContainerView,
                binding.textError,
                binding.progressBar,
                true,
                resources.getString(R.string.native_manual_ad_unit)
            )

        }

    }

    // Helper method to find the ActionBar's view group
    private fun findActionBarView(view: View): ViewGroup? {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                if (child.javaClass.simpleName.contains("ActionBar", ignoreCase = true)) {
                    return child as? ViewGroup
                } else {
                    val result = findActionBarView(child)
                    if (result != null) return result
                }
            }
        }
        return null
    }

    // Helper function to find TextView with specific text
    private fun findTextViewWithText(parent: ViewGroup, text: CharSequence?): TextView? {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is TextView && child.text == text) {
                return child
            } else if (child is ViewGroup) {
                val result = findTextViewWithText(child, text)
                if (result != null) return result
            }
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        adFormatManager.onDestroy()
    }
    fun adaptiveInlineBannerSize(context: Context, binding: FrameLayout): BannerAdSize
    {
        val screenHeight = context.resources.displayMetrics.run { heightPixels / density }.roundToInt()
        // Calculate the width of the ad, taking into account the padding in the ad container.
        var adWidthPixels = binding.width
        if (adWidthPixels == 0) {
            // If the ad hasn't been laid out, default to the full screen width
            adWidthPixels = context.resources.displayMetrics.widthPixels
        }
        val adWidth = (adWidthPixels / context.resources.displayMetrics.density).roundToInt()
        // Determine the maximum allowable ad height. The current value is given as an example.
        val maxAdHeight = screenHeight / 2

        return BannerAdSize.inlineSize(context, adWidth, maxAdHeight)
    }


    fun adaptiveStickyBannerSize(context: Context, binding: FrameLayout): BannerAdSize{
        // Calculate the width of the ad, taking into account the padding in the ad container.
        var adWidthPixels = binding.width
        if (adWidthPixels == 0) {
            // If the ad hasn't been laid out, default to the full screen width
            adWidthPixels = context.resources.displayMetrics.widthPixels
        }
        val adWidth = (adWidthPixels / context.resources.displayMetrics.density).roundToInt()

        return BannerAdSize.stickySize(context, adWidth)
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
}