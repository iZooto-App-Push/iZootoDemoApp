package com.k.deeplinkingtesting
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.k.deeplinkingtesting.admob.AdMobActivity
import com.k.deeplinkingtesting.unityAds.UnityAdsActivity
import com.yandex.mobile.ads.instream.media3.YandexAdsLoader

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            startActivity(Intent(this, CommonActivity::class.java))
    }

}