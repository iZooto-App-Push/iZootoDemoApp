package com.k.deeplinkingtesting.SFWebView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.k.deeplinkingtesting.R;
import com.outbrain.OBSDK.SFWebView.SFWebViewClickListener;
import com.outbrain.OBSDK.SFWebView.SFWebViewEventsListener;
import com.outbrain.OBSDK.SFWebView.SFWebViewHeightDelegate;
import com.outbrain.OBSDK.SFWebView.SFWebViewWidget;

import org.json.JSONObject;


public class SFWebViewScrollViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SFWebViewClickListener, SFWebViewHeightDelegate {

    private final String LOG_TAG = "SFWebViewScrollViewActivity";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SFWebViewWidget mSFRegularWidget;
    private SFWebViewWidget mSFSmartfeedWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfwidget_scrollview);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("SFWebViewScrollViewActivity List");
           }
        this.mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSFRegularWidget = findViewById(R.id.sf_regular_widget);
        mSFSmartfeedWidget = findViewById(R.id.sf_smartfeed_widget);
        SFWebViewWidget.setHeightDelegateWeakReference(this);

        final ScrollView scrollView = findViewById(R.id.scroll_view);

        mSFSmartfeedWidget.setSfWebViewEventsListener(new SFWebViewEventsListener() {
            @Override
            public void onWidgetEvent(String eventName, JSONObject additionalData) {
                Log.i("ScrollViewActivity", "onWidgetEvent: " + eventName);
                Log.i("ScrollViewActivity", "onWidgetEvent: " + additionalData);
            }
        });

        SFWebViewWidget.isWidgetEventsEnabled = true;
        mSFRegularWidget.init(scrollView, "http://mobile-demo.outbrain.com");
        mSFSmartfeedWidget.init(scrollView, "http://mobile-demo.outbrain.com");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mSFRegularWidget.onActivityConfigurationChanged();
        mSFSmartfeedWidget.onActivityConfigurationChanged();
    }

    @Override
    public void onOrganicClick(String url) {
        // internal navigation in your app
        Log.i(LOG_TAG, "onOrganicClick: " + url);
    }

    @Override
    public void onRefresh() {
        final SFWebViewWidget sfRegularWidget = findViewById(R.id.sf_regular_widget);
        final SFWebViewWidget sfSmartfeedWidget = findViewById(R.id.sf_smartfeed_widget);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sfRegularWidget.reload();
                sfSmartfeedWidget.reload();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public int bottomPaddingForWidget(String widgetId) {
        if (widgetId.equals("MB_2")) {
            return 0;
        }
        return 100;
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

