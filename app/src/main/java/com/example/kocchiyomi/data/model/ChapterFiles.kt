package com.example.kocchiyomi.data.model

import com.google.gson.annotations.SerializedName

data class ChapterFiles(
    val hash: String,
    @SerializedName("data") val data: List<String>,
    @SerializedName("dataSaver") val dataSaver: List<String>,
)
