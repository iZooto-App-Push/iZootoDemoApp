package com.k.deeplinkingtesting.insteam;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.k.deeplinkingtesting.CatalogUtils;
import com.k.deeplinkingtesting.OBViewWizard.OBWidgetListener;
import com.k.deeplinkingtesting.R;
import com.outbrain.OBSDK.Entities.OBRecommendation;
import com.outbrain.OBSDK.Entities.OBRecommendationsResponse;
import com.outbrain.OBSDK.FetchRecommendations.OBRequest;
import com.outbrain.OBSDK.FetchRecommendations.RecommendationsListener;
import com.outbrain.OBSDK.Outbrain;


import java.util.ArrayList;


public class SwipableActivity extends AppCompatActivity implements RecommendationsListener, OBWidgetListener {
    private static final int REQUESTS_COUNT = 4;
    private SwipeableListViewAdapter swipeableListViewAdapter;

    @Override
    public void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.outbrain_single_list_view_activity);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("InStream Widget List");
        }





        ListView listView = findViewById(R.id.outbrain_main_list_view);

        swipeableListViewAdapter = new SwipeableListViewAdapter(this, this);
        listView.setAdapter(swipeableListViewAdapter);

        fetchRecommendations();
    }

    private void fetchRecommendations() {
        for (int i = 0; i < REQUESTS_COUNT; i++) {
            OBRequest request = new OBRequest();
            request.setUrl(getString(R.string.outbrain_sample_url));
            request.setWidgetId("APP_1");//SDK_1
            request.setWidgetIndex(i);
            Outbrain.fetchRecommendations(request, this);
        }
    }

    @Override
    public void onOutbrainRecommendationsSuccess(final OBRecommendationsResponse recommendations) {
        swipeableListViewAdapter.addRecommendations(recommendations);
        swipeableListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOutbrainRecommendationsFailure(Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onRecommendationClick(OBRecommendation recommendation) {
        openRecommendationInBrowser(recommendation);
    }

    @Override
    public void onAdChoicesClick(String url) {
        CatalogUtils.openURLInBrowser(url, this);
    }

    private void openRecommendationInBrowser(OBRecommendation obRecommendation) {
        CatalogUtils.openURLInBrowser(Outbrain.getUrl(obRecommendation), this);
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

class SwipeableListViewAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<OBRecommendationsResponse> recommendationsResponses = new ArrayList<OBRecommendationsResponse>();
    private final OBWidgetListener obViewListener;

    public SwipeableListViewAdapter(Context context, OBWidgetListener obViewListener) {
        this.context = context;
        this.obViewListener = obViewListener;
    }

    @Override
    public int getCount() {
        return recommendationsResponses.size();
    }

    @Override
    public Object getItem(int i) {
        return recommendationsResponses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View returnView = view;
        if (view == null) {
            returnView = LayoutInflater.from(context).inflate(R.layout.swipable_cell, null);
        }
        OBRecommendationsResponse item = (OBRecommendationsResponse) getItem(i);
        SwipableCellViewPager cell = returnView.findViewById(R.id.swipable_cell);
        cell.update(item, obViewListener);
        return returnView;
    }

    public void addRecommendations(OBRecommendationsResponse recs) {
        recommendationsResponses.add(recs);
    }

}



