package com.k.deeplinkingtesting

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.izooto.NotificationHelperListener
import com.izooto.Payload
import com.izooto.iZooto


class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
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

                }
                override fun onNotificationOpened(data: String) {
                    Log.e("Open Notification", data)
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            })
            .build()
        iZooto.promptForPushNotifications();
    }
}
