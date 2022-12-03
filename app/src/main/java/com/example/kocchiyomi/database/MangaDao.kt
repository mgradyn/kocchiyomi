package com.example.kocchiyomi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kocchiyomi.data.model.MangaEntity
@Dao
interface MangaDao {
    @Query("SELECT * FROM manga")
    fun getAll(): List<MangaEntity>

    @Query("SELECT * FROM manga WHERE id = :id")
    fun getById(id: String): MangaEntity?

    @Insert
    fun save(mangaEntity: MangaEntity)

    @Query("DELETE FROM manga WHERE id = :id")
    fun delete(id: String)
}