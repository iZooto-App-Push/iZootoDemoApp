package com.k.deeplinkingtesting
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.k.deeplinkingtesting.remoteconfigData.AppOpenAdManager

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appOpenAdManager = AppOpenAdManager(this)
        appOpenAdManager.loadAndShowAppOpenAd()

        startActivity(Intent(this, CommonActivity::class.java))
    }

}