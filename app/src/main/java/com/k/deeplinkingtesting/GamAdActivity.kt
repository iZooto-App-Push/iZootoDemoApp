package com.k.deeplinkingtesting

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.adsbynimbus.NimbusAdManager
import com.adsbynimbus.NimbusError
import com.adsbynimbus.openrtb.enumerations.Position
import com.adsbynimbus.openrtb.request.Format
import com.adsbynimbus.render.AdController
import com.adsbynimbus.request.NimbusRequest
import com.adsbynimbus.request.NimbusResponse
import com.adsbynimbus.request.withAdMobBanner
import com.adsbynimbus.request.withAdMobInterstitial
import com.adsbynimbus.request.withAdMobRewarded
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import java.util.Arrays

class GamAdActivity : AppCompatActivity() {
    private val nimbusAdManager = NimbusAdManager()

    var ad_container : LinearLayout? =null
    var n_intersitital_ads : Button? =null
    var n_rewarded_ads : Button? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ad_container = findViewById<LinearLayout>(R.id.ad_container)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "GAM Nimbus Network"


        val testDeviceIds = Arrays.asList("54A13F7013E9BF3059E775592737C268")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        /*
           User click the button then open the interstitial ads
         */
        n_intersitital_ads=findViewById(R.id.n_interstitial_ads)
        n_intersitital_ads?.setOnClickListener {
            showInterstitialAds()
        }
        /*
         User click the button then open the rewarded ads
       */
        n_rewarded_ads= findViewById(R.id.n_rewarded_ads)
        n_rewarded_ads?.setOnClickListener {
            showRewardedAds()
        }

      /*
         Banner Ads
       */
        ad_container?.let {
                nimbusAdManager?.showAd(
                    NimbusRequest.forBannerAd(
                        "test_banner",Format.BANNER_320_50,
                        screenPosition = Position.HEADER ).apply {
                        withAdMobBanner(adUnitId = "ca-app-pub-3940256099942544/6300978111")



                    }, it,
                    object : NimbusAdManager.Listener {
                        override fun onAdResponse(nimbusResponse: NimbusResponse) {
                            Log.e("AdResponse","onAdResponse"+nimbusResponse.renderInfo().keys)
                        }

                        override fun onAdRendered(controller: AdController) {
                            Log.e("AdResponse","controller"+controller.volume)

                        }

                        override fun onError(error: NimbusError) {
                            Log.e("AdResponse","error"+error.toString())
                        }
                    })

            }


    }


    /**
     * Rewarded Ads with Nimbus
     *
     *
     */

    private fun showRewardedAds() {
        nimbusAdManager?.showBlockingAd(NimbusRequest.forRewardedVideo("1").apply {
            withAdMobRewarded(adUnitId = "ca-app-pub-3940256099942544/5224354917") // for test ad unit id

        }, this, object : NimbusAdManager.Listener {
            override fun onAdResponse(nimbusResponse: NimbusResponse) {
                Log.e("showInterstitialAds","NimbusResponse"+nimbusResponse.toString())            }

            override fun onAdRendered(controller: AdController) {
                Log.e("showInterstitialAds","AdController"+controller.toString())            }

            override fun onError(error: NimbusError) {
                Log.e("showInterstitialAds","error"+error.toString())            }
        })
    }



    /**
     * Interstitial   Ads with Nimbus
     *
     *
     */
    private fun showInterstitialAds() {

        nimbusAdManager?.showBlockingAd(NimbusRequest.forInterstitialAd("1").apply {
            withAdMobInterstitial(adUnitId = "ca-app-pub-9298860897894361/8470985254")

        }, this, object : NimbusAdManager.Listener {
            override fun onAdResponse(nimbusResponse: NimbusResponse) {
                Log.e("showInterstitialAds","NimbusResponse"+nimbusResponse.toString())            }

            override fun onAdRendered(controller: AdController) {
                Log.e("showInterstitialAds","AdController"+controller.toString())            }

            override fun onError(error: NimbusError) {
                Log.e("showInterstitialAds","error"+error.toString())            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed() // Correctly handles back stack
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    }








