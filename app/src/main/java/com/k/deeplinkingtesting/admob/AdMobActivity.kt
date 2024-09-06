package com.k.deeplinkingtesting.admob

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.izooto.iZooto
import com.k.deeplinkingtesting.R

class AdMobActivity : AppCompatActivity() {

    private var nativeAd: NativeAd? = null
    private lateinit var native_ad_view : NativeAdView
    private var mainLayout : LinearLayout? = null
    private var scrollView : ScrollView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_mob)
        native_ad_view=findViewById(R.id.native_ad_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "News Feed"
        scrollView=findViewById(R.id.scrollView)
        mainLayout = findViewById(R.id.mainLayout)

        iZooto.enablePulse(this,scrollView,mainLayout,true)
        // Load the native ad
        loadNativeAd()
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
                    populateNativeAdView(ad, native_ad_view)
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
    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        //adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.headlineView=adView.findViewById(R.id.ad_headline)
        if (nativeAd.icon != null) {
            (adView.iconView as ImageView).setImageDrawable(nativeAd.icon?.drawable)
        } else {
            adView.iconView?.visibility = View.GONE
        }
       // (adView.bodyView as TextView).text = nativeAd.body
        (adView.headlineView as TextView).text = nativeAd.headline
        Log.e("Native Ads",nativeAd.advertiser.toString())
        Log.e("Native Ads",nativeAd.price.toString())
        Log.e("Native Ads",nativeAd.store.toString())
        Log.e("Native Ads",nativeAd.adChoicesInfo?.text.toString())
        Log.e("Native Ads",nativeAd.mediaContent.toString())
        Log.e("Native Ads",nativeAd.starRating.toString())
        Log.e("Native Ads",nativeAd.responseInfo?.responseId.toString())


        val adAttributionView = adView.findViewById<TextView>(R.id.ad_attribution)
        if (nativeAd.advertiser != null) {
            adAttributionView.text = "Ad from ${nativeAd.advertiser}"
        } else {
            adAttributionView.text = "Sponsored" // Default attribution if advertiser is null
        }
        adView.setNativeAd(nativeAd)
    }
    override fun onDestroy() {
        nativeAd?.destroy()
        super.onDestroy()
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