package com.example.job.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "response_data")
data class ResponseDataEntity(
    @PrimaryKey val id: Int = 1,
    val data: String
)