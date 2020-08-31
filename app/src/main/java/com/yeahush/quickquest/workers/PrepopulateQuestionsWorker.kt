package com.yeahush.quickquest.workers

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.yeahush.quickquest.data.local.db.QuestionDao
import com.yeahush.quickquest.data.local.model.Question
import com.yeahush.quickquest.utilities.QUESTION_DATA_FILENAME
import timber.log.Timber

class PrepopulateQuestionsWorker @WorkerInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val questionDao: QuestionDao
) : Worker(context, workerParams) {

    override fun doWork(): Result =
        try {
            val json = applicationContext.assets.open(QUESTION_DATA_FILENAME).bufferedReader()
                .use { it.readText() }
            val listType = Types.newParameterizedType(List::class.java, Question::class.java)
            val adapter: JsonAdapter<List<Question>> = Moshi.Builder().build().adapter(listType)
            adapter.fromJson(json)?.let { questionDao.insertAll(it) }
            Result.success()
        } catch (ex: Exception) {
            Timber.e(ex, "Error prepopulate questions")
            Result.failure()
        }

}