package com.k.deeplinkingtesting.jioads

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jio.jioads.adinterfaces.JioAdError
import com.jio.jioads.adinterfaces.JioAdListener
import com.jio.jioads.adinterfaces.JioAdView
import com.k.deeplinkingtesting.R

class JioAdsInStreamActivity: AppCompatActivity() {
    private val TAG: String = JioAdsInStreamActivity::class.java.simpleName
    private var instreamVideoAdView: JioAdView? = null
    private lateinit var instreamAdContainer: FrameLayout
    private var adspot: String? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instream)

       // adspot = "z3fpiwv8"
        adspot ="ezso6c1h" // video
        //adspot = "an27mz1i" // interstitials

        startCacheAd()

    }

    override fun onDestroy() {
        instreamVideoAdView?.onDestroy()
        super.onDestroy()
    }

    private fun startCacheAd() {

        instreamAdContainer = findViewById(R.id.nativeAdContainerin)
        instreamVideoAdView =
            JioAdView(
                this,
                adspot.toString(),
                JioAdView.AD_TYPE.INSTREAM_VIDEO
            )
        instreamVideoAdView!!.setAdListener(object : JioAdListener() {
            override fun onAdClosed(
                jioAdView: JioAdView?,
                isVideoCompleted: Boolean,
                isEligibleForReward: Boolean
            ) {
                Log.i(
                    TAG,
                    "Inside On adClosed,isCompleted $isVideoCompleted and isRewardEligible: $isEligibleForReward"
                )
                showToast("Ad is Closed..")
            }

            override fun onAdFailedToLoad(jioAdView: JioAdView?, jioAdError: JioAdError?) {
                Log.i(
                    TAG,
                    "Inside onFailedToLoad Title: ${jioAdError!!.getErrorTitle()} and code : ${jioAdError.getErrorCode()}"
                )
                showToast("Ad Failed to load")
            }

            override fun onAdMediaEnd(jioAdView: JioAdView?) {
                Log.i(TAG, "Inside On MediaEnd")
                showToast("Inside On MediaEnd")

            }

            override fun onAdPrepared(jioAdView: JioAdView?) {

                Log.i(TAG, "Inside onAdPrepared")
                showToast("Ad is prepared")

                startShowAd()

            }

            override fun onAdRender(jioAdView: JioAdView?) {
                Log.i(TAG, "Inside on AdClicked")
                showToast("Ad Clicked")
            }

            override fun onAdRefresh(jioAdView: JioAdView?) {
                Log.i(TAG, "Inside onAdRefresh")
                super.onAdRefresh(jioAdView)
                showToast("Ad Refresh")
            }
        })


        instreamVideoAdView?.cacheAd()
    }

    private fun startShowAd() {
        if (instreamVideoAdView!!.getAdState() == JioAdView.AdState.PREPARED) {

            instreamAdContainer.removeAllViews()
            instreamAdContainer.addView(instreamVideoAdView?.getAdView())
            instreamAdContainer.visibility = View.VISIBLE
            showToast("Publisher called load ad")

            instreamVideoAdView?.loadAd()
        }
    }

    private fun showToast(message: String) {
        runOnUiThread(Runnable {
            Toast.makeText(this, message, Toast.LENGTH_SHORT)
        })
    }


}