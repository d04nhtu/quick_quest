package com.yeahush.quickquest.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.yeahush.quickquest.R
import com.yeahush.quickquest.data.AppDatabase
import com.yeahush.quickquest.data.Category
import com.yeahush.quickquest.data.Question
import com.yeahush.quickquest.utilities.QUESTION_DATA_FILENAME
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import java.lang.Exception

class PrepopulateCategoriesWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            val categoryList = mutableListOf<Category>()
            categoryList.add(Category("animals", "Animals", R.drawable.ic_animals))
            categoryList.add(Category("art", "Art", R.drawable.ic_art))
            categoryList.add(Category("astronomy", "Astronomy", R.drawable.ic_astronomy))
            categoryList.add(Category("books", "Books", R.drawable.ic_books))
            categoryList.add(Category("games", "Games", R.drawable.ic_games))
            categoryList.add(Category("geography", "Geography", R.drawable.ic_geography))
            categoryList.add(Category("math", "Math", R.drawable.ic_math))
            categoryList.add(Category("movies", "Movies", R.drawable.ic_movies))
            categoryList.add(Category("music", "Music", R.drawable.ic_music))
            categoryList.add(Category("sport", "Sport", R.drawable.ic_sport))

            val database = AppDatabase.getInstance(applicationContext)
            database.categoryDao().insertAll(categoryList)

            Result.success()
        } catch (ex: Exception) {
            Timber.e(ex, "Error prepopulate categories")
            Result.failure()
        }
    }
}