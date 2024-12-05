package com.k.deeplinkingtesting.SmartFeed;

import android.os.Handler;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.k.deeplinkingtesting.CatalogUtils;
import com.k.deeplinkingtesting.R;
import com.outbrain.OBSDK.Entities.OBRecommendation;
import com.outbrain.OBSDK.Outbrain;
import com.outbrain.OBSDK.SmartFeed.OBSmartFeed;
import com.outbrain.OBSDK.SmartFeed.OBSmartFeedAdvancedListener;
import com.outbrain.OBSDK.SmartFeed.OBSmartFeedListener;

import java.util.ArrayList;

/**
 * Created by Amit on 05/07/2024.
 */
public class SmartFeedMidFeedActivity extends AppCompatActivity implements OBSmartFeedListener, OBSmartFeedAdvancedListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private ArticleMidFeedListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private OBSmartFeed obSmartFeed;
    private final String OUTBRAIN_WIDGET_ID = "SFD_MAIN_1";
    private final String OUTBRAIN_WIDGET_ID_TABLET = "SFD_MAIN_3";
    private final String LOG_TAG = "SmartFeedActivity";
    private String widgetID = OUTBRAIN_WIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_feed);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Show Smart Mid List");
        }

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        //recyclerView.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_end_color));
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ArticleMidFeedListAdapter();
        recyclerView.setAdapter(mAdapter);

        // SwipeRefreshLayout
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);

        // change widgetID for tablet
//        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
//        if (isTablet) {
//            widgetID = OUTBRAIN_WIDGET_ID_TABLET;
//        }

        this.obSmartFeed = new OBSmartFeed(getString(R.string.outbrain_sample_url), widgetID, recyclerView, this);
        this.obSmartFeed.setAdvancedListener(this);
        this.obSmartFeed.isInMiddleOfRecycleView = true;
        mAdapter.setOBSmartFeed(obSmartFeed);
        this.obSmartFeed.start();
    }

    @Override
    public void userTappedOnRecommendation(OBRecommendation rec) {
        Log.i(LOG_TAG, "userTappedOnRecommendation: " + rec.getContent());
        String url = Outbrain.getUrl(rec);
        CatalogUtils.openURLInBrowser(url, this);
    }

    @Override
    public void userTappedOnAdChoicesIcon(String url) {
        Log.i(LOG_TAG, "userTappedOnAdChoicesIcon: " + url);
        CatalogUtils.openURLInBrowser(url, this);
    }

    @Override
    public void userTappedOnAboutOutbrain() {
        String aboutOutbrainUrl = Outbrain.getOutbrainAboutURL(this);
        CatalogUtils.openURLInBrowser(aboutOutbrainUrl, this);
    }

    @Override
    public void userTappedOnVideo(String url) {
        Log.i(LOG_TAG, "userTappedOnVideo: " + url);
        CatalogUtils.openURLInBrowser(url, this);
    }

    // OBSmartFeedAdvancedListener methods
    @Override
    public void onOutbrainRecsReceived(ArrayList<OBRecommendation> recommendations, String widgetId) {
        Log.i(LOG_TAG, "onOutbrainRecsReceived for widget id: " + widgetId);
    }

    @Override
    public boolean isVideoCurrentlyPlaying() {
        // Your logic here
        return false;
    }

    @Override
    public void smartfeedIsReadyWithRecs() {
        this.mAdapter.notifyDataSetChanged();
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        final Handler handler = new Handler();
        final SmartFeedMidFeedActivity weakSelf = this;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                weakSelf.obSmartFeed = new OBSmartFeed(weakSelf.getString(R.string.outbrain_sample_url), widgetID, recyclerView, weakSelf);
                mAdapter.setOBSmartFeed(obSmartFeed);
                weakSelf.obSmartFeed.isInMiddleOfRecycleView = true;
                weakSelf.obSmartFeed.setAdvancedListener(weakSelf);
                weakSelf.obSmartFeed.start();
                mAdapter.notifyDataSetChanged();
                weakSelf.mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                // Perform your custom action here
                // For example, show a dialog or navigate to a specific activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        // Your custom code here

        // If you want to use the default back button behavior as well
        super.onBackPressed();
    }
}
