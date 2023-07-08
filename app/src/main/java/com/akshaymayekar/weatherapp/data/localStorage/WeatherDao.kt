package com.akshaymayekar.weatherapp.data.localStorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {
    @Query("SELECT * FROM LastWeatherInfo")
    fun getAll(): List<LastWeatherInfo>

  /*  @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<LastWeatherInfo>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
           "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): LastWeatherInfo*/

    @Insert
    fun insert(vararg info: LastWeatherInfo)

    @Query("DELETE FROM LastWeatherInfo")
    fun delete()
}