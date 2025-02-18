package com.k.deeplinkingtesting.inmobi.nativesad.sample.recyclerview

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiNative
import com.inmobi.ads.listeners.NativeAdEventListener
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.inmobi.PlacementId
import com.k.deeplinkingtesting.inmobi.nativesad.utility.FeedData
import com.k.deeplinkingtesting.inmobi.nativesad.utility.FeedData.FeedItem
import com.k.deeplinkingtesting.inmobi.nativesad.utility.SwipeRefreshLayoutWrapper

/**
 * Demonstrates the use of InMobiNativeStrand to place ads in a RecyclerView.
 *
 *
 * Note: Swipe to refresh ads.
 */
class RecyclerFeedFragment : Fragment() {
    //All the InMobiNativeStrand instances created for this list feed will be held here
    private val mStrands: MutableList<InMobiNative> = ArrayList()

    //Position in feed where the Ads needs to be placed once loaded.
    private val mAdPositions = intArrayOf(3, 8, 18)

    private var mRecyclerView: RecyclerView? = null
    private var mFeedAdapter: FeedsAdapter? = null
    private var mFeedItems: ArrayList<FeedItem>? = null

    private val mFeedMap: MutableMap<Int, FeedItem> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler_feed, container, false)
        mRecyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        mRecyclerView!!.setHasFixedSize(false)
        val layoutManager = LinearLayoutManager(
            requireActivity().applicationContext
        )
        mRecyclerView!!.layoutManager = layoutManager
        val swipeRefreshLayout = SwipeRefreshLayoutWrapper.getInstance(
            requireActivity(),
            object : SwipeRefreshLayoutWrapper.Listener {
                override fun canChildScrollUp(): Boolean {
                    return mRecyclerView!!.visibility == View.VISIBLE && canViewScrollUp(
                        mRecyclerView!!
                    )
                }

                override fun onRefresh() {
                    refreshAds()
                }
            })
        swipeRefreshLayout.addView(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        return swipeRefreshLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mFeedItems = FeedData.generateFeedItems(NUM_FEED_ITEMS)
        mFeedAdapter = FeedsAdapter(mFeedItems, requireActivity())
        mRecyclerView!!.adapter = mFeedAdapter
        mFeedAdapter?.notifyDataSetChanged()
        createStrands()
        loadAds()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mFeedAdapter!!.notifyDataSetChanged()
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

    override fun onDestroyView() {
        clearAds()
        super.onDestroyView()
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

    @Suppress("deprecation")
    private fun canViewScrollUp(recyclerView: RecyclerView): Boolean {
        return if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.canScrollVertically(recyclerView, -1)
        } else {
            recyclerView.childCount > 0 &&
                    ((recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() > 0
                            || recyclerView.getChildAt(0).top < recyclerView.paddingTop)
        }
    }

    private inner class StrandAdListener(private val mPosition: Int) : NativeAdEventListener() {
        override fun onAdLoadSucceeded(
            inMobiNativeStrand: InMobiNative,
            adMetaInfo: AdMetaInfo
        ) {
            Log.d(
                TAG,
                "Strand loaded at position $mPosition"
            )
            if (mFeedItems!!.isNotEmpty()) {
                val oldFeedItem = mFeedMap[mPosition]
                if (oldFeedItem != null) {
                    mFeedMap.remove(mPosition)
                    mFeedItems!!.remove(oldFeedItem)
                }
                val adFeedItem = AdFeedItem(inMobiNativeStrand)
                mFeedMap[mPosition] = adFeedItem
                mFeedItems!!.add(mPosition, adFeedItem)
                mFeedAdapter!!.notifyItemChanged(mPosition)
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
                    mFeedAdapter!!.notifyItemRemoved(mPosition)
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
        }

        override fun onAdFullScreenWillDisplay(inMobiNative: InMobiNative) {
        }

        override fun onAdFullScreenDisplayed(inMobiNative: InMobiNative) {
        }

        override fun onUserWillLeaveApplication(inMobiNative: InMobiNative) {
        }

        override fun onAdClicked(inMobiNativeStrand: InMobiNative) {
            Log.d(
                TAG,
                "Click recorded for ad at position:$mPosition"
            )
        }

        override fun onAdStatusChanged(inMobiNative: InMobiNative) {
        }

        override fun onAdImpression(inMobiNative: InMobiNative) {
        }
    }

    companion object {
        private val TAG: String = RecyclerFeedFragment::class.java.simpleName

        private const val NUM_FEED_ITEMS = 20

        val title: String
            get() = "RecyclerView Placement"
    }
}