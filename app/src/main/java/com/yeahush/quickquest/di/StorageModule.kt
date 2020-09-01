package com.yeahush.quickquest.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.yeahush.quickquest.data.local.db.AppDatabase
import com.yeahush.quickquest.data.local.db.CategoryDao
import com.yeahush.quickquest.data.local.db.QuestionDao
import com.yeahush.quickquest.data.local.prefs.AppPreferences
import com.yeahush.quickquest.data.remote.ApiService
import com.yeahush.quickquest.ui.trivia.TriviaRepository
import com.yeahush.quickquest.utilities.DATABASE_NAME
import com.yeahush.quickquest.workers.PrepopulateCategoriesWorker
import com.yeahush.quickquest.workers.PrepopulateQuestionsWorker
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        // Make sure a read is made before writing so our onCreate callback is executed first
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    WorkManager.getInstance(context).enqueue(
                        listOf(
                            OneTimeWorkRequestBuilder<PrepopulateCategoriesWorker>().build(),
                            OneTimeWorkRequestBuilder<PrepopulateQuestionsWorker>().build()
                        )
                    )
                }
            }).build()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Singleton
    @Provides
    fun provideQuestionDao(database: AppDatabase): QuestionDao {
        return database.questionDao()
    }

    @Singleton
    @Provides
    fun provideTriviaRepository(apiService: ApiService): TriviaRepository {
        return TriviaRepository(apiService)
    }

    @Singleton
    @Provides
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences {
        return AppPreferences(context)
    }

}