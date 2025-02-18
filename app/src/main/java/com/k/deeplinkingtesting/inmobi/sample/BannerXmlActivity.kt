package com.k.deeplinkingtesting.inmobi.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.inmobi.ads.InMobiBanner
import com.k.deeplinkingtesting.R

class BannerXmlActivity : AppCompatActivity() {
    private var mBanner: InMobiBanner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_xml)
        mBanner = findViewById<View>(R.id.banner) as InMobiBanner
        mBanner!!.load()
    }
}
