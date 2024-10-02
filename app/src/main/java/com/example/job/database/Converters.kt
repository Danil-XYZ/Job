package com.example.job.database

import androidx.room.TypeConverter
import com.example.job.model.ResponseData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromResponseData(value: ResponseData?): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toResponseData(value: String): ResponseData {
        val objectType = object : TypeToken<ResponseData>() {}.type
        return Gson().fromJson(value, objectType)
    }
}