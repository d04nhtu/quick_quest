package com.yeahush.quickquest.workers

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yeahush.quickquest.R
import com.yeahush.quickquest.data.local.db.CategoryDao
import com.yeahush.quickquest.data.local.model.Category
import timber.log.Timber

class PrepopulateCategoriesWorker @WorkerInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val categoryDao: CategoryDao
) : Worker(context, workerParams) {

    override fun doWork(): Result =
        try {
            categoryDao.insertAll(getCategories())
            Result.success()
        } catch (ex: Exception) {
            Timber.e(ex, "Error prepopulate categories")
            Result.failure()
        }

    private fun getCategories(): MutableList<Category> {
        val cats = mutableListOf<Category>()
        cats.add(Category("animals", "Animals", getResName(R.drawable.ic_animals)))
        cats.add(Category("art", "Art", getResName(R.drawable.ic_art)))
        cats.add(Category("astronomy", "Astronomy", getResName(R.drawable.ic_astronomy)))
        cats.add(Category("books", "Books", getResName(R.drawable.ic_books)))
        cats.add(Category("games", "Games", getResName(R.drawable.ic_games)))
        cats.add(Category("geography", "Geography", getResName(R.drawable.ic_geography)))
        cats.add(Category("math", "Math", getResName(R.drawable.ic_math)))
        cats.add(Category("movies", "Movies", getResName(R.drawable.ic_movies)))
        cats.add(Category("music", "Music", getResName(R.drawable.ic_music)))
        cats.add(Category("sport", "Sport", getResName(R.drawable.ic_sport)))
        return cats
    }

    private fun getResName(resId: Int) = context.resources.getResourceEntryName(resId)
}