package com.aman.tomtomimplementation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.tomtomimplementation.databinding.ActivityRestaurantListBinding
import com.aman.tomtomimplementation.room.RestaurantEntity
import com.aman.tomtomimplementation.room.RestaurantViewModel

class RestaurantListActivity : AppCompatActivity() {

    lateinit var restaurantViewModel: RestaurantViewModel
    var roomRestaurants = arrayListOf<RestaurantEntity>()

    private val binding: ActivityRestaurantListBinding by lazy {
        ActivityRestaurantListBinding.inflate(layoutInflater)
    }
    var restaurantRecycler = RestaurantRecycler(roomRestaurants)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        restaurantViewModel = ViewModelProvider(this)[RestaurantViewModel::class.java]

        binding?.recycler?.layoutManager = LinearLayoutManager(this)
        binding.recycler?.adapter = restaurantRecycler

        restaurantViewModel.restaurantList.observe(this
        ) { t ->
            roomRestaurants.clear()
            roomRestaurants.addAll(t as ArrayList<RestaurantEntity>)
            restaurantRecycler.notifyDataSetChanged()
        }

    }
}