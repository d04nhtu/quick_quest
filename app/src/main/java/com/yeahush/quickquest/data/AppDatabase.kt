package com.yeahush.quickquest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.yeahush.quickquest.utilities.DATABASE_NAME
import com.yeahush.quickquest.workers.PrepopulateCategoriesWorker
import com.yeahush.quickquest.workers.PrepopulateQuestionsWorker

@Database(entities = [Question::class, Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun categoryDao(): CategoryDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        WorkManager.getInstance().enqueue(
                            listOf(
                                OneTimeWorkRequestBuilder<PrepopulateQuestionsWorker>().build(),
                                OneTimeWorkRequestBuilder<PrepopulateCategoriesWorker>().build()
                            )
                        )
                    }
                }).build()
        }
    }
}