package com.yeahush.quickquest.di

import android.content.Context
import com.yeahush.quickquest.data.local.db.AppDatabase
import com.yeahush.quickquest.data.local.prefs.ChoicesPreferenceStorage
import com.yeahush.quickquest.data.local.prefs.CommonPreferenceStorage
import com.yeahush.quickquest.data.remote.ApiService
import com.yeahush.quickquest.ui.trivia.TriviaRepository
import com.yeahush.quickquest.ui.home.category.CategoryRepository
import com.yeahush.quickquest.ui.home.question.QuestionRepository
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
    fun provideOnlineQuestionRepository(apiService: ApiService): TriviaRepository {
        return TriviaRepository(apiService)
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