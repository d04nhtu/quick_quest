package com.yeahush.quickquest.ui.home.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yeahush.quickquest.data.Category
import com.yeahush.quickquest.data.CategoryRepository
import javax.inject.Inject

class CategoryListViewModel @Inject constructor(
    categoryRepository: CategoryRepository
) : ViewModel() {
    val categories: LiveData<List<Category>> = categoryRepository.getCategories()
}