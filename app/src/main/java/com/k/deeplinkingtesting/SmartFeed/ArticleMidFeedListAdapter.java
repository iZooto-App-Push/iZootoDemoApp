package com.k.deeplinkingtesting.SmartFeed;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.k.deeplinkingtesting.R;
import com.outbrain.OBSDK.SmartFeed.OBSmartFeed;


/**
 * Created by Amit on 05/07/2024.
 */

public class ArticleMidFeedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TEXT_VIEW_TYPE = 1;
    private static final int ARTICLE_HEADER_VIEW_TYPE = 2;

    private static final int RECYCLE_VIEW_SIZE_BEFORE_SMARTFEED = 4;
    private static final int ORIGINAL_RECYCLE_VIEW_SIZE = 8;

    private OBSmartFeed obSmartFeed;

    public void setOBSmartFeed(OBSmartFeed obSmartFeed) {
        this.obSmartFeed = obSmartFeed;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class TextItemViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public View layout;

        public TextItemViewHolder(View v) {
            super(v);
            layout = v;
            textView = (TextView) v.findViewById(R.id.article_text_view);
        }
    }

    public class ImageItemViewHolder extends RecyclerView.ViewHolder {
        public View layout;

        public ImageItemViewHolder(View v) {
            super(v);
            layout = v;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (this.obSmartFeed.isPositionBelongToSmartfeed(position, RECYCLE_VIEW_SIZE_BEFORE_SMARTFEED)) {
            return this.obSmartFeed.getItemViewType(position, RECYCLE_VIEW_SIZE_BEFORE_SMARTFEED);
        }

        final int afterSmartfeedOffset = RECYCLE_VIEW_SIZE_BEFORE_SMARTFEED + this.obSmartFeed.getSmartFeedItemCount();

        // Article content will be displayed "before" and "after" the Smartfeed
        if (position == 0 || position == afterSmartfeedOffset) {
            return ARTICLE_HEADER_VIEW_TYPE;
        }
        else if (position == 1 || position == 1 + afterSmartfeedOffset) {
            return TEXT_VIEW_TYPE;
        }
        else if (position == 2 || position == 2 + afterSmartfeedOffset) {
            return TEXT_VIEW_TYPE;
        }
        else if (position == 3 || position == 3 + afterSmartfeedOffset) {
            return TEXT_VIEW_TYPE;
        }

        return 0;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (this.obSmartFeed.isViewTypeBelongToSmartfeed(viewType)) {
            Log.i("OBSDK", "onCreateViewHolder - viewType: " + viewType);
            return this.obSmartFeed.onCreateViewHolder(parent, viewType);
        }


        View v;
        if (viewType == TEXT_VIEW_TYPE) {
            v = inflater.inflate(R.layout.article_text_row, parent, false);
            TextItemViewHolder tvh = new TextItemViewHolder(v);
            return tvh;
        }
        else {
            // ARTICLE_HEADER_VIEW_TYPE
            v = inflater.inflate(R.layout.article_header_row, parent, false);
            ImageItemViewHolder ivh = new ImageItemViewHolder(v);
            return ivh;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextItemViewHolder tvh;

        if (this.obSmartFeed.isPositionBelongToSmartfeed(position, RECYCLE_VIEW_SIZE_BEFORE_SMARTFEED)) {
            this.obSmartFeed.onBindViewHolder(holder, position, RECYCLE_VIEW_SIZE_BEFORE_SMARTFEED);
            return;
        }

        // Article content will be displayed "before" and "after" the Smartfeed
        if (position == 1 || position == 1 + this.obSmartFeed.getSmartFeedItemCount()) {
            tvh = (TextItemViewHolder)holder;
            tvh.textView.setText(tvh.textView.getContext().getResources().getString(R.string.article_txt_1));
        }
        else if (position == 2 || position == 2 + this.obSmartFeed.getSmartFeedItemCount()) {
            tvh = (TextItemViewHolder)holder;
            tvh.textView.setText(tvh.textView.getContext().getResources().getString(R.string.article_txt_2));
        }
        else if (position == 3 || position == 3 + this.obSmartFeed.getSmartFeedItemCount()) {
            tvh = (TextItemViewHolder)holder;
            tvh.textView.setText(tvh.textView.getContext().getResources().getString(R.string.article_txt_3));
        }
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ORIGINAL_RECYCLE_VIEW_SIZE + this.obSmartFeed.getSmartFeedItemCount();
    }
}