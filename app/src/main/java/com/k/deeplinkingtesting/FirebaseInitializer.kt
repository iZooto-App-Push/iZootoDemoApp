package com.k.deeplinkingtesting

import android.app.Application
import android.os.Trace
import com.google.firebase.FirebaseApp

object FirebaseInitializer {

    fun init(app: Application) {
        Trace.beginSection("firebase_init")

        if (FirebaseApp.getApps(app).isEmpty()) {
            FirebaseApp.initializeApp(app)
        }

        Trace.endSection()
    }
}