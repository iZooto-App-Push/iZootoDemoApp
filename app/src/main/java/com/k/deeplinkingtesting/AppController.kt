package com.k.deeplinkingtesting

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.izooto.NotificationHelperListener
import com.izooto.Payload
import com.izooto.iZooto
import com.k.deeplinkingtesting.appopen.OnAdsCallbackListener
import com.yandex.mobile.ads.common.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AppController : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Main.immediate)
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        try {
            // Initialize Google Mobile Ads SDK on the Main Thread
            com.google.android.gms.ads.MobileAds.initialize(this){ initializationStatus ->
                val statusMap = initializationStatus.adapterStatusMap
                for ((adapterClass, status) in statusMap) {
                    Log.d(
                        "CommonActivity",
                        "Adapter name: $adapterClass, Description: ${status.description}, Latency: ${status.latency}"
                    )
                }
            }

             applicationScope.launch {
                delay(2000)
                GAMAdManager.setAppOpenManager(this@AppController, resources.getString(R.string.gam_app_open), object :
                    OnAdsCallbackListener {
                    override fun onComplete() {
                        super.onComplete()
                        Log.d("CommonActivity", "onComplete.")
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        Log.d("CommonActivity", "onAdImpression.")
                    }

                    override fun onError(var1: Int, var2: String?) {
                        super.onError(var1, var2)
                        Log.e("CommonActivity", "Error: $var1, $var2")
                    }
                })
            }


            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            Log.e("AppController", "Ads execution failure " + e.message)
        }

        iZooto.initialize(this)
            .setTokenReceivedListener { token: String? -> Log.e("Token", token!!) }
            .setLandingURLListener { landingUrl: String? ->
                Log.e("landing URL", landingUrl!!)
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .setNotificationReceiveListener(object : NotificationHelperListener {
                override fun onNotificationReceived(payload: Payload) {

                    Log.e("Received payload", ""+payload.defaultNotificationPreview)

                }
                override fun onNotificationOpened(data: String) {
                    Log.e("Open Notification", data)
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            })
            .build()
    }
}





