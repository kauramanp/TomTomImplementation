package com.aman.tomtomimplementation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aman.tomtomimplementation.databinding.ItemLayoutBinding
import com.aman.tomtomimplementation.room.RestaurantEntity

class RestaurantRecycler(var restaurants : ArrayList<RestaurantEntity>) : RecyclerView.Adapter<RestaurantRecycler.ViewHolder>() {
    class ViewHolder(var binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind( restaurantEntity: RestaurantEntity){
            binding.name = restaurantEntity.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = restaurants.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(restaurants[position])
    }
}