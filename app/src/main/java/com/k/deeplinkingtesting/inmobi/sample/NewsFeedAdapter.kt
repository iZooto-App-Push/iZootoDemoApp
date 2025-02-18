package com.k.deeplinkingtesting.inmobi.sample

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.k.deeplinkingtesting.R
import com.k.deeplinkingtesting.inmobi.utility.NewsSnippet

internal class NewsFeedAdapter(context: Context, private val mItems: List<NewsSnippet>) :
    ArrayAdapter<NewsSnippet?>(
        context, R.layout.news_headline_view,
        mItems
    ) {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView
        val viewHolder: ViewHolder
        if (null == rowView || null == convertView!!.tag) {
            rowView = mInflater.inflate(R.layout.news_headline_view, parent, false)
            viewHolder = ViewHolder()
            viewHolder.headline = rowView.findViewById<View>(R.id.caption) as TextView
            viewHolder.content = rowView.findViewById<View>(R.id.content) as TextView
            viewHolder.icon = rowView.findViewById<View>(R.id.photo) as SimpleDraweeView
            rowView.tag = viewHolder
        } else {
            viewHolder = rowView.tag as ViewHolder
        }

        val newsSnippet = mItems[position]
        viewHolder.headline!!.text = newsSnippet.title
        viewHolder.content!!.text = newsSnippet.content
        viewHolder.icon!!.setImageURI(Uri.parse(newsSnippet.imageUrl))
        return rowView!!
    }


    private class ViewHolder {
        var headline: TextView? = null
        var content: TextView? = null
        var icon: SimpleDraweeView? = null
    }
}