package com.nrs.finopaytest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nrs.finopaytest.data.local.entity.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val dao: WeatherDao
}
