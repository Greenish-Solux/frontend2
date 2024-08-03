package com.example.greenish

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greenish.databinding.GridItemPlantBinding

class PlantGridAdapter(private val context: Context) :
    BaseAdapter() {
    private val plants = mutableListOf<PlantListItem>()

    fun updatePlants(newPlants: List<PlantListItem>) {
        plants.clear()
        plants.addAll(newPlants)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = plants.size

    override fun getItem(position: Int): Any = plants[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item_plant, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val plant = plants[position]

// null 체크 추가
        val imageUrl = plant.photo?.url
        if (imageUrl != null) {
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholderimage)
                .error(R.drawable.placeholderimage)
                .into(holder.imageView)
        } else {
            // 이미지 URL이 없는 경우 기본 이미지 설정
            holder.imageView.setImageResource(R.drawable.placeholderimage)
        }

        return view
    }

    private class ViewHolder(view: View) {
        val imageView: ImageView = view.findViewById(R.id.plantImageView)
    }
}