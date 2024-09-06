package com.k.deeplinkingtesting.admob

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowMetrics
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.k.deeplinkingtesting.MenuItem
import com.k.deeplinkingtesting.R


class InLineBannerAdActivity : AppCompatActivity() {
    private val recyclerViewItems: MutableList<Any> = ArrayList()
    private val adView: AdView? = null
    private var adapter : PulseAdapter? =null
    private val mFirebaseRemoteConfig: FirebaseRemoteConfig? = null
    private val itemList = mutableListOf<Any>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_in_line_banner_ad)

        // Enable the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "News List"


        val recyclerView: RecyclerView? = findViewById(R.id.recycler_view)

        // The size of the RecyclerView depends on the height of the ad.
        recyclerView?.setHasFixedSize(false)

        // Specify a linear layout manager.
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager

        addRegularItems();
       // loadBannerAds()

        adapter = PulseAdapter(itemList,this)
        recyclerView!!.adapter = adapter

        loadNativeAds()


        // Specify an adapter.
       // val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder> = RecyclerViewAdapter(this, recyclerViewItems)
       // recyclerView?.adapter = adapter

    }
    private fun addRegularItems() {
        // Add your regular items to the itemList
        itemList.add("Item 1")
        itemList.add("Item 2")
        itemList.add("Item 3")
        itemList.add("Item 1")
        itemList.add("Item 2")
        itemList.add("Item 3")
        itemList.add("Item 1")
        itemList.add("Item 2")
        itemList.add("Item 3")
        itemList.add("Item 1")
        itemList.add("Item 2")
        itemList.add("Item 3")
        // Add more items as needed
    }

    private fun loadNativeAds() {
        val adLoader = AdLoader.Builder(this, AD_UNIT_ID)
            .forNativeAd { nativeAd -> // Insert the native ad into the list
                itemList.add(nativeAd)
                adapter!!.notifyItemInserted(itemList.size - 1)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the error
                    Log.e("AdLoadError", "Ad failed to load: " + adError.message)
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }
    // Determine the screen width to use for the ad width.
    private val adWidth: Int
        get() {
            val displayMetrics = resources.displayMetrics
            val adWidthPixels =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val windowMetrics: WindowMetrics = this.windowManager.currentWindowMetrics
                    windowMetrics.bounds.width()
                } else {
                    displayMetrics.widthPixels
                }
            val density = displayMetrics.density
            return (adWidthPixels / density).toInt()
        }
    /** Adds [MenuItem]'s from a JSON file. */
    /** Adds banner ads to the items list. */
    private fun addBannerAds() {
        // Loop through the items array and place a new banner ad in every ith position in
        // the items List.
        var i = 0
        while (i <= recyclerViewItems.size) {
            val adView = AdView(this@InLineBannerAdActivity)
            adView.setAdSize(AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(this, adWidth))
            adView.adUnitId = AdUnitConfig.inlineBannerAdUnitId
            recyclerViewItems.add(i, adView)
            i += ITEMS_PER_AD
        }
    }
    /** Sets up and loads the banner ads. */
    private fun loadBannerAds() {
        // Load the first banner ad in the items list (subsequent ads will be loaded automatically
        // in sequence).
        loadBannerAd(0)
    }


    /** Loads the banner ads in the items list. */
    private fun loadBannerAd(index: Int) {
        if (index >= recyclerViewItems.size) {
            return
        }
        val item =
            itemList[index] as? AdView
                ?: throw ClassCastException("Expected item at index $index to be a banner ad ad.")

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        item.adListener =
            object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    // The previous banner ad loaded successfully, call this method again to
                    // load the next ad in the items list.
                    loadBannerAd(index + ITEMS_PER_AD)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // The previous banner ad failed to load. Call this method again to load
                    // the next ad in the items list.
                    val error =
                        String.format(
                            "domain: %s, code: %d, message: %s",
                            loadAdError.domain,
                            loadAdError.code,
                            loadAdError.message,
                        )
                    Log.e(
                        "MainActivity",
                        "The previous banner ad failed to load with error: " +
                                error +
                                ". Attempting to" +
                                " load the next banner ad in the items list.",
                    )
                    loadBannerAd(index + ITEMS_PER_AD)
                }
            }

        // Load the banner ad.
        item.loadAd(AdRequest.Builder().build())
    }

    companion object {
        // A banner ad is placed in every 8th position in the RecyclerView.
        const val ITEMS_PER_AD = 2
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
    }
    override fun onResume() {
        for (item in recyclerViewItems) {
            if (item is AdView) {
                item.resume()
            }
        }
        super.onResume()
    }

    override fun onPause() {
        for (item in recyclerViewItems) {
            if (item is AdView) {
                item.pause()
            }
        }
        super.onPause()
    }
    override fun onDestroy() {
        for (item in recyclerViewItems) {
            if (item is AdView) {
                item.destroy()
            }
        }
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                // Perform your custom action here
                // For example, show a dialog or navigate to a specific activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}