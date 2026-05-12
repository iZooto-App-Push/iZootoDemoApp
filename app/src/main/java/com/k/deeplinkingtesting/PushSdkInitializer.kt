package com.k.deeplinkingtesting

import android.app.Application
import android.os.Trace
import android.util.Log
import com.izooto.iZooto

object PushSdkInitializer {

    fun init(app: Application) {
        Trace.beginSection("push_sdk_init")

        iZooto.initialize(app)
            .setTokenReceivedListener { token ->
                Log.d("iZooto", "Token: $token")
            }
            .build()

        Trace.endSection()
    }
}