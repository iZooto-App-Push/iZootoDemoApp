package com.k.deeplinkingtesting.VideoWidget;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.k.deeplinkingtesting.CatalogUtils;
import com.k.deeplinkingtesting.R;
import com.k.deeplinkingtesting.SmartFeed.ArticleListAdapter;
import com.outbrain.OBSDK.Entities.OBRecommendation;
import com.outbrain.OBSDK.OBClickListener;
import com.outbrain.OBSDK.Outbrain;
import com.outbrain.OBSDK.VideoWidget.OBVideoWidget;

public class VideoWidgetActivity extends AppCompatActivity implements OBClickListener {
    private RecyclerView recyclerView;
    private ArticleListVideoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OBVideoWidget obVideoWidget;

    private final String OUTBRAIN_WIDGET_ID = "SFD_MAIN_4";
    private final String LOG_TAG = "VideoWidgetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_widget);

        recyclerView = findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        //recyclerView.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_end_color));
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ArticleListVideoAdapter();
        recyclerView.setAdapter(mAdapter);

        obVideoWidget = new OBVideoWidget(
                OUTBRAIN_WIDGET_ID,
                getString(R.string.outbrain_sample_url),
                this
        );


        mAdapter.setOBVideoWidget(obVideoWidget);
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
}
