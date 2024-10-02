package com.example.job.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.job.database.ResponseDatabase
import com.example.job.database.ResponseDataDao
import com.example.job.database.ResponseDataEntity
import com.example.job.model.ResponseData
import com.example.job.network.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val responseDataDao: ResponseDataDao) {

    fun getResponseData(): LiveData<ResponseDataEntity?> {
        return responseDataDao.getResponseData()
    }

    suspend fun saveDataToDatabase(responseData: ResponseData) {
        val jsonData = Gson().toJson(responseData)
        val responseDataEntity = ResponseDataEntity(data = jsonData)
        responseDataDao.updateResponseData(responseDataEntity)
    }

    suspend fun fetchData(): ResponseData? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getData().execute()
                if (response.isSuccessful) {
                    response.body().also {
                        it?.let {
                            Log.d("Success", "Получен ответ")
                        } ?: Log.e("Error", "Ответ пустой")
                    }
                } else {
                    Log.e("Error", "Ошибка сервера: ${response.message()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("Error", "Ошибка сети: ${e.message}")
                null
            }
        }
    }
}