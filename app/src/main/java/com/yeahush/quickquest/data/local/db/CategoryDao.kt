package com.yeahush.quickquest.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yeahush.quickquest.data.local.model.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name")
    fun getCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getCategory(categoryId: String): LiveData<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<Category>)
}