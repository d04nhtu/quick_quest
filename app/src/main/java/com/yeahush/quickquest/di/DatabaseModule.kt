package com.yeahush.quickquest.di

import android.content.Context
import com.yeahush.quickquest.data.AppDatabase
import com.yeahush.quickquest.data.CategoryDao
import com.yeahush.quickquest.data.CategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideCategoryRepository(@ApplicationContext context: Context): CategoryRepository {
        return CategoryRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).categoryDao()
        )
    }

}