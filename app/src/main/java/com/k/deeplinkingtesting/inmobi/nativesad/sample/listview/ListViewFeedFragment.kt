package com.k.deeplinkingtesting.inmobi.nativesad.sample.listview

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.core.view.ViewCompat
import androidx.fragment.app.ListFragment
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiNative
import com.inmobi.ads.listeners.NativeAdEventListener
import com.k.deeplinkingtesting.inmobi.PlacementId
import com.k.deeplinkingtesting.inmobi.nativesad.utility.FeedData
import com.k.deeplinkingtesting.inmobi.nativesad.utility.FeedData.FeedItem
import com.k.deeplinkingtesting.inmobi.nativesad.utility.SwipeRefreshLayoutWrapper

/**
 * Demonstrates the use of InMobiNativeStrand to place ads in a ListView.
 *
 *
 * Note: Swipe to refresh ads.
 */
class ListViewFeedFragment : ListFragment() {
    private val mStrands: MutableList<InMobiNative> = ArrayList()

    //Position in feed where the Ads needs to be placed once loaded.
    // Note: Actual position where ad is visible depends on
    // 1. availability of ad (case like NO_FILL)
    // 2. Order in which ad response arrives.
    private val mAdPositions = intArrayOf(3, 6, 17)

    private var mFeedAdapter: BaseAdapter? = null
    private var mFeedItems: ArrayList<FeedItem>? = null
    private val mFeedMap: MutableMap<Int, FeedItem> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val listFragmentView = super.onCreateView(inflater, container, savedInstanceState)
        val swipeRefreshLayout = SwipeRefreshLayoutWrapper.getInstance(
            requireActivity(),
            object : SwipeRefreshLayoutWrapper.Listener {
                override fun canChildScrollUp(): Boolean {
                    val listView = listView
                    return listView.visibility == View.VISIBLE && canListViewScrollUp(listView)
                }

                override fun onRefresh() {
                    refreshAds()
                }
            })
        swipeRefreshLayout.addView(
            listFragmentView,
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        return swipeRefreshLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mFeedItems = FeedData.generateFeedItems(NUM_FEED_ITEMS)
        mFeedAdapter = FeedItemAdapter(requireActivity(), mFeedItems)
        listAdapter = mFeedAdapter
        createStrands()
        loadAds()
    }

    private fun createStrands() {
        for (position in mAdPositions) {
            val nativeStrand = InMobiNative(
                requireActivity(),
                PlacementId.YOUR_PLACEMENT_ID_NATIVE, StrandAdListener(position)
            )

            mStrands.add(nativeStrand)
        }
    }

    private fun loadAds() {
        for (strand in mStrands) {
            strand.load()
        }
    }

    private fun refreshAds() {
        clearAds()
        createStrands()
        loadAds()
    }

    private fun clearAds() {
        val feedItemIterator = mFeedItems!!.iterator()
        while (feedItemIterator.hasNext()) {
            val feedItem = feedItemIterator.next()
            if (feedItem is AdFeedItem) {
                feedItemIterator.remove()
            }
        }
        mFeedAdapter!!.notifyDataSetChanged()
        for (strand in mStrands) {
            strand.destroy()
        }
        mStrands.clear()
        mFeedMap.clear()
    }

    override fun onDestroyView() {
        clearAds()
        super.onDestroyView()
    }


    private inner class StrandAdListener(private val mPosition: Int) : NativeAdEventListener() {
        override fun onAdLoadSucceeded(inMobiNativeStrand: InMobiNative, adMetaInfo: AdMetaInfo) {
            Log.d(
                TAG,
                "Strand loaded at position $mPosition"
            )
            if (!mFeedItems!!.isEmpty()) {
                val oldFeedItem = mFeedMap[mPosition]
                if (oldFeedItem != null) {
                    mFeedMap.remove(mPosition)
                    mFeedItems!!.remove(oldFeedItem)
                }
                val adFeedItem = AdFeedItem(inMobiNativeStrand)
                mFeedMap[mPosition] = adFeedItem
                mFeedItems!!.add(mPosition, adFeedItem)
                mFeedAdapter!!.notifyDataSetChanged()
            }
        }

        override fun onAdLoadFailed(
            inMobiNativeStrand: InMobiNative,
            inMobiAdRequestStatus: InMobiAdRequestStatus
        ) {
            Log.d(
                TAG,
                "Ad Load failed  for" + mPosition + "(" + inMobiAdRequestStatus.message + ")"
            )
            if (!mFeedItems!!.isEmpty()) {
                val oldFeedItem = mFeedMap[mPosition]
                if (oldFeedItem != null) {
                    mFeedMap.remove(mPosition)
                    mFeedItems!!.remove(oldFeedItem)
                    mFeedAdapter!!.notifyDataSetChanged()
                    Log.d(
                        TAG,
                        "Ad removed for$mPosition"
                    )
                }
            }
        }

        override fun onAdFetchSuccessful(inMobiNative: InMobiNative, adMetaInfo: AdMetaInfo) {
            Log.d(TAG, "onAdFetchSuccessful with bid " + adMetaInfo.bid)
        }

        override fun onAdFullScreenDismissed(inMobiNative: InMobiNative) {
            Log.d(TAG, "Ad fullscreen dismissed.")
        }

        override fun onAdFullScreenWillDisplay(inMobiNative: InMobiNative) {
            Log.d(TAG, "Ad going fullscreen.")
        }

        override fun onAdFullScreenDisplayed(inMobiNative: InMobiNative) {
            Log.d(TAG, "Ad fullscreen displayed.")
        }

        override fun onUserWillLeaveApplication(inMobiNative: InMobiNative) {
            Log.d(TAG, "User left app.")
        }

        override fun onAdClicked(inMobiNativeStrand: InMobiNative) {
            Log.d(
                TAG,
                "Click recorded for ad at position:$mPosition"
            )
        }

        override fun onAdStatusChanged(inMobiNative: InMobiNative) {
            Log.d(TAG, "Ad status changed")
        }

        override fun onAdImpression(inMobiNative: InMobiNative) {
            Log.d(TAG, "onAdImpression")
        }
    }

    @Suppress("deprecation")
    private fun canListViewScrollUp(listView: ListView): Boolean {
        return if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.canScrollVertically(listView, -1)
        } else {
            listView.childCount > 0 &&
                    (listView.firstVisiblePosition > 0
                            || listView.getChildAt(0).top < listView.paddingTop)
        }
    }

    companion object {
        private val TAG: String = ListViewFeedFragment::class.java.simpleName

        private const val NUM_FEED_ITEMS = 20

        val title: String
            get() = "ListView Placement"
    }
}
