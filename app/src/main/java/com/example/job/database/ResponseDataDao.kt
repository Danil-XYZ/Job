package com.example.job.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ResponseDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateResponseData(responseDataEntity: ResponseDataEntity)

    @Query("SELECT * FROM response_data LIMIT 1")
    fun getResponseData(): LiveData<ResponseDataEntity?>
}