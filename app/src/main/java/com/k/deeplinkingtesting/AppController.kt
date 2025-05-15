package com.k.deeplinkingtesting

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri

import com.google.android.gms.ads.MobileAds
import com.indixital.DeepLinkCallback
import com.indixital.Indixital
import org.json.JSONObject


class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        Indixital.init(this, "123456789")
        Indixital.getInstance().setCallback(object : DeepLinkCallback {
            override fun onDeepLinkCaptured(deepLinkData: JSONObject) {
                Log.e("onDeepLinkCaptured", deepLinkData.toString())
                val activity = Indixital.getInstance().getCurrentActivity() ?: return
                try {
                    val jsonObject = deepLinkData
                    var url = jsonObject.optString("url")
                    //  val urlString = "https://stage-cdn.indixital.com/details?pid=123456&clickid=e45fgh1234ij&deeplink=test://thirdactivity&dp_value=test_camp&utm_source=&utm_campaign=&utm_medium=&utm_term=&utm_content="

                    val uri = url.toUri()

                    val pid = uri.getQueryParameter("pid")
                    val clickId = uri.getQueryParameter("clickid")
                    val deeplink = uri.getQueryParameter("deeplink")

                    println("pid: $pid")
                    println("clickid: $clickId")
                    println("deeplink: $deeplink")

                    Log.e("Referrer", jsonObject.optString("utm_source"))
                    Log.e("Referrer", jsonObject.optString("utm_campaign"))
                    Log.e("Referrer", jsonObject.optString("utm_medium"))
                    Log.e("Referrer", jsonObject.optString("utm_term"))
                    Log.e("Referrer", jsonObject.optString("utm_content"))
                    Log.e("Referrer", jsonObject.optString("utm_medium"))
                    val intent = Intent(activity, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        putExtra("deepLinkData", jsonObject.toString())
                    }
                    activity.startActivity(intent)
                    activity.finish()
                } catch (ex: Exception) {
                    Log.e("onDeepLinkCaptured", ex.toString())
                }
            }

            override fun onConsumerDeepLinkCaptured(deepLinkData: String) {
                Log.e("onConsumerDeepLinkCaptured", deepLinkData)
                val activity = Indixital.getInstance().getCurrentActivity() ?: return
                val intent = Intent(activity, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    putExtra("deepLinkData", deepLinkData.toString())

                }
                activity.startActivity(intent)
                activity.finish()
            }
        })
    }










}





