package com.k.deeplinkingtesting.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.k.deeplinkingtesting.R;
import com.outbrain.OBSDK.Entities.OBRecommendation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GridAdapter extends ArrayAdapter<OBRecommendation> implements GridViewMeasurableItemsAdapter {
    Context context;
    int layoutResourceId;
    ArrayList<OBRecommendation> data = new ArrayList<OBRecommendation>();

    public GridAdapter(Context context, int layoutResourceId,
                       ArrayList<OBRecommendation> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GridAdapter.RecordHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = row.findViewById(R.id.outbrain_grid_layouts_container_title_text_label);
            holder.descriptionTitle = row.findViewById(R.id.outbrain_grid_layouts_container_description_text_label);
            holder.imageItem = row.findViewById(R.id.outbrain_grid_layouts_container_image_view);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        OBRecommendation item = data.get(position);
        holder.txtTitle.setText(item.getContent());
        holder.descriptionTitle.setText("(" + item.getSourceName()+ ")");
        if (holder.imageItem != null) {
            Picasso.get().load(item.getThumbnail().getUrl()).into(holder.imageItem);
        }
        return row;

    }

    public void measureItems(int columnWidth) {
        // Obtain system inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Inflate temp layout object for measuring
        GridViewItemLayout itemView = (GridViewItemLayout)inflater.inflate(layoutResourceId, null);

        // Create measuring specs
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(columnWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        // Loop through each data object
        for(int index = 0; index < data.size(); index++) {

            // Set position and data
            itemView.setPosition(index);

            // Force measuring
            itemView.requestLayout();
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    static class RecordHolder {
        TextView txtTitle;
        TextView descriptionTitle;
        ImageView imageItem;
    }
}
