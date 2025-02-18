package com.k.deeplinkingtesting.inmobi.nativesad.sample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.k.deeplinkingtesting.inmobi.nativesad.sample.listview.ListViewFeedFragment
import com.k.deeplinkingtesting.inmobi.nativesad.sample.recyclerview.RecyclerFeedFragment
import com.k.deeplinkingtesting.inmobi.nativesad.sample.singlestrand.SingleNativeAdFragment
import com.k.deeplinkingtesting.inmobi.nativesad.sample.singlestrand.SingleNativeAdFragment.Companion.title

internal class FragmentAdapter(fm: FragmentManager, tabLayout: TabLayout) :
    FragmentStatePagerAdapter(fm) {
    init {
        for (position in 0 until NUM_TABS) {
            tabLayout.addTab(tabLayout.newTab().setText(getTitle(position)))
        }
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            POSITION_CUSTOM_INTEGRATION -> SingleNativeAdFragment()

            POSITION_LIST_VIEW_INTEGRATION -> ListViewFeedFragment()

            POSITION_RECYCLER_VIEW_INTEGRATION -> RecyclerFeedFragment()

            else -> throw IllegalArgumentException("No fragment for position:$position")
        }
    }

    private fun getTitle(position: Int): String {
        return when (position) {
            POSITION_CUSTOM_INTEGRATION -> title

            POSITION_LIST_VIEW_INTEGRATION -> ListViewFeedFragment.title

            POSITION_RECYCLER_VIEW_INTEGRATION -> RecyclerFeedFragment.title

            else -> throw IllegalArgumentException("No Title for position:$position")
        }
    }

    override fun getCount(): Int {
        return 3
    }

    companion object {
        private const val NUM_TABS = 3

        private const val POSITION_CUSTOM_INTEGRATION = 0

        private const val POSITION_LIST_VIEW_INTEGRATION = 1

        private const val POSITION_RECYCLER_VIEW_INTEGRATION = 2
    }
}
