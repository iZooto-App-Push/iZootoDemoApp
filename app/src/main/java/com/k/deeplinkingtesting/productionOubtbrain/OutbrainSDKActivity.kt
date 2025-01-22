package com.k.deeplinkingtesting.productionOubtbrain

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.k.deeplinkingtesting.R
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class OutbrainSDKActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_out_brain_sdk_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Outbrain Production"

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        kotlinx.coroutines.runBlocking {
            fetchLiveUrlOutBrainData(recyclerView)
        }




    }
    suspend fun fetchLiveUrlOutBrainData(recyclerView: RecyclerView) {
        val client = HttpClient()
        try {
            var productionUrl  =  "https://mv.outbrain.com/Multivac/api/platforms?bundleUrl=https%3A%2F%2Fplay.google.com%2Fstore%2Fapps%2Fdetails%3Fid%3Dcom.k.deeplinkingtesting&widgetJSId=APP_8&key=DATAB2HQ71I65P5JML02NJDEE&api_user_id=278559ce-b132-4b7f-9078-e20fe121216d&lang=en&idx=0&format=vjnc&testMode=true"
            val response: io.ktor.client.statement.HttpResponse = client.get(productionUrl) {
                headers {
                    append("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Mobile Safari/537.36")
                    append("Cookie", "obuid=b958ee45-7055-4823-9fca-07a8c01481df")
                }
            }
            val responseBody = response.bodyAsText()
            val responseData = responseBody
            try{
                val json = Json { ignoreUnknownKeys = true }
                val jsonObject = json.parseToJsonElement(responseData).jsonObject

                val documentsArray =
                    jsonObject["response"]?.jsonObject?.get("documents")?.jsonObject?.get("doc")?.jsonArray
                val documents = documentsArray?.map {
                    val documentObject = it.jsonObject
                    Documents(
                        sourceName = documentObject["source_name"]?.jsonPrimitive?.content ?: "Unknown",
                        content = documentObject["content"]?.jsonPrimitive?.content ?: "Unknown",
                        landingURl = documentObject["url"]?.jsonPrimitive?.content ?: "Unknown",
                        bannerImage = documentObject["orig_url"]?.jsonPrimitive?.content ?: "Unknown",
                        publisherName = documentObject["adv_name"]?.jsonPrimitive?.content ?: "Unknown",
                        thumbnailUrl = documentObject["thumbnail"]?.jsonObject?.get("url")?.jsonPrimitive?.content ?: "" // Extract thumbnail URL
                        )
                } ?: emptyList()

//                val adapter = DocumentAdapter(documents) { document ->
//                    println("Document clicked: ${document.landingURl}")
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(document.landingURl))
//                    startActivity(intent)
//                }
//                recyclerView.layoutManager = LinearLayoutManager(this)
//                recyclerView.adapter = adapter


            }
            catch (e: Exception)
            {
                 Log.e("Exception",e.toString())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            client.close()
        }
    }
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

