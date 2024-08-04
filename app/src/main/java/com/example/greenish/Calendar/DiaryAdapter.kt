package com.example.greenish

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greenish.databinding.ItemDiaryBinding

class DiaryAdapter(private val posts: List<DiaryPost>, private val onClick: (DiaryPost) -> Unit) :
    RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {
    class DiaryViewHolder(private val binding: ItemDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: DiaryPost, onClick: (DiaryPost) -> Unit) {
            binding.tvProfileDiaryTitle.text = post.title
            binding.tvProfileDiaryDate.text = post.createdAt
            itemView.setOnClickListener { onClick(post) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val binding = ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(binding)
    }
    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(posts[position], onClick)
    }
    override fun getItemCount() = posts.size
}
