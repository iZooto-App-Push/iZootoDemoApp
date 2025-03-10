package com.k.deeplinkingtesting.jioads

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jio.jioads.adinterfaces.JioAdError
import com.jio.jioads.adinterfaces.JioAdListener
import com.jio.jioads.adinterfaces.JioAdView
import com.jio.jioads.adinterfaces.JioAds
import com.k.deeplinkingtesting.R

@Suppress("DEPRECATION")
class JioAdsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private var adspotId: String? = null
    private var packu: String? = null
    private var jioAdViewInterstitial: JioAdView? = null
    private var isAdLoaded = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_jio_ads)


        if (!checkPermission()) {
            requestPermission()

        } else {
            Log.d("merc", "permissions are already granted")
        }

//

        findViewById<Button>(R.id.banner).setOnClickListener {
            val startAd = Intent(this, JioBannerAdsActivity::class.java)
            startActivity(startAd)
        }


        findViewById<Button>(R.id.instream).setOnClickListener {
            val startAd = Intent(this, JioAdsInStreamActivity::class.java)
//
            startActivity(startAd)
//

        }
        findViewById<Button>(R.id.jio_interstitial).setOnClickListener {
            showInterstitialAd()


        }
        findViewById<Button>(R.id.jio_rewarded).setOnClickListener {


        }
        loadInterstitialAd()


    }


    private fun loadInterstitialAd() {
        if (jioAdViewInterstitial == null) {
            jioAdViewInterstitial = JioAdView(this, "an27mz1i", JioAdView.AD_TYPE.INTERSTITIAL)
            jioAdViewInterstitial?.setAdListener(object : JioAdListener() {
                override fun onAdFailedToLoad(jioAdView: JioAdView?, jioAdError: JioAdError?) {
                    Log.e("JioAds", "Ad Failed to Load: ${jioAdError}")
                    isAdLoaded = false
                }

                override fun onAdPrepared(jioAdView: JioAdView?) {
                    Log.d("JioAds", "Ad is prepared and ready")
                    isAdLoaded = true
                }

                override fun onAdClosed(
                    jioAdView: JioAdView?,
                    isVideoCompleted: Boolean,
                    isEligibleForReward: Boolean
                ) {
                    Log.d("JioAds", "Ad Closed, reloading..."+isEligibleForReward)
                    isAdLoaded = false
                    jioAdViewInterstitial?.cacheAd() // Reload for next time
                }

                override fun onAdRender(jioAdView: JioAdView?) {
                    Log.d("JioAds", "Ad is rendered on screen")
                }

                override fun onAdMediaEnd(jioAdView: JioAdView?) {
                    Log.d("JioAds", "Ad media finished playing")
                }
            })

            jioAdViewInterstitial?.cacheAd() // Start loading the ad
        }
    }

    // Show the ad only if it’s loaded
    private fun showInterstitialAd() {
        if (isAdLoaded) {
            jioAdViewInterstitial?.loadAd()
        } else {
            Toast.makeText(this, "Ad not ready yet, please wait...", Toast.LENGTH_SHORT).show()
            loadInterstitialAd() // Try to load again
        }
    }
    override fun onBackPressed() {

        super.onBackPressed()
    }

    override fun onDestroy() {
        JioAds.getInstance().release()
        super.onDestroy()
    }

    private val PERMISSION_REQUEST_CODE = 100
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val result2 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_PHONE_STATE
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
    }

    private fun showToast(message: String) {
        runOnUiThread(Runnable {
            Toast.makeText(this, message, Toast.LENGTH_SHORT)
        })
    }
}

