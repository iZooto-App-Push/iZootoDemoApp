package com.k.deeplinkingtesting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.outbrain.OBSDK.Entities.OBRecommendation;
import com.outbrain.OBSDK.Outbrain;
import com.outbrain.OBSDK.SFWebView.OutbrainBusProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OutbrainSingleItemLayoutAdapter extends ArrayAdapter<OBRecommendation> {
    private final ArrayList<OBRecommendation> items;
    private final int layoutResource;
    private final Context context;
    private int itemsLimitCount = 0;

    public void setListItemsCount(int itemsLimitCount) {
        this.itemsLimitCount = itemsLimitCount;
        notifyDataSetChanged();
    }

    public OutbrainSingleItemLayoutAdapter(Context context, int itemLayoutResourceId, ArrayList<OBRecommendation> items) {
        super(context, itemLayoutResourceId);
        this.items = items;
        this.layoutResource = itemLayoutResourceId;
        this.context = context;
       // OutbrainBusProvider.getInstance().register(this);
    }

    @Override
    public int getCount() {
        return itemsLimitCount == 0 ? items.size() : itemsLimitCount;
    }

    @Override
    public OBRecommendation getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResource, null);
        ViewGroup outbrainRecContainer = view.findViewById(R.id.outbrain_rec_container);
        TextView title = view.findViewById(R.id.outbrain_layouts_title_text_label);
        TextView desc = view.findViewById(R.id.outbrain_layouts_author_text_label);
        ImageView recImageView = view.findViewById(R.id.outbrain_rec_image_view);
        ImageView disclosureImageView = view.findViewById(R.id.outbrain_rec_disclosure_image_view);
        final Context ctx = OutbrainSingleItemLayoutAdapter.this.context;
        final OBRecommendation rec = getItem(position);
        title.setText(rec.getContent());
        desc.setText("(" + rec.getSourceName() + ")");

        if (recImageView == null) {
            return view;
        }

        if (rec.isPaid() && rec.shouldDisplayDisclosureIcon()) {
            // Set the RTB disclosure icon image and click handler
            disclosureImageView.setVisibility(View.VISIBLE);
            Picasso.get().load(rec.getDisclosure().getIconUrl()).into(disclosureImageView);
            disclosureImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CatalogUtils.openURLInBrowser(rec.getDisclosure().getClickUrl(), ctx);
                }
            });
        }
        else {
            disclosureImageView.setVisibility(View.GONE);
        }

        Picasso.get().load(rec.getThumbnail().getUrl()).into(recImageView);
        Outbrain.configureViewabilityPerListingFor(outbrainRecContainer, rec);
        return view;
    }

    @Subscribe
    public void receivedViewabilityFiredEvent(OutbrainBusProvider.ViewabilityFiredEvent viewabilityFiredEvent) {
        Log.i("OBSDK_APP", "receivedViewabilityFiredEvent: reqId: " + viewabilityFiredEvent.getRequestId());
        Log.i("OBSDK_APP", "receivedViewabilityFiredEvent: position: " + viewabilityFiredEvent.getPosition());
    }
}
