package com.yeahush.quickquest.di

import android.content.Context
import com.yeahush.quickquest.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object StorageModule {

    @Singleton
    @Provides
    fun provideCategoryRepository(@ApplicationContext context: Context): CategoryRepository {
        return CategoryRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).categoryDao()
        )
    }

    @Singleton
    @Provides
    fun provideQuestionRepository(@ApplicationContext context: Context): QuestionRepository {
        return QuestionRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).questionDao()
        )
    }

    @Singleton
    @Provides
    fun provideChoicesPreference(@ApplicationContext context: Context): ChoicesPreferenceStorage =
        ChoicesPreferenceStorage(context)

    @Singleton
    @Provides
    fun provideCommonPreference(@ApplicationContext context: Context): CommonPreferenceStorage =
        CommonPreferenceStorage(context)

}