package com.k.deeplinkingtesting.applovin

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.k.deeplinkingtesting.R


class ApplovinAdFormatManager(private val context: Activity) {

    private lateinit var maxAdViewBanner: MaxAdView

    fun loadBannerAd(

        bannerContainer: FrameLayout
    ) {
        try {

                maxAdViewBanner = MaxAdView(context.resources.getString(R.string.aap_lovin_banner_ad_unit), context)
                maxAdViewBanner.setListener(object : MaxAdViewAdListener {
                    override fun onAdLoaded(p0: MaxAd) {
                        Log.i("Applovin", "Ad Loaded")
                    }

                    override fun onAdDisplayed(p0: MaxAd) {
                        Log.i("Applovin", "onAdDisplayed")

                    }

                    override fun onAdHidden(p0: MaxAd) {
                        Log.i("Applovin", "onAdHidden")
                    }

                    override fun onAdClicked(p0: MaxAd) {
                        Log.i("Applovin", "onAdClicked")
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onAdLoadFailed(p0: String, p1: MaxError) {
                        Log.i("Applovin", "onAdLoadFailed")

                    }

                    @SuppressLint("SetTextI18n")
                    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                        Log.i("Applovin", "onAdDisplayFailed")

                    }

                    override fun onAdExpanded(p0: MaxAd) {
                        Log.i("Applovin", "onAdExpanded")
                    }

                    override fun onAdCollapsed(p0: MaxAd) {
                        Log.i("Applovin", "onAdCollapsed")
                    }
                })

                // banner setup
                val width = ViewGroup.LayoutParams.MATCH_PARENT
            val heightDp = 90

            maxAdViewBanner.setLayoutParams(FrameLayout.LayoutParams(width, heightDp, Gravity.BOTTOM))
                bannerContainer.addView(maxAdViewBanner)
                maxAdViewBanner.loadAd()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }














}