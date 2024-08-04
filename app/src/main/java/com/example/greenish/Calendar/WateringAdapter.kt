package com.example.greenish

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.greenish.databinding.ItemWaterBinding

class WateringAdapter(private val onCompleteClick: (Int) -> Unit) :
    ListAdapter<WateringData, WateringAdapter.WateringViewHolder>(WateringDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WateringViewHolder {
        val binding = ItemWaterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WateringViewHolder(binding)
    }
    override fun onBindViewHolder(holder: WateringViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    inner class WateringViewHolder(private val binding: ItemWaterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WateringData) {

            try {
                Log.d("WateringAdapter", "Binding item: $item")

                binding.tvWaterCycle.text = "${item.plantName} 물주기"
                binding.waterCheckBox.isChecked = item.status == "COMPLETE"

                // 체크박스 상태에 따라 활성화/비활성화 설정
                binding.waterCheckBox.isEnabled = item.status != "COMPLETE"

                Log.d("WateringAdapter", "Item ${item.wateringId} status: ${item.status}")

                binding.waterCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked && item.status != "COMPLETE") {
                        Log.d("WateringAdapter", "Checkbox checked for item ${item.wateringId}")
                        Log.d("WateringAdapter", "Calling onCompleteClick for item ${item.wateringId}")
                        onCompleteClick(item.wateringId)
                        // 체크박스를 비활성화하여 더 이상 변경할 수 없게 함
                        binding.waterCheckBox.isEnabled = false
                    } else if (!isChecked) {
                        // 체크 해제 시도 시 다시 체크 상태로 되돌림
                        binding.waterCheckBox.isChecked = true
                    }
                }
            }
            catch (e: Exception) {
                Log.e("WateringAdapter", "Error binding item", e)
            }
        }
    }
}
class WateringDiffCallback : DiffUtil.ItemCallback<WateringData>() {
    override fun areItemsTheSame(oldItem: WateringData, newItem: WateringData): Boolean {
        return oldItem.wateringId == newItem.wateringId
    }
    override fun areContentsTheSame(oldItem: WateringData, newItem: WateringData): Boolean {
        return oldItem == newItem
    }
}