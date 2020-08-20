package com.yeahush.quickquest.ui.offline.category

import com.yeahush.quickquest.data.local.db.CategoryDao

class CategoryRepository private constructor(private val categoryDao: CategoryDao) {

    fun getCategories() = categoryDao.getCategories()

    fun getCategory(categoryId: String) = categoryDao.getCategory(categoryId)

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: CategoryRepository? = null

        fun getInstance(categoryDao: CategoryDao) =
            instance ?: synchronized(this) {
                instance ?: CategoryRepository(categoryDao).also { instance = it }
            }
    }
}