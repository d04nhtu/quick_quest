package com.yeahush.quickquest.ui.home.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yeahush.quickquest.data.local.model.Category
import com.yeahush.quickquest.data.local.prefs.AppPreferences
import javax.inject.Inject

class CategoryListViewModel @Inject constructor(
    categoryRepository: CategoryRepository,
    private val appPreferences: AppPreferences
) : ViewModel() {
    val categories: LiveData<List<Category>> = categoryRepository.getCategories()

    fun isSoundEnable() = appPreferences.isSoundEnable()
    fun isVibrateEnable() = appPreferences.isVibrateEnable()
}