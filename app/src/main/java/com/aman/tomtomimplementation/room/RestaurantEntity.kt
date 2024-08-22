package com.aman.tomtomimplementation.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var name : String?= null
)
