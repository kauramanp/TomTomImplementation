package com.aman.tomtomimplementation.room

import androidx.lifecycle.LiveData

class RestaurantRepository(private val restaurantDao: RestaurantDao) {
    val readAllData: LiveData<List<RestaurantEntity>> = restaurantDao.getRestaurants()

    suspend fun insertRestaurant(restaurantEntity: RestaurantEntity){
        restaurantDao.insertRestaurant(restaurantEntity)
    }
    suspend fun insertRestaurants(restaurantEntity: List<RestaurantEntity>){
        restaurantDao.insertRestaurants(restaurantEntity)
    }
    suspend fun deleteRestaurants(){
        restaurantDao.deleteRestaurants()
    }
}