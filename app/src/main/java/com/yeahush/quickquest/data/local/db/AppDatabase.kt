package com.yeahush.quickquest.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yeahush.quickquest.data.local.model.Category
import com.yeahush.quickquest.data.local.model.Question

@Database(entities = [Question::class, Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun categoryDao(): CategoryDao
}