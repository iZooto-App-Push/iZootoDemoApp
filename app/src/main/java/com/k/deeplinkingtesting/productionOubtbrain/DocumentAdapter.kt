package com.k.deeplinkingtesting.productionOubtbrain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.k.deeplinkingtesting.R

class DocumentAdapter(private val documents: List<Documents>,
                      private val onItemClick: (Documents) -> Unit) :
    RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.outbrain_item_documents, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val document = documents[position]
        holder.bind(document)
    }

    override fun getItemCount(): Int = documents.size

    inner class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sourceNameTextView: TextView = itemView.findViewById(R.id._title)
        private val sourceNameDisplay: TextView = itemView.findViewById(R.id.message)
        private val sourcePublisherName: TextView = itemView.findViewById(R.id.publisherName)
        private  val bannerImage : ImageView = itemView.findViewById(R.id._image)


        init {
            itemView.setOnClickListener {
                val document = documents[adapterPosition] // Access the documents list
                onItemClick(document) // Invoke the callback
            }
        }

        fun bind(document: Documents) {
            sourceNameTextView.text = document.sourceName
            sourceNameDisplay.text = document.content
            sourcePublisherName.text = document.publisherName
            Glide.with(itemView.context)
                .load(document.thumbnailUrl)
                .into(bannerImage)
        }
    }
}
