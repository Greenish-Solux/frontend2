package com.example.greenish.SearchpageANDMypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greenish.databinding.ItemSearchResultBinding
import com.bumptech.glide.Glide

class SearchResultAdapter(private val onItemClick: (SearchResult) -> Unit) :
    ListAdapter<SearchResult, SearchResultAdapter.ViewHolder>(SearchResultDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivSearchResultBox.setOnClickListener {
                onItemClick(getItem(adapterPosition))
            }
        }

        fun bind(item: SearchResult) {
            binding.tvSearchResultTitle.text = item.cntntsSj
            binding.tvSearchResultDescription.text = item.fmlCodeNm
            binding.tvSearchResultDescription2.text = item.clCodeNm
            Glide.with(binding.root.context)
                .load(item.rtnFileUrl)
                .into(binding.ivSearchResultImage)
        }
    }
}

class SearchResultDiffCallback : DiffUtil.ItemCallback<SearchResult>() {
    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem.cntntsNo == newItem.cntntsNo
    }

    override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem == newItem
    }
}