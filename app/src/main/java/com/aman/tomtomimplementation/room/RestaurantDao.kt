package com.aman.tomtomimplementation.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {
    @Insert
    suspend fun insertRestaurant(restaurantEntity: RestaurantEntity)

    @Insert
    suspend fun insertRestaurants(restaurantEntity: List<RestaurantEntity>)

    @Query("SELECT * FROM RestaurantEntity")
    fun getRestaurants() : LiveData<List<RestaurantEntity>>

    @Query("DELETE FROM RestaurantEntity")
    suspend fun deleteRestaurants()
}