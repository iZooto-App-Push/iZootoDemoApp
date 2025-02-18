package com.k.deeplinkingtesting.inmobi.nativesad.sample.listview

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.inmobi.nativesad.utility.FeedData.FeedItem
import com.squareup.picasso.Picasso

internal class FeedItemAdapter(
    private val mContext: Context,
    private val mUsers: ArrayList<FeedItem>?
) :
    ArrayAdapter<FeedItem>(
        mContext, R.layout.listitem, R.id.title,
        mUsers!!
    ) {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getViewTypeCount(): Int {
        return NUM_CONTENT_FEED_VIEW_TYPES + NUM_AD_FEED_VIEW_TYPES
    }

    override fun getItemViewType(position: Int): Int {
        val feedItem = getItem(position)
        if (feedItem is AdFeedItem) {
            return VIEW_TYPE_INMOBI_STRAND
        }
        return VIEW_TYPE_CONTENT_FEED
    }

    override fun getItem(position: Int): FeedItem {
        return mUsers!![position]
    }

    @Suppress("deprecation")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val itemViewType = getItemViewType(position)
        val feedItem = getItem(position)

        if (itemViewType == VIEW_TYPE_CONTENT_FEED) {
            val viewHolder: ContentViewHolder
            if (null == convertView || null == convertView.tag) {
                convertView = mLayoutInflater.inflate(R.layout.listitem, parent, false)
                viewHolder = ContentViewHolder()
                viewHolder.title = convertView.findViewById<View>(R.id.title) as TextView
                viewHolder.subtitle = convertView.findViewById<View>(R.id.subtitle) as TextView
                viewHolder.time_tt = convertView.findViewById<View>(R.id.time_tt) as TextView
                viewHolder.description_tt =
                    convertView.findViewById<View>(R.id.description_tt) as TextView
                viewHolder.thumb_image =
                    convertView.findViewById<View>(R.id.thumb_image) as ImageView
                viewHolder.big_image = convertView.findViewById<View>(R.id.big_image) as ImageView
                viewHolder.bottom_img = convertView.findViewById<View>(R.id.bottom_img) as ImageView
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ContentViewHolder
            }

            val feed = getItem(position)
            viewHolder.title!!.text = feed.title
            viewHolder.subtitle!!.text = feed.subtitle
            viewHolder.time_tt!!.text = feed.timestamp
            viewHolder.description_tt!!.text = feed.description

            Picasso.get()
                .load(feed.thumbImage)
                .into(viewHolder.thumb_image)

            Picasso.get()
                .load(feed.thumbImage)
                .into(viewHolder.big_image)

            Picasso.get()
                .load(feed.thumbImage)
                .into(viewHolder.bottom_img)

            return convertView!!
        } else {
            //If ad feed, request InMobiStrand View. Note InMobiNativeStrand.getStrandView
            //returns null if ad is not loaded.
            //As we add AdFeed only onAdLoadSucceeded & clear the AdFeed onAdLoadFailed
            //ad, here we do not run into Null pointer Exception
            val inMobiNative = (feedItem as AdFeedItem).mNativeStrand

            val viewHolder: AdViewHolder
            if (null == convertView || null == convertView.tag) {
                convertView = mLayoutInflater.inflate(R.layout.layout_ad, parent, false)
                viewHolder = AdViewHolder()
                viewHolder.icon = convertView.findViewById<View>(R.id.adIcon) as ImageView
                viewHolder.title = convertView.findViewById<View>(R.id.adTitle) as TextView
                viewHolder.description =
                    convertView.findViewById<View>(R.id.adDescription) as TextView
                viewHolder.action = convertView.findViewById<View>(R.id.adAction) as Button
                viewHolder.content = convertView.findViewById<View>(R.id.adContent) as FrameLayout
                viewHolder.ratingBar = convertView.findViewById<View>(R.id.adRating) as RatingBar
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as AdViewHolder
            }

            Picasso.get()
                .load(inMobiNative.adIconUrl)
                .into(viewHolder.icon)
            viewHolder.title!!.text = inMobiNative.adTitle
            viewHolder.description!!.text = inMobiNative.adDescription
            viewHolder.action!!.text = inMobiNative.adCtaText

            val displayMetrics = DisplayMetrics()
            (mContext as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            viewHolder.content!!.addView(
                inMobiNative.getPrimaryViewOfWidth(
                    mContext, viewHolder.content,
                    parent, displayMetrics.widthPixels
                )
            )

            val rating = inMobiNative.adRating
            if (rating != 0f) {
                viewHolder.ratingBar!!.rating = rating
            }
            viewHolder.ratingBar!!.visibility = if (rating != 0f) View.VISIBLE else View.GONE

            viewHolder.action!!.setOnClickListener { inMobiNative.reportAdClickAndOpenLandingPage() }

            return convertView!!
        }
    }

    private class ContentViewHolder {
        var title: TextView? = null
        var subtitle: TextView? = null
        var time_tt: TextView? = null
        var description_tt: TextView? = null
        var thumb_image: ImageView? = null
        var big_image: ImageView? = null
        var bottom_img: ImageView? = null
    }

    private class AdViewHolder {
        var title: TextView? = null
        var description: TextView? = null
        var icon: ImageView? = null
        var action: Button? = null
        var content: FrameLayout? = null
        var ratingBar: RatingBar? = null
    }

    companion object {
        //Number of types of Views that Content feed wishes to display
        private const val NUM_CONTENT_FEED_VIEW_TYPES = 1

        //InmobiStrand always uses the same type of view to display Ad.
        private const val NUM_AD_FEED_VIEW_TYPES = 1

        //View type for Content Feed.
        private const val VIEW_TYPE_CONTENT_FEED = 0

        //View type for Ad Feed - from InMobi (InMobi Native Strand)
        private const val VIEW_TYPE_INMOBI_STRAND = 1
    }
}