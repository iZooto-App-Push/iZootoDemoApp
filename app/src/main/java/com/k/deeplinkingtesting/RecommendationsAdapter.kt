package com.k.deeplinkingtesting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecommendationsAdapter(private val recommendations: List<Recommendation>) :
    RecyclerView.Adapter<RecommendationsAdapter.RecommendationViewHolder>() {

    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val urlTextView: TextView = itemView.findViewById(R.id.urlTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false)
        return RecommendationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val recommendation = recommendations[position]
        holder.titleTextView.text = recommendation.title
        holder.urlTextView.text = recommendation.url
    }

    override fun getItemCount() = recommendations.size
}