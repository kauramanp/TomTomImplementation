package com.aman.tomtomimplementation.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aman.tomtomimplementation.R


@Database(entities = [RestaurantEntity::class], version = 1, exportSchema = true)
abstract class RestaurantDatabase  : RoomDatabase(){
    abstract fun restaurantDao() : RestaurantDao

    companion object{
        private var restaurantDatabase: RestaurantDatabase?= null

        @Synchronized
        fun getDatabase(context: Context) : RestaurantDatabase{
            if(restaurantDatabase == null){
                restaurantDatabase = Room.databaseBuilder(context,
                    RestaurantDatabase::class.java,
                    context.resources.getString(R.string.app_name)).build()
            }

            return restaurantDatabase!!
        }
    }
}