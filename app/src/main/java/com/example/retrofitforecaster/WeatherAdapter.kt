package com.example.retrofitforecaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WeatherAdapter : ListAdapter<WeatherItem, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val ITEM_COLD = 123
        private const val ITEM_HOT = 321
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            ITEM_COLD -> WeatherViewHolderCold(inflater.inflate(R.layout.viewholdercold, parent, false))
            ITEM_HOT -> WeatherViewHolderHot(inflater.inflate(R.layout.viewholderhot, parent, false))
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).main.temp < 0) {
            ITEM_COLD
        } else {
            ITEM_HOT
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val weatherItem = getItem(position)
        when (holder) {
            is WeatherViewHolderCold -> holder.bind(weatherItem)
            is WeatherViewHolderHot -> holder.bind(weatherItem)
        }
    }

    class WeatherViewHolderCold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weatherItem: WeatherItem) {
            itemView.findViewById<TextView>(R.id.date).text = weatherItem.dt_txt
            itemView.findViewById<TextView>(R.id.temp).text = "${weatherItem.main.temp} °C"

            val iconUrl = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}@2x.png"
            Glide.with(itemView)
                .load(iconUrl)
                .into(itemView.findViewById(R.id.weather_icon))
        }
    }

    class WeatherViewHolderHot(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weatherItem: WeatherItem) {
            itemView.findViewById<TextView>(R.id.date).text = weatherItem.dt_txt
            itemView.findViewById<TextView>(R.id.temp).text = "${weatherItem.main.temp} °C"

            val iconUrl = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}@2x.png"
            Glide.with(itemView)
                .load(iconUrl)
                .into(itemView.findViewById(R.id.weather_icon))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<WeatherItem>() {
        override fun areItemsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
            return oldItem.dt_txt == newItem.dt_txt
        }

        override fun areContentsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
            return oldItem == newItem
        }
    }
}