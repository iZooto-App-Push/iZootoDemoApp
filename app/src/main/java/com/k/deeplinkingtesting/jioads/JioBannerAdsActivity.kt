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
import com.jio.jioads.utils.Constants.DynamicDisplaySize
import com.k.deeplinkingtesting.R

class JioBannerAdsActivity: AppCompatActivity() {
    private val TAG: String = JioBannerAdsActivity::class.java.simpleName
    private var dynamicDispalyAdView: JioAdView? = null
    private lateinit var bannerAdContainer: FrameLayout
    private var adspot: String? = null
    private val sizeList: MutableList<DynamicDisplaySize> = ArrayList()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)

       // adspot = "bzpf5akn"
        adspot = "ityd8071"

        startCacheAd()

    }

    override fun onDestroy() {
        dynamicDispalyAdView?.onDestroy()
        super.onDestroy()
    }

    private fun startCacheAd() {

        bannerAdContainer = findViewById(R.id.nativeAdContainerin)
        dynamicDispalyAdView =
            JioAdView(
                this,
                adspot.toString(),
                JioAdView.AD_TYPE.DYNAMIC_DISPLAY
            )
        dynamicDispalyAdView!!.setAdListener(object : JioAdListener() {
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

        sizeList.add(DynamicDisplaySize.SIZE_300x250)
        dynamicDispalyAdView?.setDisplayAdSize(sizeList) // set size

        dynamicDispalyAdView?.cacheAd()
    }

    private fun startShowAd() {
        if (dynamicDispalyAdView!!.getAdState() == JioAdView.AdState.PREPARED) {

            bannerAdContainer.removeAllViews()
            bannerAdContainer.addView(dynamicDispalyAdView?.getAdView())
            bannerAdContainer.visibility = View.VISIBLE
            showToast("Publisher called load ad")

            dynamicDispalyAdView?.loadAd()
        }
    }

    private fun showToast(message: String) {
        runOnUiThread(Runnable {
            Toast.makeText(this, message, Toast.LENGTH_SHORT)
        })
    }


}