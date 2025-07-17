package com.k.deeplinkingtesting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adsbynimbus.NimbusAdManager
import com.adsbynimbus.NimbusError
import com.adsbynimbus.render.AdController
import com.adsbynimbus.render.VungleRenderer
import com.adsbynimbus.request.NimbusRequest
import com.adsbynimbus.request.NimbusResponse
import com.facebook.ads.NativeAdLayout
import com.vungle.ads.NativeAd

class AdMoreActivity : AppCompatActivity() {
    private val nimbusAdManager = NimbusAdManager()
    lateinit var requestPositionName: String
    var unityAdsNetwork : Button? = null
    var liftoff_ads_network : Button? = null
    private var nativeAd: NativeAd? = null

    val closeButtonDelayInSeconds: Int = 15


    //molacoAdNetwork


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_more)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Add More Ads Network"

      /* Unity Ads Video Integration....  */
        unityAdsNetwork=findViewById<Button>(R.id.unityAdsNetwork)
        unityAdsNetwork?.setOnClickListener {
            loadVideoAdsforUnity()

        }
        nativeAd = NativeAd(this, "NIMBUS_LIFTOFF_NATIVE-4569387").apply {
            adOptionsPosition = NativeAd.TOP_LEFT
        }.apply {
           // adListener = this@AdMoreActivity
            load()
        }
        liftoff_ads_network=findViewById<Button>(R.id.liftoff_ads_network)
        liftoff_ads_network?.setOnClickListener {
            loadNativeAdsforLiftOff()

        }


        /* molaco ads integration .... */


/*
        VungleRenderer.delegate = object : VungleRenderer.Delegate {
            override fun customViewForRendering(container: ViewGroup, nativeAd: NativeAd): View = LayoutInflater.from(container.context)
                .inflate(R.layout.native_ad, container, false).apply {
                    // you need to create a View that includes the fields necessary to display the native ad

                    // configure your view using the values received in the nativeAd parameter

                    // after your View is setup, call registerViewForInteraction passing the NativeAdLayout
                    // for your View and all the other views that handle interaction
                    val nativeLayout = findViewById<NativeAdLayout>(R.id.native_ad_layout)
                    nativeAd.registerViewForInteraction(
                        nativeLayout, /* your other views */
                    )
                }
        }

 */




    }

    private fun loadNativeAdsforLiftOff() {
        Log.e("Liftoff","Start")

        VungleRenderer.delegate = object : VungleRenderer.Delegate {
            override fun customViewForRendering(container: ViewGroup, nativeAd: NativeAd): View {
                val adView = LayoutInflater.from(container.context)
                    .inflate(R.layout.lift_of_native_ads, container, false)

                // Bind UI elements
                val adTitle = adView.findViewById<TextView>(R.id.lbAdTitle)
                val adBody = adView.findViewById<TextView>(R.id.lbAdBody)
              //  val adIcon = adView.findViewById<ImageView>(R.id.ad_app_icon)
             //   val adCallToAction = adView.findViewById<Button>(R.id.ad_call_to_action)
             //   val nativeAdLayout = adView.findViewById<NativeAdLayout>(R.id.native_ad_container)
                Log.e("Liftoff","Start"+nativeAd.getAdTitle())

                // Set ad data
                adTitle.text = nativeAd.getAdTitle()
                adBody.text = nativeAd.getAdBodyText()
             //   adCallToAction.text = nativeAd.getAdBodyText()

                // Load and set icon
//                val icon = nativeAd.adIcon
//                if (icon != null && icon.drawable != null) {
//                    adIcon.setImageDrawable(icon.drawable)
//                }

                // Register views for interaction
//                nativeAd.registerViewForInteraction(
//                    nativeAdLayout,
//                    listOf(adTitle, adBody, adCallToAction))

                return adView
            }
        }


    }

    private fun loadVideoAdsforUnity() {
        val rewardedRequest = NimbusRequest.forRewardedVideo("requestPositionName")
        nimbusAdManager.showRewardedAd(rewardedRequest, closeButtonDelayInSeconds, this,
            object : NimbusAdManager.Listener {

                override fun onAdResponse(nimbusResponse: NimbusResponse) {
                    Log.e("AdResponse Unity","onAdResponse"+nimbusResponse.toString())
                }

                override fun onAdRendered(controller: AdController) {
                    Log.e("AdResponse Unity","onAdResponse"+controller.toString())
                }

                override fun onError(error: NimbusError) {
                    Log.e("AdResponse Unity","onAdResponse"+error.toString())


                }
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