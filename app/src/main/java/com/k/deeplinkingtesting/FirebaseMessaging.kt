package com.k.deeplinkingtesting

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.izooto.iZooto
import org.json.JSONObject


class FirebaseMessaging : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        try {
            val data: Map<String?, String?> = message.data
            val jsonObject = JSONObject(data)
            if (jsonObject.has("t") || jsonObject.has("an")) {
                iZooto.iZootoHandleNotification(this, data)
            }
        } catch (ex: Exception) {
            Log.e("ABC", "data")
        }

    }

    override fun onNewToken(token: String) {
        Log.d("TAG", "Refreshed token: $token")
    }
}
