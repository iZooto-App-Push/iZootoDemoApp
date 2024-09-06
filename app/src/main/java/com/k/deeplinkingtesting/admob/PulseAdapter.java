package com.k.deeplinkingtesting.admob;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.k.deeplinkingtesting.R;

import java.util.List;


public class PulseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>

{

private final List<Object> itemList;
private final Context context;

private static final int VIEW_TYPE_AD = 1;
private static final int VIEW_TYPE_ITEM = 2;

public PulseAdapter(List<Object> itemList, Context context) {
    this.itemList = itemList;
    this.context = context;
}

@Override
public int getItemViewType(int position) {
    return itemList.get(position) instanceof NativeAd ? VIEW_TYPE_AD : VIEW_TYPE_ITEM;
}

@NonNull
@Override
public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_AD) {
        View adView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nativeadad_ad_layout, parent, false);
        return new NativeAdViewHolder(adView);
    } else {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pulse_new_design, parent, false);
        return new ItemViewHolder(itemView);
    }
}

@Override
public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder.getItemViewType() == VIEW_TYPE_AD) {
        NativeAd nativeAd = (NativeAd) itemList.get(position);
        ((NativeAdViewHolder) holder).bind(nativeAd);
    } else {
        // Bind your regular item data here
        ((ItemViewHolder) holder).bind(itemList.get(position));
    }
}

@Override
public int getItemCount() {
    return itemList.size();
}

// ViewHolder for Native Ads
//public static class NativeAdViewHolder extends RecyclerView.ViewHolder {
//    TextView adHeadline;
//    ImageView adIcon;
//    MediaView mediaView;
//    Button callToAction;
//    NativeAdView adView;
//
//    public NativeAdViewHolder(View itemView) {
//        super(itemView);
//        adView = (NativeAdView) itemView;
//        adHeadline = itemView.findViewById(R.id.ad_headline);
//        adIcon = itemView.findViewById(R.id.ad_icon);
//        mediaView = itemView.findViewById(R.id.ad_media);
//        callToAction = itemView.findViewById(R.id.ad_call_to_action);
//    }
//
//    public void bind(NativeAd nativeAd) {
//        // Set the headline
//        adHeadline.setText(nativeAd.getHeadline());
//
//        // Set the call to action button text
//        callToAction.setText(nativeAd.getCallToAction());
//
//        // Set the media content
//        mediaView.setMediaContent(nativeAd.getMediaContent());
//
//        // Load the icon if available
//        NativeAd.Image icon = nativeAd.getIcon();
//        if (icon != null) {
//            adIcon.setImageDrawable(icon.getDrawable());
//            adIcon.setVisibility(View.VISIBLE);
//        } else {
//            adIcon.setVisibility(View.GONE);
//        }
//
//        // Associate the native ad object with the native ad view.
//        adView.setNativeAd(nativeAd);
//    }
//}
public class NativeAdViewHolder extends RecyclerView.ViewHolder {
    private TextView adHeadline;
    private ImageView adIcon;
    private MediaView mediaView;
    private Button callToAction;
    private NativeAdView adView;

    public NativeAdViewHolder(View itemView) {
        super(itemView);
        adHeadline = itemView.findViewById(R.id.ad_headline);
        adIcon = itemView.findViewById(R.id.ad_icon);
        mediaView = itemView.findViewById(R.id.ad_media);  // Ensure this ID matches your XML layout
        callToAction = itemView.findViewById(R.id.ad_call_to_action);
        adView = (NativeAdView) itemView;  // NativeAdView should be the root view
    }

    public void bind(NativeAd nativeAd) {
        // Set the headline
        adHeadline.setText(nativeAd.getHeadline());

        // Set the call to action button text
        callToAction.setText(nativeAd.getCallToAction());

        // Set the media content (ensure mediaView and mediaContent are not null)
        if (nativeAd.getMediaContent() != null && mediaView != null) {
            mediaView.setMediaContent(nativeAd.getMediaContent());
        }

        // Load the icon if available
        if (nativeAd.getIcon() != null) {
            adIcon.setImageDrawable(nativeAd.getIcon().getDrawable());
            adIcon.setVisibility(View.VISIBLE);
        } else {
            adIcon.setVisibility(View.GONE);
        }

        // Associate the native ad object with the native ad view.
        adView.setNativeAd(nativeAd);
    }
}
// ViewHolder for regular items
public static class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView itemTitle; // Example of a regular item field

    public ItemViewHolder(View itemView) {
        super(itemView);
        itemTitle = itemView.findViewById(R.id.nt_title); // Example field
    }

    public void bind(Object item) {
        // Bind your regular item data here
        itemTitle.setText(item.toString());
    }

}
}




















