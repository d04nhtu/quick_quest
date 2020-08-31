package com.yeahush.quickquest.ui.home.category

import com.yeahush.quickquest.data.local.db.CategoryDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(private val categoryDao: CategoryDao) {
    fun getCategories() = categoryDao.getCategories()
}