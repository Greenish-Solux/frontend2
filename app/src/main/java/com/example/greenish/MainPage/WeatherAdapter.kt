package com.example.greenish.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.greenish.R
import com.example.greenish.databinding.ItemWeatherBinding
import com.example.greenish.retrofit.TemperatureResponse

class WeatherAdapter(private var weatherList: List<TemperatureResponse>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    override fun getItemCount(): Int = weatherList.size

    // Method to update the weather list and notify the adapter
    fun updateWeatherList(newWeatherList: List<TemperatureResponse>) {
        weatherList = newWeatherList
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    class WeatherViewHolder(private val binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weather: TemperatureResponse) {
            // Bind data to views using data binding
            binding.weather = weather
            binding.executePendingBindings()

            // Set image based on weatherCode
            val weatherImageRes = when (weather.weatherCode.lowercase()) {
                "clear sky" -> R.drawable.ic_clearsky
                "few clouds" -> R.drawable.ic_fewclouds
                "scattered clouds" -> R.drawable.ic_scatteredclouds
                "shower rain" -> R.drawable.ic_showerrain
                "rain" -> R.drawable.ic_rain
                "thunderstorm" -> R.drawable.ic_thunderstorm
                "snow" -> R.drawable.ic_snow
                else -> R.drawable.ic_mist
            }
            binding.ivHomeWeather.setImageResource(weatherImageRes)
        }
    }
}