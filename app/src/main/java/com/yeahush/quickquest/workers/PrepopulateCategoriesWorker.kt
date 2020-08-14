package com.yeahush.quickquest.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yeahush.quickquest.R
import com.yeahush.quickquest.data.AppDatabase
import com.yeahush.quickquest.data.Category
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

class PrepopulateCategoriesWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            AppDatabase.getInstance(applicationContext).categoryDao().insertAll(getCategories())
            Result.success()
        } catch (ex: Exception) {
            Timber.e(ex, "Error prepopulate categories")
            Result.failure()
        }
    }

    private fun getCategories(): MutableList<Category> {
        val catList = mutableListOf<Category>()
        catList.add(Category("animals", "Animals", getResName(R.drawable.ic_animals)))
        catList.add(Category("art", "Art", getResName(R.drawable.ic_art)))
        catList.add(Category("astronomy", "Astronomy", getResName(R.drawable.ic_astronomy)))
        catList.add(Category("books", "Books", getResName(R.drawable.ic_books)))
        catList.add(Category("games", "Games", getResName(R.drawable.ic_games)))
        catList.add(Category("geography", "Geography", getResName(R.drawable.ic_geography)))
        catList.add(Category("math", "Math", getResName(R.drawable.ic_math)))
        catList.add(Category("movies", "Movies", getResName(R.drawable.ic_movies)))
        catList.add(Category("music", "Music", getResName(R.drawable.ic_music)))
        catList.add(Category("sport", "Sport", getResName(R.drawable.ic_sport)))
        return catList
    }

    private fun getResName(resId: Int) = context.resources.getResourceEntryName(resId)
}