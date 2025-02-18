package com.k.deeplinkingtesting.inmobi.nativesad.sample.recyclerview

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.inmobi.nativesad.utility.FeedData.FeedItem
import com.squareup.picasso.Picasso

internal class FeedsAdapter(
    private val mFeedItems: ArrayList<FeedItem>?,
    private val mContext: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return mFeedItems!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val feedItem = mFeedItems?.get(position)
        if (feedItem is AdFeedItem) {
            return VIEW_TYPE_INMOBI_STRAND
        }
        return VIEW_TYPE_CONTENT_FEED
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val card = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.recycler_card_layout,
            viewGroup,
            false
        )
        if (viewType == VIEW_TYPE_CONTENT_FEED) {
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.listitem, card as ViewGroup, true)
            return FeedViewHolder(card)
        } else {
            return AdViewHolder(
                mContext, card
            )
        }
    }

    @Suppress("deprecation")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val feedItem = mFeedItems?.get(position)
        if (viewHolder is FeedViewHolder) {
            val feedViewHolder = viewHolder
            if (feedItem != null) {
                feedViewHolder.title.text = feedItem.title
            }
            if (feedItem != null) {
                feedViewHolder.subTitle.text = feedItem.subtitle
            }
            if (feedItem != null) {
                feedViewHolder.timeStamp.text = feedItem.timestamp
            }
            if (feedItem != null) {
                feedViewHolder.description.text = feedItem.description
            }

            if (feedItem != null) {
                Picasso.get()
                    .load(feedItem.thumbImage)
                    .into(feedViewHolder.thumbImage)
            }

            if (feedItem != null) {
                Picasso.get()
                    .load(feedItem.thumbImage)
                    .into(feedViewHolder.image)
            }

            Picasso.get()
                .load(R.drawable.linkedin_bottom)
                .into(feedViewHolder.bottom)
        } else {
            val adViewHolder = viewHolder as AdViewHolder
            val inMobiNative = (feedItem as AdFeedItem).mNativeStrand

            Picasso.get()
                .load(inMobiNative.adIconUrl)
                .into(adViewHolder.icon)
            adViewHolder.title.text = inMobiNative.adTitle
            adViewHolder.description.text = inMobiNative.adDescription
            adViewHolder.action.text = inMobiNative.adCtaText

            val displayMetrics = DisplayMetrics()
            (mContext as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            adViewHolder.adContent.addView(
                inMobiNative.getPrimaryViewOfWidth(
                    mContext, adViewHolder.adView,
                    adViewHolder.cardView, displayMetrics.widthPixels
                )
            )

            val rating = inMobiNative.adRating
            if (rating != 0f) {
                adViewHolder.ratingBar.rating = rating
            }
            adViewHolder.ratingBar.visibility = if (rating != 0f) View.VISIBLE else View.GONE

            adViewHolder.action.setOnClickListener { inMobiNative.reportAdClickAndOpenLandingPage() }
        }
    }

    private class AdViewHolder(context: Context?, adCardView: View) :
        RecyclerView.ViewHolder(adCardView) {
        var cardView: CardView = adCardView as CardView
        var adView: View =
            LayoutInflater.from(context).inflate(R.layout.layout_ad, null)

        var icon: ImageView = adView.findViewById<View>(R.id.adIcon) as ImageView
        var title: TextView = adView.findViewById<View>(R.id.adTitle) as TextView
        var description: TextView = adView.findViewById<View>(R.id.adDescription) as TextView
        var action: Button = adView.findViewById<View>(R.id.adAction) as Button
        var adContent: FrameLayout = adView.findViewById<View>(R.id.adContent) as FrameLayout
        var ratingBar: RatingBar = adView.findViewById<View>(R.id.adRating) as RatingBar

        init {
            cardView.addView(adView)
        }
    }

    private class FeedViewHolder(feedCardView: View) : RecyclerView.ViewHolder(feedCardView) {
        var cardView: CardView = feedCardView as CardView
        var thumbImage: ImageView =
            feedCardView.findViewById<View>(R.id.thumb_image) as ImageView
        var title: TextView =
            feedCardView.findViewById<View>(R.id.title) as TextView
        var subTitle: TextView =
            feedCardView.findViewById<View>(R.id.subtitle) as TextView
        var timeStamp: TextView =
            feedCardView.findViewById<View>(R.id.time_tt) as TextView
        var description: TextView =
            feedCardView.findViewById<View>(R.id.description_tt) as TextView
        var image: ImageView =
            feedCardView.findViewById<View>(R.id.big_image) as ImageView
        var bottom: ImageView =
            feedCardView.findViewById<View>(R.id.bottom_img) as ImageView
    }

    companion object {
        //View type for Content Feed.
        private const val VIEW_TYPE_CONTENT_FEED = 0

        //View type for Ad Feed - from InMobi (InMobi Native Strand)
        private const val VIEW_TYPE_INMOBI_STRAND = 1
    }
}