package com.k.deeplinkingtesting.VideoWidget;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.k.deeplinkingtesting.R;
import com.outbrain.OBSDK.VideoWidget.OBVideoWidget;


public class ArticleListVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TEXT_VIEW_TYPE = 1;
    private static final int ARTICLE_HEADER_VIEW_TYPE = 2;
    private static final int RECOMMENDATION_VIEW_TYPE = 3;

    private static final int RECYCLE_VIEW_SIZE = 5;

    private OBVideoWidget videoWidget;

    public void setOBVideoWidget(OBVideoWidget videoWidget) {
        this.videoWidget = videoWidget;
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
            textView = v.findViewById(R.id.article_text_view);
        }
    }

    public class ImageItemViewHolder extends RecyclerView.ViewHolder {
        public View layout;

        public ImageItemViewHolder(View v) {
            super(v);
            layout = v;
        }
    }

    public class RecommendationViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public FrameLayout frameLayout;

        public RecommendationViewHolder(View v) {
            super(v);
            layout = v;
            frameLayout = v.findViewById(R.id.video_widget_frame_layout);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ARTICLE_HEADER_VIEW_TYPE;
            case 2:
                return RECOMMENDATION_VIEW_TYPE;
            default:
                return TEXT_VIEW_TYPE;
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
            case RECOMMENDATION_VIEW_TYPE:
                v = inflater.inflate(R.layout.article_recommendation_row, parent, false);
                RecommendationViewHolder rvh = new RecommendationViewHolder(v);
                return rvh;
            default:
                return  null;
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
                RecommendationViewHolder rvh = (RecommendationViewHolder) holder;
                videoWidget.load(rvh.frameLayout);
                break;
            case 3:
                tvh = (TextItemViewHolder)holder;
                tvh.textView.setText(tvh.textView.getContext().getResources().getString(R.string.article_txt_2));
                break;
            case 4:
                tvh = (TextItemViewHolder)holder;
                tvh.textView.setText(tvh.textView.getContext().getResources().getString(R.string.article_txt_3));
                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return RECYCLE_VIEW_SIZE;
    }
}