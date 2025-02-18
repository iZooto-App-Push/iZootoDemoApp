package com.k.deeplinkingtesting.inmobi.nativesad.sample.singlestrand

import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiNative
import com.inmobi.ads.listeners.NativeAdEventListener
import com.inmobi.ads.listeners.VideoEventListener
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.inmobi.PlacementId
import com.k.deeplinkingtesting.inmobi.nativesad.utility.SwipeRefreshLayoutWrapper
import com.squareup.picasso.Picasso

/**
 * Demonstrates the use of InMobiNative to place a single ad.
 *
 *
 * Note: Swipe to refresh ads.
 */
class SingleNativeAdFragment : Fragment() {
    private var mContainer: ViewGroup? = null
    private var mInMobiNative: InMobiNative? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_custom_integration, container, false)
        mContainer = view.findViewById<View>(R.id.container) as ViewGroup

        val swipeRefreshLayout = SwipeRefreshLayoutWrapper.getInstance(
            requireActivity(),
            object : SwipeRefreshLayoutWrapper.Listener {
                override fun canChildScrollUp(): Boolean {
                    return false
                }

                override fun onRefresh() {
                    reloadAd()
                }
            })
        swipeRefreshLayout.addView(mContainer)
        return swipeRefreshLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createStrands()
        Log.d(TAG, "Requesting ad")
        loadAd()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val view = loadAdIntoView(mInMobiNative!!)
        if (view != null) {
            mContainer!!.removeAllViews()
            mContainer!!.addView(view)
        }
    }

    private fun createStrands() {
        mInMobiNative =
            InMobiNative(requireActivity(), PlacementId.YOUR_PLACEMENT_ID_NATIVE, StrandAdListener())
        mInMobiNative!!.setVideoEventListener(object : VideoEventListener() {
            override fun onVideoCompleted(inMobiNative: InMobiNative) {
                super.onVideoCompleted(inMobiNative)
                Log.d(TAG, "Video completed")
            }

            override fun onVideoSkipped(inMobiNative: InMobiNative) {
                super.onVideoSkipped(inMobiNative)
                Log.d(TAG, "Video skipped")
            }

            override fun onAudioStateChanged(inMobiNative: InMobiNative, b: Boolean) {
                super.onAudioStateChanged(inMobiNative, b)
                Log.d(TAG, "Audio state changed")
            }
        })
    }

    override fun onDestroyView() {
        mInMobiNative!!.destroy()
        super.onDestroyView()
    }

    private fun loadAd() {
        mInMobiNative!!.load()
    }

    private fun clearAd() {
        mContainer!!.removeAllViews()
        mInMobiNative!!.destroy()
    }

    private fun reloadAd() {
        clearAd()
        createStrands()
        loadAd()
    }

    @Suppress("deprecation")
    private fun loadAdIntoView(inMobiNative: InMobiNative): View {
        val adView = LayoutInflater.from(activity).inflate(R.layout.layout_ad, null)

        val icon = adView.findViewById<View>(R.id.adIcon) as ImageView
        val title = adView.findViewById<View>(R.id.adTitle) as TextView
        val description = adView.findViewById<View>(R.id.adDescription) as TextView
        val action = adView.findViewById<View>(R.id.adAction) as Button
        val content = adView.findViewById<View>(R.id.adContent) as FrameLayout
        val ratingBar = adView.findViewById<View>(R.id.adRating) as RatingBar

        Picasso.get()
            .load(inMobiNative.adIconUrl)
            .into(icon)
        title.text = inMobiNative.adTitle
        description.text = inMobiNative.adDescription
        action.text = inMobiNative.adCtaText

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        content.addView(
            inMobiNative.getPrimaryViewOfWidth(
                activity,
                content,
                mContainer,
                displayMetrics.widthPixels
            )
        )

        val rating = inMobiNative.adRating
        if (rating != 0f) {
            ratingBar.rating = rating
        }
        ratingBar.visibility =
            if (rating != 0f) View.VISIBLE else View.GONE

        action.setOnClickListener { mInMobiNative!!.reportAdClickAndOpenLandingPage() }

        return adView
    }

    private inner class StrandAdListener : NativeAdEventListener() {
        override fun onAdLoadSucceeded(
            inMobiNative: InMobiNative,
            adMetaInfo: AdMetaInfo
        ) {
            //Pass the old ad view as the first parameter to facilitate view reuse.
            val view = loadAdIntoView(inMobiNative)
            if (view == null) {
                Log.d(TAG, "Could not render Strand!")
            } else {
                mContainer!!.addView(view)
            }
        }

        override fun onAdLoadFailed(
            inMobiNative: InMobiNative,
            inMobiAdRequestStatus: InMobiAdRequestStatus
        ) {
            Log.d(TAG, "Ad Load failed (" + inMobiAdRequestStatus.message + ")")
        }

        override fun onAdFetchSuccessful(inMobiNative: InMobiNative, adMetaInfo: AdMetaInfo) {
            Log.d(TAG, "onAdFetchSuccessful with bid " + adMetaInfo.bid)
        }

        override fun onAdFullScreenDismissed(inMobiNative: InMobiNative) {
        }

        override fun onAdFullScreenWillDisplay(inMobiNative: InMobiNative) {
            Log.d(TAG, "Ad going fullscreen.")
        }

        override fun onAdFullScreenDisplayed(inMobiNative: InMobiNative) {
        }

        override fun onUserWillLeaveApplication(inMobiNative: InMobiNative) {
        }

        override fun onAdClicked(inMobiNative: InMobiNative) {
            Log.d(TAG, "Ad clicked")
        }


        override fun onAdStatusChanged(inMobiNative: InMobiNative) {
        }

        override fun onAdImpression(inMobiNative: InMobiNative) {
        }
    }

    companion object {
        private val TAG: String = SingleNativeAdFragment::class.java.simpleName

        @JvmStatic
        val title: String
            get() = "Custom Placement"
    }
}
