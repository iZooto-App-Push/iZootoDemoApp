package com.k.deeplinkingtesting

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var deepLinkData : TextView? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        deepLinkData = findViewById(R.id.deepLinkData)
        val deepLinkDataString = intent.getStringExtra("deepLinkData")

        // Optional: Parse if it was originally a JSONObject or another format
        deepLinkDataString?.let {
            Log.d("DeepLinkData", " Received data: $it")
            deepLinkData?.text = deepLinkDataString
            // If it was JSON, you can parse it back like:
            // val jsonObject = JSONObject(it)
        }
    }


}
