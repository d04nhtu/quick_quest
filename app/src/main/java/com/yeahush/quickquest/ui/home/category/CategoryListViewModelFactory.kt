package com.yeahush.quickquest.ui.home.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeahush.quickquest.data.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryListViewModelFactory @Inject constructor(
    private val repository: CategoryRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoryListViewModel(repository) as T
    }
}