package com.aman.tomtomimplementation.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestaurantViewModel(application: Application) : AndroidViewModel(application) {
    val restaurantList: LiveData<List<RestaurantEntity>>

    private val repository:RestaurantRepository
    init {
        val taskDao=RestaurantDatabase.getDatabase(application).restaurantDao()
        repository= RestaurantRepository(taskDao)
        restaurantList=repository.readAllData
    }
    fun insertRestaurant(restaurantEntity: RestaurantEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertRestaurant(restaurantEntity)

        }
    }
    fun insertRestaurants(restaurantEntity: List<RestaurantEntity>){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertRestaurants(restaurantEntity)

        }
    }
    fun deleteRestaurant(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRestaurants()

        }
    }


    fun getRestaurants(): LiveData<List<RestaurantEntity>> {
        return restaurantList
    }
}