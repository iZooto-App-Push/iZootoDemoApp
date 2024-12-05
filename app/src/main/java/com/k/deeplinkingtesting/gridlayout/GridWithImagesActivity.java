package com.k.deeplinkingtesting.gridlayout;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.k.deeplinkingtesting.CatalogUtils;
import com.k.deeplinkingtesting.R;
import com.k.deeplinkingtesting.utils.AutoMeasureGridView;
import com.k.deeplinkingtesting.utils.GridAdapter;
import com.outbrain.OBSDK.Entities.OBRecommendation;
import com.outbrain.OBSDK.Entities.OBRecommendationsResponse;
import com.outbrain.OBSDK.FetchRecommendations.MultivacListener;
import com.outbrain.OBSDK.FetchRecommendations.OBRequest;
import com.outbrain.OBSDK.FetchRecommendations.RecommendationsListener;
import com.outbrain.OBSDK.Outbrain;
import com.outbrain.OBSDK.OutbrainService;

import java.util.ArrayList;


public class GridWithImagesActivity extends AppCompatActivity implements RecommendationsListener, MultivacListener {
    private AutoMeasureGridView gridView;

    @Override
    public void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.outbrain_grid_images_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Show Grid List");
        }
        gridView = findViewById(R.id.outbrain_grid_layouts_grid_view);
        fetchRecommendations();
    }

    private void fetchRecommendations() {
        OBRequest request = new OBRequest();
        request.setWidgetId("SDF_MAIN_1");
        request.setUrl(getString(R.string.outbrain_sample_url));

        Outbrain.fetchRecommendations(request, this);
        OutbrainService.getInstance().fetchMultivac(request,this);

//        Outbrain.fetchRecommendations(request, new RecommendationsListener() {
//            @Override
//            public void onOutbrainRecommendationsSuccess(OBRecommendationsResponse recommendations) {
//                int numRecs = recommendations.getAll().size();
//
//                for (int i = 0; i < numRecs; i++)
//                {
//                    OBRecommendation rec = recommendations.get(i);
//                    Log.e("Data",rec.getContent());
//                    Log.e("Data",""+rec.getThumbnail());
//                    Log.e("Data",rec.getSourceName());
//                    Log.e("Data",""+rec.isPaid());
//
//                    // displaying the recommendation
//
//                }            }
//
//            @Override
//            public void onOutbrainRecommendationsFailure(Exception ex) {
//                //Handle failure (write to log, hide the UI component, etc...)
//            }
//        });

    }

    @Override
    public void onOutbrainRecommendationsSuccess(final OBRecommendationsResponse recommendations) {
        populateGridView(recommendations);
    }

    @Override
    public void onOutbrainRecommendationsFailure(Exception ex) {
        ex.printStackTrace();
    }

    private void populateGridView(final OBRecommendationsResponse recommendations) {
        GridAdapter adapter = new GridAdapter(this, R.layout.grid_with_image, recommendations.getAll());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openRecommendationInBrowser(recommendations.get(i));
            }
        });
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

    @Override
    public void onMultivacSuccess(ArrayList<OBRecommendationsResponse> cardsResponseList, int feedIdx, boolean hasMore) {
        Log.e("Response",""+cardsResponseList.size());
    }

    @Override
    public void onMultivacFailure(Exception ex) {
        Log.e("Response",""+ex.toString());

    }
}


