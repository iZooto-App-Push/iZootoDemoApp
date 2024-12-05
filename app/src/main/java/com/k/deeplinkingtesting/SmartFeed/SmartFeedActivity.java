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

import okhttp3.Request;

/**
 * Created by Amit on 05/07/2024.
 */
public class SmartFeedActivity extends AppCompatActivity implements OBSmartFeedListener, OBSmartFeedAdvancedListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private ArticleListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private OBSmartFeed obSmartFeed;
    private final String OUTBRAIN_WIDGET_ID = "APP_4";//SFD_MAIN_2
//    private final String OUTBRAIN_WIDGET_ID = "SDK_MAIN_1"; // to test trending in category
    private final String OUTBRAIN_WIDGET_ID_TABLET = "SFD_MAIN_3";
    private final String LOG_TAG = "SmartFeedActivity";
    private String widgetID = OUTBRAIN_WIDGET_ID;
    private boolean darkMode = false;
    private boolean readMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_feed);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Show Smart List");
        }
        recyclerView = findViewById(R.id.my_recycler_view);


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        //recyclerView.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_end_color));
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ArticleListAdapter();
        recyclerView.setAdapter(mAdapter);

        // SwipeRefreshLayout
        this.mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);

//        // change widgetID for tablet
//        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
//        if (isTablet) {
//            widgetID = OUTBRAIN_WIDGET_ID_TABLET;
//        }

        this.obSmartFeed = new OBSmartFeed("https://mv.outbrain.com/Multivac/api/platforms", widgetID, recyclerView, this);

        this.obSmartFeed.setAdvancedListener(this);
        this.obSmartFeed.setDarkMode(darkMode);
//        this.obSmartFeed.defaultMarginInGrid = 15.0f;

        // Read More module
        if (this.readMore) {
            this.obSmartFeed.setReadMoreModule(1, 100);
            this.obSmartFeed.setReadMoreModuleGradientViewHeightPx(500);
        }

        // Custom UI for Read More module
        // this.obSmartFeed.addCustomUI(SFItemData.SFItemType.SF_READ_MORE_ITEM, R.layout.outbrain_sfeed_read_more_item_custom);

        // Custom UI
        // this.obSmartFeed.addCustomUI(SFItemData.SFItemType.SINGLE_ITEM, R.layout.outbrain_sfeed_single_item_custom);
        // this.obSmartFeed.addCustomUI(SFItemData.SFItemType.GRID_TWO_ITEMS_IN_LINE, R.layout.outbrain_sfeed_single_rec_in_line_custom);

        mAdapter.setOBSmartFeed(obSmartFeed);
        mAdapter.setDarkMode(darkMode);
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
      //  String aboutOutbrainUrl = Outbrain.getOutbrainAboutURL();
       // CatalogUtils.openURLInBrowser(aboutOutbrainUrl, this);
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
        // Empty implementation if Smartfeed is not in the middle of the screen.
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        final Handler handler = new Handler();
        final SmartFeedActivity weakSelf = this;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                weakSelf.obSmartFeed = new OBSmartFeed(weakSelf.getString(R.string.outbrain_sample_url), widgetID, recyclerView, weakSelf);
                weakSelf.obSmartFeed.setDarkMode(darkMode);
                mAdapter.setOBSmartFeed(obSmartFeed);
                mAdapter.setDarkMode(darkMode);
                if (readMore) {
                    weakSelf.obSmartFeed.setReadMoreModule(1, 100);
                    weakSelf.obSmartFeed.setReadMoreModuleGradientViewHeightPx(500);
                }
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
