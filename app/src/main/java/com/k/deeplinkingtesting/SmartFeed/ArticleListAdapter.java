package com.k.deeplinkingtesting.SmartFeed;


import android.graphics.Color;
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

public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TEXT_VIEW_TYPE = 1;
    private static final int ARTICLE_HEADER_VIEW_TYPE = 2;

    private static final int ORIGINAL_RECYCLE_VIEW_SIZE = 4;

    private OBSmartFeed obSmartFeed;
    private boolean darkMode = false;

    public void setOBSmartFeed(OBSmartFeed obSmartFeed) {
        this.obSmartFeed = obSmartFeed;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    private int getBackgroundColor() {
        return darkMode ? Color.BLACK : Color.WHITE;
    }

    private int getTextColor() {
        return darkMode ? Color.WHITE : Color.BLACK;
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
            layout.setBackgroundColor(getBackgroundColor());
            textView = v.findViewById(R.id.article_text_view);
            textView.setBackgroundColor(getBackgroundColor());
            textView.setTextColor(getTextColor());
        }
    }

    public class ImageItemViewHolder extends RecyclerView.ViewHolder {
        public View layout;

        public ImageItemViewHolder(View v) {
            super(v);
            layout = v;
            layout.setBackgroundColor(getBackgroundColor());
            v.findViewById(R.id.boxes_wrapper_ll).setBackgroundColor(getBackgroundColor());
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ARTICLE_HEADER_VIEW_TYPE;
            case 1:
            case 2:
            case 3:
                return TEXT_VIEW_TYPE;
            default:
                return this.obSmartFeed.getItemViewType(position, ORIGINAL_RECYCLE_VIEW_SIZE);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case TEXT_VIEW_TYPE:
                v = inflater.inflate(R.layout.article_text_row, parent, false);
                TextItemViewHolder tvh = new TextItemViewHolder(v);
                return tvh;
            case ARTICLE_HEADER_VIEW_TYPE:
                v = inflater.inflate(R.layout.article_header_row, parent, false);
                ImageItemViewHolder ivh = new ImageItemViewHolder(v);
                return ivh;
            default:
                return this.obSmartFeed.onCreateViewHolder(parent, viewType);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextItemViewHolder tvh;

        switch (position) {
            case 0:
                break;
            case 1:
                tvh = (TextItemViewHolder)holder;
                tvh.textView.setText(tvh.textView.getContext().getResources().getString(R.string.article_txt_1));
                break;
            case 2:
                tvh = (TextItemViewHolder)holder;
                tvh.textView.setText(tvh.textView.getContext().getResources().getString(R.string.article_txt_2));
                break;
            case 3:
                tvh = (TextItemViewHolder)holder;
                tvh.textView.setText(tvh.textView.getContext().getResources().getString(R.string.article_txt_3));
                break;
            default:
                break;
        }
        // always call to obSmartFeed.onBindViewHolder() to support Read More module
        this.obSmartFeed.onBindViewHolder(holder, position, ORIGINAL_RECYCLE_VIEW_SIZE);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ORIGINAL_RECYCLE_VIEW_SIZE + this.obSmartFeed.getSmartFeedItemCount();
    }
}