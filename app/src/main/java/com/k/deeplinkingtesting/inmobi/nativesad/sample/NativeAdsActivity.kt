package com.k.deeplinkingtesting.inmobi.nativesad.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.k.deeplinkingtesting.R
import java.util.Objects

class NativeAdsActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ads)

        try {
            val tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
            tabLayout.tabGravity = TabLayout.GRAVITY_FILL
            val viewPager = findViewById<View>(R.id.pager) as ViewPager
            val adapter = FragmentAdapter(supportFragmentManager, tabLayout)
            viewPager.adapter = adapter
            viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
            tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                }
            })

            val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
            setSupportActionBar(toolbar)
            Objects.requireNonNull(supportActionBar)
                ?.setDisplayHomeAsUpEnabled(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}