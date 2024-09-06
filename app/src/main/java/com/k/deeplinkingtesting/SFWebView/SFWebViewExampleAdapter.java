package com.k.deeplinkingtesting.SFWebView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.k.deeplinkingtesting.R;
import com.outbrain.OBSDK.SFWebView.SFWebViewWidget;
import com.outbrain.OBSDK.SFWebView.SFWebViewWidgetViewHolder;


public class SFWebViewExampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // RecyclerView view types
    private static final int TEXT_VIEW_TYPE = 1;
    private static final int ARTICLE_HEADER_VIEW_TYPE = 2;
    private static final int SF_REGULAR_WIDGET_VIEW_TYPE = 3;
    private static final int SF_SMARTFEED_WIDGET_VIEW_TYPE = 4;

    // RecyclerView original item count
    private static final int ORIGINAL_RECYCLE_VIEW_SIZE = 3;

    // Holding SFWidget
    private SFWebViewWidget mSFRegularWidget;
    private SFWebViewWidget mSFSmartfeedWidget;

    private SFWebViewWidgetViewHolder mSFRegularWidgetViewHolder;
    private SFWebViewWidgetViewHolder mSFSmartfeedWidgetViewHolder;

    public void setSFRegularWidget(SFWebViewWidget sfWebViewWidget) {
        this.mSFRegularWidget = sfWebViewWidget;
    }

    public void setSFSmartfeedWidget(SFWebViewWidget sfWebViewWidget) {
        this.mSFSmartfeedWidget = sfWebViewWidget;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class TextItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View layout;

        TextItemViewHolder(View v) {
            super(v);
            layout = v;
            textView = v.findViewById(R.id.article_text_view);
        }
    }

    public static class ImageItemViewHolder extends RecyclerView.ViewHolder {
        View layout;

        ImageItemViewHolder(View v) {
            super(v);
            layout = v;
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ARTICLE_HEADER_VIEW_TYPE;
            case 1:
                return TEXT_VIEW_TYPE;
            case 2:
                return this.mSFRegularWidget != null ? SF_REGULAR_WIDGET_VIEW_TYPE : TEXT_VIEW_TYPE;
            case 3:
                return TEXT_VIEW_TYPE;
            default:
                return SF_SMARTFEED_WIDGET_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case TEXT_VIEW_TYPE:
                v = inflater.inflate(R.layout.sfwidget_article_text, parent, false);
                return new TextItemViewHolder(v);
            case ARTICLE_HEADER_VIEW_TYPE:
                v = inflater.inflate(R.layout.sfwidget_article_header, parent, false);
                return new ImageItemViewHolder(v);
            case SF_REGULAR_WIDGET_VIEW_TYPE:
                return new SFWebViewWidgetViewHolder(mSFRegularWidget);
            case SF_SMARTFEED_WIDGET_VIEW_TYPE:
                return new SFWebViewWidgetViewHolder(mSFSmartfeedWidget);
            default:
                v = inflater.inflate(R.layout.sfwidget_article_text, parent, false);
                return new TextItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextItemViewHolder tvh;
        switch (position) {
            case 1:
                tvh = (TextItemViewHolder)holder;
                tvh.textView.setText(tvh.textView.getContext().getResources().getString(R.string.article_txt_1));
                break;
            case 2:
                if (this.mSFRegularWidget == null) {
                    tvh = (TextItemViewHolder)holder;
                    tvh.textView.setText(tvh.textView.getContext().getResources().getString(R.string.article_txt_3));
                }
                break;
            case 3:
                tvh = (TextItemViewHolder)holder;
                tvh.textView.setText(tvh.textView.getContext().getResources().getString(R.string.article_txt_3));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return ORIGINAL_RECYCLE_VIEW_SIZE + 2; // 2 can be regular + Smartfeed (if twoWidgetsOnPage) or TEXT_VIEW_TYPE + Smartfeed
    }
}
