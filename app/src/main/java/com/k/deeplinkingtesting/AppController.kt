package com.k.deeplinkingtesting

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.yandex.mobile.ads.common.MobileAds
import androidx.core.content.edit
import com.indixital.DeepLinkCallback
import com.indixital.Indixital
import org.json.JSONObject


class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        if (isMainProcess()) {
            Log.d("AppController", "Running in MAIN process")
            MobileAds.initialize(this@AppController) {}
           // addReferrer(1)
            Indixital.Builder(applicationContext)
                .setDevKey("your_dev_key_here")
                .setDeepLinkCallback(object : DeepLinkCallback {
                    override fun onDeepLinkCaptured(deepLinkData: JSONObject) {
                        Log.d("LOG_TAG", "Received deep link data: $deepLinkData")
                        passDeepLinkDataToActivity(deepLinkData)


                    }

                    private fun passDeepLinkDataToActivity(`object`: JSONObject) {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("DL_ATTRS", `object`.toString())
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // required when starting activity from non-Activity context
                        applicationContext.startActivity(intent)
                    }

                    override fun onConsumerDeepLinkCaptured(deepLinkData: JSONObject) {
                        Log.d("LOG_TAG", "Received deep link data: $deepLinkData")
                    }
                })

                .build()
        } else {
            Log.d("AppController", "Running in a non-main process, skipping init")
        }


    }

    private fun addReferrer(count : Int) {
        Log.i("Referrer", "" + count)
        val sharedPrefs = getSharedPreferences("install_ref", Context.MODE_PRIVATE)
        val alreadyFetched = sharedPrefs.getBoolean("referrer_fetched", false)

    }



    private fun isMainProcess(): Boolean {
        val processName = applicationContext.applicationInfo.processName
        val currentProcessId = android.os.Process.myPid()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val runningProcesses = activityManager.runningAppProcesses

        runningProcesses?.forEach {
            if (it.pid == currentProcessId) {
                return it.processName == processName
            }
        }
        return false
    }





}





