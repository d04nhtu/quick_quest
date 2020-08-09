package com.yeahush.quickquest.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeahush.quickquest.ui.home.category.CategoryListViewModel
import com.yeahush.quickquest.utilities.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.IntoMap

@InstallIn(ApplicationComponent::class)
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CategoryListViewModel::class)
    abstract fun bindCategoryListViewModel(categoryListViewModel: CategoryListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelProviderFactory(
        factory: ViewModelProviderFactory
    ): ViewModelProvider.Factory

}