package com.example.job.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.job.database.ResponseDataDao
import com.example.job.database.ResponseDataEntity
import com.example.job.database.ResponseDatabase
import com.example.job.model.ResponseData
import com.example.job.repository.Repository
import com.google.gson.Gson
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    private val responseDataDao: ResponseDataDao
    val data: LiveData<ResponseData?>

    init {
        responseDataDao = ResponseDatabase.getDatabase(application).responseDataDao()
        repository = Repository(responseDataDao)

        data = repository.getResponseData().map { responseDataEntity ->
            responseDataEntity?.let {
                Gson().fromJson(it.data, ResponseData::class.java)
            } ?: null
        }
    }

    fun fetchDataFromServer() {
        viewModelScope.launch {
            val responseData = repository.fetchData()
            if (responseData != null) {
                val jsonData = Gson().toJson(responseData)
                val responseDataEntity = ResponseDataEntity(data = jsonData)
                responseDataDao.updateResponseData(responseDataEntity)
            }
        }
    }

    fun updateData(newData: ResponseData) {
        viewModelScope.launch {
            repository.saveDataToDatabase(newData)
        }
    }
}