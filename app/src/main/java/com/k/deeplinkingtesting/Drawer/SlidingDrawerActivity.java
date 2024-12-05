package com.k.deeplinkingtesting.Drawer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.k.deeplinkingtesting.CatalogUtils;
import com.k.deeplinkingtesting.OutbrainSingleItemLayoutAdapter;
import com.k.deeplinkingtesting.R;
import com.k.deeplinkingtesting.utils.ArrowDrawView;
import com.outbrain.OBSDK.Entities.OBRecommendation;
import com.outbrain.OBSDK.Entities.OBRecommendationsResponse;
import com.outbrain.OBSDK.FetchRecommendations.OBRequest;
import com.outbrain.OBSDK.FetchRecommendations.RecommendationsListener;
import com.outbrain.OBSDK.Outbrain;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

public class SlidingDrawerActivity extends AppCompatActivity implements RecommendationsListener, SlidingUpPanelLayout.PanelSlideListener, ScrollViewListener {
    private static float cellSize;
    private static float headerCellSize;
    private static final int NUM_OF_RECS = 3;
    private static float drawerHeight;
    private static float drawerAnchor;
    private static final int SCROLL_UP_THRESHOLD = 5;

    private boolean didSeeBottomRecs;
    private SlidingUpPanelLayout slidingDrawer;
    private ListView fullListView;
    private ListenableScrollView mainScrollView;
    private TextView articleTextView;
    private LinearLayout classicRecommendationsContainer;
    private String widgetId;
    private String url;
    private ArrowDrawView drawerArrowImage;
    private OutbrainSingleItemLayoutAdapter listAdapter;

    private void fetchRecommendations() {
        OBRequest request = new OBRequest();
        request.setUrl(this.url);
        request.setWidgetId(this.widgetId);
        Outbrain.fetchRecommendations(request, this);

        // 2nd widget
        OBRequest request2 = new OBRequest();
        request2.setUrl(this.url);
        request2.setWidgetId(this.widgetId);
        request2.setWidgetIndex(1);
        Outbrain.fetchRecommendations(request2, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.widgetId = "SDK_3";//SDK/_3
        this.url = getString(R.string.outbrain_sample_url);
        setContentView(R.layout.sliding_drawer_activity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Show Drawer List");
        }
        bindLayout();
        setupDrawer();
        setupScrollView();
        fetchRecommendations();
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

    private void setupScrollView() {
        mainScrollView.setScrollViewListener(this);
    }

    private void setupDrawer() {
        RelativeLayout outbrainDrawerHeaderLayout = findViewById(R.id.drawer_header_wrapper);
        slidingDrawer.setDragView(outbrainDrawerHeaderLayout);
        slidingDrawer.setPanelSlideListener(this);
        slidingDrawer.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        ImageView whatIsOutbrainIV= outbrainDrawerHeaderLayout.findViewById(R.id.recommended_by_image);
        whatIsOutbrainIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchWhatIs();
            }
        });
    }

    private void launchWhatIs() {
        slidingDrawer.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        String aboutOutbrainUrl = Outbrain.getOutbrainAboutURL();
        CatalogUtils.openURLInBrowser(aboutOutbrainUrl, this);
    }

    private void bindLayout() {
        classicRecommendationsContainer = findViewById(R.id.classicRecommendationsContainer);
        slidingDrawer = findViewById(R.id.sliding_drawer);
        fullListView = findViewById(R.id.drawer_list_view);
        mainScrollView = findViewById(R.id.drawer_main_scroll_view);
        articleTextView = findViewById(R.id.article_text_view);
        drawerArrowImage = findViewById(R.id.drawer_arrow_image);
    }

    @Override
    public void onOutbrainRecommendationsSuccess(final OBRecommendationsResponse recommendationsResponse) {
        if (recommendationsResponse.getRequest().getIdx().equals("0")) {
            setRecommendationsInDrawer(recommendationsResponse);
        }
        else {
            setRecommendationsInFooter(recommendationsResponse);
        }
    }

    @Override
    public void onOutbrainRecommendationsFailure(Exception ex) {
        ex.printStackTrace();
    }

    public void setRecommendationsInDrawer(final OBRecommendationsResponse recs) {
        LayoutInflater inflater = getLayoutInflater();
        if (recs.getAll().size() > 0) {
            View child = inflater.inflate(R.layout.outbrain_classic_recommendation_header_view, null);
            classicRecommendationsContainer.addView(child);

            ImageView whatIsOutbrainIV= child.findViewById(R.id.recommended_by_image);
            whatIsOutbrainIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchWhatIs();
                }
            });
        }

        for (final OBRecommendation item : recs.getAll()) {
            addClassicRecommendationView(item, inflater);
        }

    }
    public void setRecommendationsInFooter(final OBRecommendationsResponse recs) {
        listAdapter = new OutbrainSingleItemLayoutAdapter(this, R.layout.outbrain_drawer_list_item, recs.getAll());
        fullListView.setAdapter(listAdapter);
        fullListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openRecommendationInBrowser(recs.get(i));
            }
        });
        listAdapter.notifyDataSetChanged();
    }

    public void onBackPressed(){
        if (slidingDrawer.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingDrawer.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        else {
            super.onBackPressed();
        }
    }

    private void addClassicRecommendationView(final OBRecommendation recommendation, LayoutInflater inflater) {
        View child = inflater.inflate(R.layout.drawer_recommendation, null);

        ViewGroup recContainer = child.findViewById(R.id.outbrain_rec_container);
        TextView title = child.findViewById(R.id.outbrain_layouts_title_text_label);
        TextView desc = child.findViewById(R.id.outbrain_layouts_author_text_label);
        ImageView imageView = child.findViewById(R.id.outbrain_rec_image_view);
        ImageView disclosureImageView = child.findViewById(R.id.outbrain_rec_disclosure_image_view);

        if (recommendation.isPaid() && recommendation.shouldDisplayDisclosureIcon()) {
            // Set the RTB disclosure icon image and click handler
            disclosureImageView.setVisibility(View.VISIBLE);
            Picasso.get().load(recommendation.getDisclosure().getIconUrl()).into(disclosureImageView);
            disclosureImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CatalogUtils.openURLInBrowser(recommendation.getDisclosure().getClickUrl(), SlidingDrawerActivity.this);
                }
            });
        }
        else {
            disclosureImageView.setVisibility(View.GONE);
        }

        RelativeLayout container = child.findViewById(R.id.outbrain_classic_recommendation_view_container);

        title.setText(recommendation.getContent());
        String text = String.format(getResources().getString(R.string.outbrain_source_name), recommendation.getSourceName());
        desc.setText(text);

        classicRecommendationsContainer.addView(child);

        //Load image
        if (imageView != null) {
            Picasso.get().load(recommendation.getThumbnail().getUrl()).into(imageView);
        }
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecommendationInBrowser(recommendation);
            }
        });

        // Viewability setup
        Outbrain.configureViewabilityPerListingFor(recContainer, recommendation);
    }

    private void openRecommendationInBrowser(OBRecommendation obRecommendation) {
        CatalogUtils.openURLInBrowser(Outbrain.getUrl(obRecommendation), this);
    }

    @Override
    public void onPanelSlide(View view, float v) {
        drawerArrowImage.setOpenPercentage(1.0f - v);
        drawerArrowImage.invalidate();
    }

    @Override
    public void onPanelCollapsed(View panel) {
        if (listAdapter != null) {
            listAdapter.setListItemsCount(1);
        }
        enableScrolling();
    }

    @Override
    public void onPanelExpanded(View panel) {
        if (listAdapter != null) {
            listAdapter.setListItemsCount(0);
        }
        disableScrolling();
    }

    @Override
    public void onPanelAnchored(View panel) {
        if (listAdapter != null) {
            listAdapter.setListItemsCount(0);
        }
        disableScrolling();
    }

    @Override
    public void onPanelHidden(View view) {

    }

    private void disableScrolling() {
        mainScrollView.setScrollingEnabled(false);
        mainScrollView.performClick();
    }

    private void enableScrolling() {
        mainScrollView.setScrollingEnabled(true);
    }

    @Override
    public void onScrollChanged(ListenableScrollView scrollView, int x, int y, ListenableScrollView.ScrollDirection direction, int delta, boolean endReached) {
        if (!didSeeBottomRecs) {
            float scrollViewBottom = scrollView.getMeasuredHeight() + y;
            if (scrollViewBottom >= articleTextView.getBottom()) {
                didSeeBottomRecs = true;
                slidingDrawer.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
            if ((direction == ListenableScrollView.ScrollDirection.UP && delta > SCROLL_UP_THRESHOLD)) {
                slidingDrawer.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }
    }

}
