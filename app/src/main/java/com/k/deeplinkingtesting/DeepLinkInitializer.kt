package com.k.deeplinkingtesting

import android.app.Application
import android.os.Trace
import com.indixital.Indixital

object DeepLinkInitializer {

    fun init(app: Application) {
        Trace.beginSection("deeplink_init")

        Indixital.Builder(app)
            .setDevKey("your_dev_key_here")
            .build()

        Trace.endSection()
    }
}