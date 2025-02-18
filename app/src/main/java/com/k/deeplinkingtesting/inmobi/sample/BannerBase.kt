package com.k.deeplinkingtesting.inmobi.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiInterstitial
import com.inmobi.ads.listeners.InterstitialAdEventListener
import com.inmobi.sdk.InMobiSdk
import com.inmobi.sdk.InMobiSdk.IM_GDPR_CONSENT_AVAILABLE
import com.inmobi.sdk.InMobiSdk.init
import com.inmobi.sdk.InMobiSdk.setLogLevel
import com.inmobi.sdk.SdkInitializationListener
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.inmobi.PlacementId
import com.k.deeplinkingtesting.inmobi.PlacementId.Companion.YOUR_PLACEMENT_ID_INTERSTITIAL
import com.k.deeplinkingtesting.inmobi.nativesad.sample.NativeAdsActivity
import org.json.JSONException
import org.json.JSONObject

class BannerBase : AppCompatActivity() {

    private var bannerIn: Button? = null
    private var bannerXMLIn: Button? = null
    private var mInterstitialAd: InMobiInterstitial? = null
    private var mLoadAdButton: Button? = null
    private var mShowAdButton: Button? = null
    private var interstitial: Button? = null
    private var rewarded: Button? = null
    private var native: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLogLevel(InMobiSdk.LogLevel.DEBUG)
        val consent = JSONObject()
        try {
            // Provide correct consent value to sdk which is obtained by User
            consent.put(IM_GDPR_CONSENT_AVAILABLE, true)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        init(
            this, resources.getString(R.string.inMobiId), consent,
            object : SdkInitializationListener {
                override fun onInitializationComplete(error: Error?) {
                    if (error == null) {
                        Log.d(TAG, "InMobi SDK Initialization Success")
                        sdkInitSuccess()
                        interstitial?.isEnabled = true
                        rewarded?.isEnabled = true

                        interstitial?.setOnClickListener {
                            rewarded?.isEnabled = false
                            adjustViewChild()
                            mLoadAdButton?.setOnClickListener {
                                if (mInterstitialAd != null) {
                                    mInterstitialAd = null
                                    setupInterstitial(YOUR_PLACEMENT_ID_INTERSTITIAL)
                                }else{
                                    setupInterstitial(YOUR_PLACEMENT_ID_INTERSTITIAL)
                                }
                            }
                        }
                        rewarded?.setOnClickListener {
                            adjustViewChild()
                            interstitial?.isEnabled = false
                            mLoadAdButton?.setOnClickListener {
                                if (mInterstitialAd != null) {
                                    mInterstitialAd = null
                                    setupInterstitial(PlacementId.YOUR_PLACEMENT_ID_REWARDED)
                                }else {
                                    setupInterstitial(PlacementId.YOUR_PLACEMENT_ID_REWARDED)
                                }
                            }
                        }


                    } else {
                        Log.e(TAG, "InMobi SDK Initialization failed: " + error.message)
                        sdkInitFailed()
                    }
                }
            })


        setContentView(R.layout.banner_base)
        bannerXMLIn = findViewById<View>(R.id.xmlSample) as Button
        bannerIn = findViewById<View>(R.id.bannerIn) as Button
        mLoadAdButton = findViewById<View>(R.id.button_load_ad) as Button
        mShowAdButton = findViewById<View>(R.id.button_show_ad) as Button
        interstitial = findViewById<View>(R.id.interstitialIn) as Button
        rewarded = findViewById<View>(R.id.rewardedIn) as Button
        native = findViewById<View>(R.id.button_native_ad) as Button
        mLoadAdButton?.isEnabled = false
        mShowAdButton?.isEnabled = false
        bannerIn?.isEnabled = false
        bannerXMLIn?.isEnabled = false
        interstitial?.isEnabled = false
        rewarded?.isEnabled = false
        native?.isEnabled = false
        mShowAdButton?.setOnClickListener { showAd() }
        native?.setOnClickListener { nativeAd() }
    }

    private fun nativeAd() {
        startActivity(Intent(this, NativeAdsActivity::class.java))
    }

    private fun sdkInitSuccess() {
        bannerXMLIn!!.setOnClickListener {
            startActivity(
                Intent(
                    this@BannerBase,
                    BannerXmlActivity::class.java
                )
            )
        }
        bannerIn!!.setOnClickListener {
            startActivity(
                Intent(
                    this@BannerBase,
                    BannerAdsActivity::class.java
                )
            )
        }
        bannerIn?.isEnabled = true
        bannerXMLIn?.isEnabled = true
        native?.isEnabled = true
    }

    private fun sdkInitFailed() {
        bannerXMLIn!!.setOnClickListener {
            Toast.makeText(
                this@BannerBase, "InMobi SDK is not initialized." +
                        "Check logs for more information", Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        private val TAG: String = BannerBase::class.java.simpleName
    }


    public override fun onResume() {
        super.onResume()
    }

    private fun adjustViewChild() {
        mLoadAdButton!!.isEnabled = true
        mShowAdButton!!.isEnabled = false
    }

    private fun showAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show()
        }
    }

    private fun setupInterstitial(placementId: Long) {
        try {
            mInterstitialAd = InMobiInterstitial(
                this@BannerBase, placementId,
                object : InterstitialAdEventListener() {
                    override fun onAdLoadSucceeded(
                        inMobiInterstitial: InMobiInterstitial,
                        adMetaInfo: AdMetaInfo
                    ) {
                        Log.d(TAG, "onAdLoadSuccessful with bid " + adMetaInfo.bid)
                        if (inMobiInterstitial.isReady()) {
                            if (mShowAdButton != null) {
                                mShowAdButton!!.isEnabled = true
                                mLoadAdButton!!.isEnabled = false
                            }
                        } else {
                            Log.d(TAG, "onAdLoadSuccessful inMobiInterstitial not ready")
                        }
                    }

                    override fun onAdLoadFailed(
                        inMobiInterstitial: InMobiInterstitial,
                        inMobiAdRequestStatus: InMobiAdRequestStatus
                    ) {
                        Log.d(
                            TAG, "Unable to load interstitial ad (error message: " +
                                    inMobiAdRequestStatus.message
                        )
                    }

                    override fun onAdFetchSuccessful(
                        inMobiInterstitial: InMobiInterstitial,
                        adMetaInfo: AdMetaInfo
                    ) {
                        Log.d(TAG, "onAdFetchSuccessful with bid " + adMetaInfo.bid)
                    }

                    override fun onAdClicked(
                        inMobiInterstitial: InMobiInterstitial,
                        map: Map<Any, Any>
                    ) {
                        Log.d(TAG, "onAdClicked " + map.size)
                    }

                    override fun onAdWillDisplay(inMobiInterstitial: InMobiInterstitial) {
                        Log.d(TAG, "onAdWillDisplay")
                    }

                    override fun onAdDisplayed(
                        inMobiInterstitial: InMobiInterstitial,
                        adMetaInfo: AdMetaInfo
                    ) {
                        Log.d(TAG, "onAdDisplayed")
                    }

                    override fun onAdDisplayFailed(inMobiInterstitial: InMobiInterstitial) {
                        Log.d(TAG, "onAdDisplayFailed")
                    }

                    override fun onAdDismissed(inMobiInterstitial: InMobiInterstitial) {
                        Log.d(TAG, "onAdDismissed")
                        mLoadAdButton!!.isEnabled = true
                        mShowAdButton!!.isEnabled = false
                        interstitial!!.isEnabled = true
                        rewarded!!.isEnabled = true
                    }

                    override fun onUserLeftApplication(inMobiInterstitial: InMobiInterstitial) {
                        Log.d(TAG, "onUserWillLeaveApplication")
                    }

                    override fun onRewardsUnlocked(
                        inMobiInterstitial: InMobiInterstitial,
                        map: Map<Any, Any>
                    ) {
                        Log.d(TAG, "onRewardsUnlocked $map")
                    }

                    override fun onAdImpression(inMobiInterstitial: InMobiInterstitial) {
                        mLoadAdButton!!.isEnabled = true
                        Log.d(TAG, "onAdImpression")
                    }
                })
            mInterstitialAd?.load()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
