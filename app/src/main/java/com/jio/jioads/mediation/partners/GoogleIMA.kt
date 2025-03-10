package com.jio.jioads.mediation.partners

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.FrameLayout
import com.jio.jioads.adinterfaces.JioAdError
import com.jio.jioads.adinterfaces.JioAdPartner
import com.jio.jioads.adinterfaces.JioAdView
import com.jio.jioads.utils.Constants
import com.jio.jioads.utils.Constants.MediationJsonKeys
import com.jio.jioads.utils.Constants.MediationLocalExtraKeys
import com.jioads.mediation.partners.JioMediationAd
import com.jioads.mediation.partners.JioMediationListener
import com.jioads.mediation.partners.videoutils.JioMediationVideoController

//Version: 3.31.0
class GoogleIMA : JioMediationAd() {
    private var mAdUIContainer: ViewGroup? = null
    private var mJioVideoPlayerController: JioMediationVideoController? = null
    var activity: Context? = null
    var jioAdView: JioAdView? = null
    var adTagUrl: String? = null
    private var jioAdPartner: JioAdPartner? = null
    private val TAG = "merc"
    //step 5 IMA ad mediation : here tag will be added

    override fun loadAd(
        jioAdView: JioAdView,
        customListener: JioMediationListener,
        localExtras: Map<String, Any>,
        serverExtras: Map<String, Any>,
    ) {
        try {
            Log.i(TAG, "loadAd() of Google IMA")
            activity = jioAdView.getContext() ?: return
            val linLayout = FrameLayout(activity!!)
            mAdUIContainer = linLayout
            if (localExtras.isNotEmpty()) {
                if (localExtras.containsKey(MediationLocalExtraKeys.AD_VIEW)) {
                    this.jioAdView = localExtras[MediationLocalExtraKeys.AD_VIEW] as? JioAdView
                } else {
                    this.jioAdView = jioAdView
                }
                if (localExtras.containsKey(MediationLocalExtraKeys.JIO_AD_PARTNER)) {
                    jioAdPartner =
                        localExtras[MediationLocalExtraKeys.JIO_AD_PARTNER] as JioAdPartner?
                    Log.i(TAG, "AdPartnerName " + "GoogleIMA")
                    jioAdPartner?.partnerName = "GoogleIMA"
                    Log.i(TAG, "AdPartnerSDKVersion " + "3.29.0")
                    jioAdPartner?.partnerSDKVersion = "3.29.0"
                }
                if (mAdUIContainer == null && customListener != null) {
                    customListener.onAdFailed(
                        JioAdError.JioAdErrorType.ERROR_MANDATORY_PARAM_MISSING.toString(),
                        "GoogleIMA Mandatory parameters missing"
                    )
                    return
                }
            }
            adTagUrl = if (serverExtras.isNotEmpty()) {
                if (extrasAreValid(serverExtras)) {
                    serverExtras[MediationJsonKeys.AD_TAG_URL].toString()
                        .trim { it <= ' ' }
                } else {
                    customListener.onAdFailed(
                        JioAdError.JioAdErrorType.ERROR_MANDATORY_PARAM_MISSING.toString(),
                        "GoogleIMA Mandatory parameters missing"
                    )
                    return
                }
            } else {
                customListener.onAdFailed(
                    JioAdError.JioAdErrorType.ERROR_MANDATORY_PARAM_MISSING.toString(),
                    "GoogleIMA serverExtras missing"
                )
                return
            }
            //step 6 IMA ad mediation :  init JioMediationVideoController and call prepared()
            if (!TextUtils.isEmpty(adTagUrl) && URLUtil.isNetworkUrl(adTagUrl)) {
                mJioVideoPlayerController =
                    JioMediationVideoController(customListener, jioAdView, adTagUrl ?: "")
                mJioVideoPlayerController!!.prepare()
            } else {
                customListener.onAdFailed(
                    JioAdError.JioAdErrorType.ERROR_MANDATORY_PARAM_MISSING.toString(),
                    "GoogleIMA " + "adtagcode is getting null"
                )
            }
        } catch (e: Exception) {
            customListener.onAdFailed(
                JioAdError.JioAdErrorType.ERROR_MANDATORY_PARAM_MISSING.toString(),
                "GoogleIMA " + e.message
            )
        }
    }


    override fun showAd() {
        //Nothing to do
    }


    override fun onInvalidate() {
        mJioVideoPlayerController?.destroy()
        mJioVideoPlayerController = null
    }

    private fun extrasAreValid(serverExtras: Map<String, Any>): Boolean {
        return serverExtras.containsKey(Constants.MediationJsonKeys.AD_TAG_URL) ?: false
    }

    fun onPause() {
        if (mJioVideoPlayerController != null) {
            mJioVideoPlayerController?.pause(false)
        }
    }

    fun onResume() {
        if (mJioVideoPlayerController != null) {
            mJioVideoPlayerController?.resume(false)
        }
    }

    fun onBackPressed() {}
    fun onDestroy() {
        if (mJioVideoPlayerController != null) {
            mJioVideoPlayerController?.destroy()
        }
    }
}