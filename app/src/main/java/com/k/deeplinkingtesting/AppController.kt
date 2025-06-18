package com.k.deeplinkingtesting

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import com.adsbynimbus.Nimbus

import com.google.android.gms.ads.MobileAds

import com.izooto.iZooto
import org.json.JSONObject


class AppController : Application() {
    lateinit var context: Context
    override fun onCreate() {
        super.onCreate()

        Nimbus.initialize(this, "dev-publisher", "DEV-af79-4612-87a9-aa70c17e8dc6")
        Nimbus.testMode = true


//        IndixitalSDK.init(
//            this, "aeb7d82c8dcc0bcebc9684c2ad8a4a384980e640", object : SubscriberResult {
//                override fun onSuccess(response: String?) {
//                    Log.d("INDIXITAL SDK", "init onSuccess: $response")
//                }
//
//                override fun onFailure(error: String?) {
//                    Log.d("INDIXITAL SDK", "init onFailure: $error")
//                }
//            })
//        IndixitalSDK.subscribeForDeepLink(object : com.indixital.feature.deeplink.DeepLinkListener {
//            override fun onDeepLinking(indDeepLinkResult: com.indixital.feature.deeplink.DeepLinkResult) {
//                Log.d("INDIXITAL SDK", "deepLinkResult: $indDeepLinkResult")
//
//                when(indDeepLinkResult.status) {
//                    com.indixital.feature.deeplink.DeepLinkResult.Status.FOUND -> {
//                        Log.d("INDIXITAL SDK", "Deep link found")
//                    }
//
//                    com.indixital.feature.deeplink.DeepLinkResult.Status.NOT_FOUND -> {
//                        Log.d("INDIXITAL SDK", "Deep link not found")
//                        return
//                    }
//
//                    else -> {
//                        val dlError = indDeepLinkResult.error
//                        Log.d("INDIXITAL SDK", "There was an error getting Deep Link data: $dlError")
//                        return
//                    }
//                }
//
//                // deeplink object
//                var indDeepLinkObj: com.indixital.feature.deeplink.DeepLink = indDeepLinkResult.deepLink
//                try {
//                    Log.d("INDIXITAL SDK", "The DeepLink data is: $indDeepLinkObj")
//                } catch (e: Exception) {
//                    Log.d("INDIXITAL SDK", "DeepLink data came back null")
//                    return
//                }
//
//                // An example for using is_deferred
//                if (indDeepLinkObj.isDeferred == true) {
//                    Log.d("INDIXITAL SDK", "This is a deferred deep link")
//                } else {
//                    Log.d("INDIXITAL SDK", "This is a direct deep link")
//                }
//
//                try {
//                    val deeplinkValue = indDeepLinkObj.deepLinkValue
//
//                    val intent = Intent(applicationContext, MainActivity::class.java).apply {
//                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
//                                Intent.FLAG_ACTIVITY_NEW_TASK or
//                                Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    }
//                    intent.putExtra("deepLinkData",deeplinkValue)
//                    startActivity(intent)
//
//                    Log.d("INDIXITAL SDK", "The DeepLink will route to: $deeplinkValue")
//                } catch (e: Exception) {
//                    Log.d("INDIXITAL SDK", "There's been an error: $e")
//                    return
//                }
//            }
//        })
//
//        IndixitalSDK.fetchInstallReferrerData(this, object: com.indixital.feature.referrer.ReferrerListener {
//
//            override fun onConversionDataSuccess(conversionData: Map<String, Any>?) {
//                Log.d("INDIXITAL SDK", "conversionData: $conversionData")
//            }
//
//            override fun onConversionDataFail(error: String?) {
//                Log.d("INDIXITAL SDK", "conversionError: $error")
//            }
//
//        })
        iZooto.initialize(this).build()
    }










}





