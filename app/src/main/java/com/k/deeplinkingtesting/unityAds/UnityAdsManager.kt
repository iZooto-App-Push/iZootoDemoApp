package com.k.deeplinkingtesting.unityAds


import android.app.Activity
import android.util.Log
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.databinding.ActivityUnityAdsBinding
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAds.load
import com.unity3d.ads.UnityAds.show
import com.unity3d.ads.UnityAdsShowOptions
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class UnityAdsManager(private val context: Activity) {

    private var binding: UnityAdsActivity? = null
    private val adUnitId = context.resources.getString(R.string.interstitial_ad_unit_id)
    private val loadListener: IUnityAdsLoadListener = object : IUnityAdsLoadListener {
        override fun onUnityAdsAdLoaded(placementId: String) {
            show(context, adUnitId, UnityAdsShowOptions(), showListener)
        }

        override fun onUnityAdsFailedToLoad(
            placementId: String,
            error: UnityAds.UnityAdsLoadError,
            message: String
        ) {
            Log.e(
                "UnityAdsExample",
                "Unity Ads failed to load ad for $placementId with error: [$error] $message"
            )
        }
    }

        private val showListener: IUnityAdsShowListener = object : IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(
                placementId: String,
                error: UnityAds.UnityAdsShowError,
                message: String
            ) {
                Log.e(
                    "UnityAdsExample",
                    "Unity Ads failed to show ad for $placementId with error: [$error] $message"
                )
            }

            override fun onUnityAdsShowStart(placementId: String) {
                Log.v("UnityAdsExample", "onUnityAdsShowStart: $placementId")
            }

            override fun onUnityAdsShowClick(placementId: String) {
                Log.v("UnityAdsExample", "onUnityAdsShowClick: $placementId")
            }

            override fun onUnityAdsShowComplete(
                placementId: String,
                state: UnityAds.UnityAdsShowCompletionState
            ) {
               if (binding != null) {
                  // binding?.interstitialAd?.isEnabled = true
               }
                Log.v("UnityAdsExample", "onUnityAdsShowComplete: $placementId")
            }
        }

    // Implement a function to load an interstitial ad. The ad will start to show after the ad has been loaded.
//    fun displayInterstitialAd(binding: ActivityUnityAdsBinding) {
//        this.binding = binding
//        load(adUnitId, loadListener)
//    }


    fun disableSSLCertificateValidation() {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate>? {
                    return null
                }

                override fun checkClientTrusted(
                    certs: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    certs: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }
            })
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { hostname: String?, session: SSLSession? -> true }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}