package com.k.deeplinkingtesting.admob

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.izooto.iZooto
import com.k.deeplinkingtesting.R


class AdMobActivity : AppCompatActivity() , BottomNavigationView
.OnNavigationItemSelectedListener {

    private var nativeAd: NativeAd? = null
    private var mainLayout : LinearLayout? = null
    private var scrollView : ScrollView? = null
    var bottomNavigationView: BottomNavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_mob)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "News Feed"
        scrollView=findViewById(R.id.scrollView)
        mainLayout = findViewById(R.id.mainLayout)

        iZooto.enablePulse(this,scrollView,mainLayout,true)
      //  bottomNavigationView = findViewById<BottomNavigationView>(com.k.deeplinkingtesting.R.id.bottomAppBar)

      //  bottomNavigationView?.setOnNavigationItemSelectedListener(this)
      //  bottomNavigationView!!.selectedItemId = R.id.person
        // Load the native ad
       // loadNativeAd()
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



    override fun onDestroy() {
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

    override fun onNavigationItemSelected(item: android.view.MenuItem): Boolean {

        return false
    }
}