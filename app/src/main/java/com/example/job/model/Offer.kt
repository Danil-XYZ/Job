package com.example.job.model

data class Offer(
    val id: String?,
    val title: String,
    val link: String,
    val button: ButtonData? = null
)

data class ButtonData(
    val text: String
)