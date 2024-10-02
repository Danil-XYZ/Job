package com.example.job.network


import com.example.job.model.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @GET("uc?id=1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r&export=download")
    fun getData(): Call<ResponseData>
}