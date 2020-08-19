package com.yeahush.quickquest.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.yeahush.quickquest.data.local.db.AppDatabase
import com.yeahush.quickquest.data.local.model.Question
import com.yeahush.quickquest.utilities.QUESTION_DATA_FILENAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class PrepopulateQuestionsWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val json = applicationContext.assets.open(QUESTION_DATA_FILENAME).bufferedReader()
                .use { it.readText() }
            val listType = Types.newParameterizedType(List::class.java, Question::class.java)
            val adapter: JsonAdapter<List<Question>> = Moshi.Builder().build().adapter(listType)
            adapter.fromJson(json)?.let {
                AppDatabase.getInstance(applicationContext).questionDao().insertAll(it)
            }
            Result.success()
        } catch (ex: Exception) {
            Timber.e(ex, "Error prepopulate questions")
            Result.failure()
        }
    }
}