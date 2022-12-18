package com.example.kocchiyomi.data.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manga")
data class MangaEntity(
    @NonNull @PrimaryKey val id: String
)