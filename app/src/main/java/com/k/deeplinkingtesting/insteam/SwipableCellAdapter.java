package com.k.deeplinkingtesting.insteam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.k.deeplinkingtesting.OBViewWizard.OBWidgetListener;
import com.k.deeplinkingtesting.R;
import com.outbrain.OBSDK.Entities.OBRecommendation;
import com.outbrain.OBSDK.Entities.OBRecommendationsResponse;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SwipableCellAdapter extends PagerAdapter {
    private OBRecommendationsResponse recommendationsResponse;
    private OBWidgetListener obListener;
    private Context context;
    ArrayList<ViewGroup> views = new ArrayList<ViewGroup>();

    public SwipableCellAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final OBRecommendation rec = recommendationsResponse.get(position);
        ViewGroup rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.swipable_page_item, null);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obListener.onRecommendationClick(rec);
            }
        });
        TextView titleView = rootView.findViewById(R.id.outbrain_layouts_title_text_label);
        TextView descriptionView = rootView.findViewById(R.id.outbrain_layouts_author_text_label);

        ImageView imageView = rootView.findViewById(R.id.outbrain_rec_image_view);
        ImageView disclosureImageView = rootView.findViewById(R.id.outbrain_rec_disclosure_image_view);

        titleView.setText(rec.getContent());
        descriptionView.setText("(" + rec.getSourceName() + ")");
        Picasso.get().load(rec.getThumbnail().getUrl()).into(imageView);
        container.addView(rootView);

        if (rec.isPaid() && rec.shouldDisplayDisclosureIcon()) {
            // Set the RTB disclosure icon image and click handler
            disclosureImageView.setVisibility(View.VISIBLE);
            Picasso.get().load(rec.getDisclosure().getIconUrl()).into(disclosureImageView);
            disclosureImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    obListener.onAdChoicesClick(rec.getDisclosure().getClickUrl());
                }
            });
        }
        else {
            disclosureImageView.setVisibility(View.GONE);
        }

        views.add(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public ViewGroup getItem(int index) {
        return views.get(index);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return recommendationsResponse == null ? 0 : recommendationsResponse.getAll().size();
    }

    public void setRecommendationsResponse(OBRecommendationsResponse recommendationsResponse) {
        this.recommendationsResponse = recommendationsResponse;
    }

    public void setObListener(OBWidgetListener obListener) {
        this.obListener = obListener;
    }
}
