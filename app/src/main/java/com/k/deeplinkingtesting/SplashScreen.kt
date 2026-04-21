package com.k.deeplinkingtesting
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.k.deeplinkingtesting.admob.AdMobActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            startActivity(Intent(this, CommonActivity::class.java))
    }

}