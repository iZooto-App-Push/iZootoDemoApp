package com.k.deeplinkingtesting

import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.outbrain.OBSDK.Entities.OBRecommendation
import com.outbrain.OBSDK.Entities.OBRecommendationsResponse
import com.outbrain.OBSDK.FetchRecommendations.OBRequest
import com.outbrain.OBSDK.FetchRecommendations.RecommendationsListener
import com.outbrain.OBSDK.Outbrain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException

class ClassicWithImageActivity : AppCompatActivity(), RecommendationsListener {
    private lateinit var listView: ListView
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classic_with_image)
        listView = findViewById(R.id.outbrain_main_list_view)
        listView.setDivider(null)
        listView.setDividerHeight(0)
        val headerView = layoutInflater.inflate(R.layout.list_header, null)
        listView.addHeaderView(headerView)
        val whatIsWrapper = headerView.findViewById<ImageView>(R.id.recommended_by_image)
        whatIsWrapper.setOnClickListener { launchWhatIs() }
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // Set to 0 for testing to always fetch fresh data
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        fetchForRecommendations()
       // launchWhatIs()
    }

    private fun launchWhatIs() {


         val url = "https://odb.outbrain.com/utils/get?widgetJSId=SDK_1&key=DATAB2HQ71I65P5JML02NJDEE&idx=0&format=vjnc&rand=7858&version=4.30.9&url=http%3A%2F%2Fmobile-demo.outbrain.com&testMode=true&location=us&doo=false&api_user_id=278559ce-b132-4b7f-9078-e20fe121216d&installationType=android_sdk&secured=true&ref=https%3A%2F%2Fapp-sdk.outbrain.com%2F&dss=5.3&dm=Pixel%2B4a&deviceType=mobile&dos=android&platform=android&dosv=33&app_ver=7.3.4&app_id=com.k.deeplinkingtesting&rtbEnabled=true&va=true&darkMode=false"

         runBlocking {
             CoroutineScope(Dispatchers.Main).launch {
                 try {
                     val response = HttpSingleton.sendGetRequest(url)
                     response?.response?.recommendations?.let { recommendations ->
                        //val adapter = RecommendationsAdapter(recommendations)
                        // listView!!.adapter = adapter

                         // recyclerView.adapter = adapter
                     }
                 } catch (e: IOException) {
                     e.printStackTrace()
                 }
             }
         }









       // val aboutOutbrainUrl = Outbrain.getOutbrainAboutURL(this)
       // Log.e("aboutOutbrainUrl", aboutOutbrainUrl)
       // CatalogUtils.openURLInBrowser(aboutOutbrainUrl, this)
    }

    private fun fetchForRecommendations() {

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    // Update UI with the fetched value
                    val adUnit = remoteConfig.getString("widgetId")
                    // adView?.adUnitId=welcomeMessage
                    Log.d("widgetId", adUnit)
                    val request = OBRequest()
                    request.widgetId = "APP_1"
                    request.url = OUTBRAIN_SAMPLE_WIDGET_URL
                    // request.setUrl(getString(R.string.outbrain_sample_url));
                    Outbrain.fetchRecommendations(request, this)
                } else {
                    Log.d("adUnit", "error")

                    // Handle error
                }
            }
    }

    override fun onOutbrainRecommendationsSuccess(recommendations: OBRecommendationsResponse) {
        assignAdapter(recommendations)
    }

    override fun onOutbrainRecommendationsFailure(ex: Exception) {
        ex.printStackTrace()
    }

    private fun assignAdapter(recommendations: OBRecommendationsResponse) {
        val adapter =
            OutbrainSingleItemLayoutAdapter(this, R.layout.c_without_image, recommendations.all)
        listView!!.adapter = adapter
        listView!!.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            try {
                openRecommendationInBrowser(recommendations[i - 1])
            } catch (e: Exception) {
            }
        }
    }

    private fun openRecommendationInBrowser(obRecommendation: OBRecommendation) {
        CatalogUtils.openURLInBrowser(Outbrain.getUrl(obRecommendation), this)
    }

    companion object {
        private const val OUTBRAIN_SAMPLE_WIDGET_ID = "SDK_1"
        private const val OUTBRAIN_SAMPLE_WIDGET_URL =
            "http://mobile-demo.outbrain.com/2013/12/15/test-page-2"
        private const val OUTBRAIN_SAMPLE_BUNDLE_URL =
            "https://play.google.com/store/apps/details?id=com.outbrain"
        private const val OUTBRAIN_SAMPLE_PORTAL_URL =
            "https://lp.outbrain.com/increase-sales-native-ads/"
    }
}