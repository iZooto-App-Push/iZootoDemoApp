package com.k.deeplinkingtesting.inmobi.sample

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiBanner
import com.inmobi.ads.InMobiBanner.AnimationType
import com.inmobi.ads.listeners.BannerAdEventListener
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.inmobi.PlacementId
import com.k.deeplinkingtesting.inmobi.sample.BannerAdsActivity.OnHeadlineSelectedListener
import com.k.deeplinkingtesting.inmobi.utility.Constants
import com.k.deeplinkingtesting.inmobi.utility.DataFetcher
import com.k.deeplinkingtesting.inmobi.utility.NewsSnippet
import org.json.JSONException
import org.json.JSONObject


class BannerAdsActivity : AppCompatActivity() {
    private var mBannerAd: InMobiBanner? = null
    private var mNewsListView: ListView? = null

    private val mHandler = Handler(Looper.getMainLooper())
    private val mItemList: MutableList<NewsSnippet> = ArrayList()
    private var mAdapter: NewsFeedAdapter? = null

    val BANNER_WIDTH: Int = 320
    val BANNER_HEIGHT: Int = 50

    internal fun interface OnHeadlineSelectedListener {
        fun onArticleSelected(position: Int)
    }

    private val mCallback: OnHeadlineSelectedListener =
        OnHeadlineSelectedListener { position: Int -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_banner_ads)

        setupListView()
        headlines
        setupBannerAd()
    }

    private fun setupBannerAd() {
        try {
            mBannerAd = InMobiBanner(this@BannerAdsActivity, PlacementId.YOUR_PLACEMENT_ID_BANNER)
            mBannerAd?.setBannerSize(BANNER_WIDTH,BANNER_HEIGHT)
            mBannerAd?.setEnableAutoRefresh(true)
            val adContainer = findViewById<RelativeLayout>(R.id.ad_container)
            mBannerAd!!.setAnimationType(AnimationType.ROTATE_HORIZONTAL_AXIS)
            mBannerAd!!.setListener(object : BannerAdEventListener() {
                override fun onAdLoadSucceeded(
                    inMobiBanner: InMobiBanner,
                    adMetaInfo: AdMetaInfo
                ) {
                    Log.d(TAG, "onAdLoadSucceeded with bid " + adMetaInfo.bid)
                }

                override fun onAdLoadFailed(
                    inMobiBanner: InMobiBanner,
                    inMobiAdRequestStatus: InMobiAdRequestStatus
                ) {
                    Log.d(
                        TAG, "Banner ad failed to load with error: " +
                                inMobiAdRequestStatus.message
                    )
                }

                override fun onAdClicked(inMobiBanner: InMobiBanner, map: Map<Any, Any>) {
                    Log.d(TAG, "onAdClicked")
                }

                override fun onAdDisplayed(inMobiBanner: InMobiBanner) {
                    Log.d(TAG, "onAdDisplayed")
                }

                override fun onAdDismissed(inMobiBanner: InMobiBanner) {
                    Log.d(TAG, "onAdDismissed")
                }

                override fun onUserLeftApplication(inMobiBanner: InMobiBanner) {
                    Log.d(TAG, "onUserLeftApplication")
                }

                override fun onRewardsUnlocked(inMobiBanner: InMobiBanner, map: Map<Any, Any>) {
                    Log.d(TAG, "onRewardsUnlocked")
                }

                override fun onAdImpression(inMobiBanner: InMobiBanner) {
                    Log.d(TAG, "onAdImpression")
                }
            })
            setBannerLayoutParams()
            adContainer.addView(mBannerAd)
            mBannerAd!!.load()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setBannerLayoutParams() {
        try {
            val width = toPixelUnits(Constants.BANNER_WIDTH)
            val height = toPixelUnits(Constants.BANNER_HEIGHT)
            val bannerLayoutParams = RelativeLayout.LayoutParams(width, height)
            bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            bannerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            mBannerAd!!.layoutParams = bannerLayoutParams
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun toPixelUnits(dipUnit: Int): Int {
        val density = resources.displayMetrics.density
        return Math.round(dipUnit * density)
    }

    private fun setupListView() {
        try {

            mNewsListView = findViewById<View>(R.id.lvNewsContainer) as ListView
            mAdapter = NewsFeedAdapter(this, mItemList)
            mNewsListView!!.adapter = mAdapter
            mNewsListView!!.onItemLongClickListener =
                OnItemLongClickListener { adapterView, view, position, id ->
                    val confirmationDialog =
                        AlertDialog.Builder(this@BannerAdsActivity)
                    confirmationDialog.setTitle("Delete Item?")
                    confirmationDialog.setPositiveButton(
                        "Yes"
                    ) { dialog, which ->
                        val newsSnippet = mItemList[position]
                        mItemList.remove(newsSnippet)
                        mAdapter!!.notifyDataSetChanged()
                    }
                    confirmationDialog.setNegativeButton(
                        "No"
                    ) { dialog, which -> dialog.cancel() }

                    confirmationDialog.show()
                    true
                }

            mNewsListView!!.onItemClickListener =
                OnItemClickListener { adapterView, view, position, l ->
                    mCallback.onArticleSelected(position)
                    mNewsListView!!.setItemChecked(position, true)
                    mAdapter!!.notifyDataSetChanged()
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val headlines: Unit
        get() {
            DataFetcher().getFeed(
                Constants.FEED_URL
            ) { data, message ->
                if (null != data) {
                    mHandler.post { loadHeadlines(data) }
                }
            }
        }

    private fun loadHeadlines(data: String) {
        try {
            val feed = JSONObject(data).getJSONArray(Constants.FeedJsonKeys.FEED_LIST)
            for (i in 0 until feed.length()) {
                val item = feed.getJSONObject(i)
                Log.v(TAG, item.toString())
                val feedEntry = NewsSnippet()
                try {
                    feedEntry.title = item.getString(Constants.FeedJsonKeys.CONTENT_TITLE)
                    val enclosureObject =
                        item.getJSONObject(Constants.FeedJsonKeys.CONTENT_ENCLOSURE)
                    if (!enclosureObject.isNull(Constants.FeedJsonKeys.CONTENT_LINK)) {
                        feedEntry.imageUrl =
                            item.getJSONObject(Constants.FeedJsonKeys.CONTENT_ENCLOSURE)
                                .getString(Constants.FeedJsonKeys.CONTENT_LINK)
                    } else {
                        feedEntry.imageUrl = Constants.FALLBACK_IMAGE_URL
                    }
                    feedEntry.landingUrl = item.getString(Constants.FeedJsonKeys.CONTENT_LINK)
                    feedEntry.content = item.getString(Constants.FeedJsonKeys.FEED_CONTENT)
                    feedEntry.isSponsored = false
                    mItemList.add(feedEntry)
                } catch (e: JSONException) {
                    Log.d(TAG, e.toString())
                }
            }
            mAdapter!!.notifyDataSetChanged()
        } catch (e: JSONException) {
            Log.d(TAG, "JSONException for loadHeadlines", e)
        }
    }

    companion object {
        private val TAG: String = BannerAdsActivity::class.java.simpleName
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBannerAd != null) {
            mBannerAd?.destroy()
        }
    }
}
