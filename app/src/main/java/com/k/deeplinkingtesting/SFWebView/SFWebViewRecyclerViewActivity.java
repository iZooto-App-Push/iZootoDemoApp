package com.k.deeplinkingtesting.SFWebView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.common.eventbus.Subscribe;
import com.k.deeplinkingtesting.R;
import com.outbrain.OBSDK.Entities.OBRecommendationsResponse;
import com.outbrain.OBSDK.FetchRecommendations.OBRequest;
import com.outbrain.OBSDK.FetchRecommendations.RecommendationsListener;
import com.outbrain.OBSDK.Outbrain;
import com.outbrain.OBSDK.SFWebView.OutbrainBusProvider;
import com.outbrain.OBSDK.SFWebView.SFWebViewClickListener;
import com.outbrain.OBSDK.SFWebView.SFWebViewWidget;


public class SFWebViewRecyclerViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SFWebViewClickListener {

    private final String LOG_TAG = "RecyclerViewActivity";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SFWebViewWidget mSFRegularWidget;
    private SFWebViewWidget mSFSmartfeedWidget;
    private boolean twoWidgetsOnPage = false;
    private boolean isSmartLogic = false;
    //private  String LIVE_URL = "https://mv.outbrain.com/Multivac/api/platforms";
    private String LIVE_URL = "http://mobile-demo.outbrain.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfwidget_recyclerview);
       // OutbrainBusProvider.getInstance().register(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("RecyclerView Feed List");
        }
        this.mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        twoWidgetsOnPage = getIntent().getBooleanExtra("twoWidgets", false);
        isSmartLogic = getIntent().getBooleanExtra("isSmartLogic", false);

        if (twoWidgetsOnPage) {//http://mobile-demo.outbrain.com
            mSFRegularWidget = new SFWebViewWidget(
                    recyclerView,
                    LIVE_URL,
                    "MB_2",
                    "NANOWDGT01"
            );
            mSFSmartfeedWidget = new SFWebViewWidget(
                    recyclerView,
                    LIVE_URL,
                    "MB_1",
                    1,
                    "NANOWDGT01",
                    null,
                    false);

        }
        else {
            mSFSmartfeedWidget = new SFWebViewWidget(
                    recyclerView,
                    LIVE_URL,
                    isSmartLogic ? "MB_3" : "MB_1",
                    0,
                    "NANOWDGT01",
                    null,
                    false);
        }

        SFWebViewExampleAdapter listAdapter = new SFWebViewExampleAdapter();
        if (twoWidgetsOnPage) {
            listAdapter.setSFRegularWidget(mSFRegularWidget);
        }

        listAdapter.setSFSmartfeedWidget(mSFSmartfeedWidget);
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // OutbrainBusProvider.getInstance().unregister(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (twoWidgetsOnPage) {
            mSFRegularWidget.onActivityConfigurationChanged();
        }

        mSFSmartfeedWidget.onActivityConfigurationChanged();
    }

    @Override
    public void onRefresh() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (twoWidgetsOnPage) {
                    mSFRegularWidget.reload();
                }

                mSFSmartfeedWidget.reload();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onOrganicClick(String url) {
        // internal navigation in your app
        Log.i(LOG_TAG, "onOrganicClick: " + url);
    }

    @Subscribe
    public void receivedBridgeRecsReceivedEvent(OutbrainBusProvider.BridgeRecsReceivedEvent event) {
        Log.i(LOG_TAG, "receivedBridgeRecsReceivedEvent: " + event.getWidgetID());
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
