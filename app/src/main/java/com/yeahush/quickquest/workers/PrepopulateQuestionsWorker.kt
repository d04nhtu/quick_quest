package com.yeahush.quickquest.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.yeahush.quickquest.data.AppDatabase
import com.yeahush.quickquest.data.Question
import com.yeahush.quickquest.utilities.QUESTION_DATA_FILENAME
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

class PrepopulateQuestionsWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(QUESTION_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val questionType = object : TypeToken<List<Question>>() {}.type
                    val questions: List<Question> = Gson().fromJson(jsonReader, questionType)
                    AppDatabase.getInstance(applicationContext).questionDao().insertAll(questions)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Timber.e(ex, "Error prepopulate questions")
            Result.failure()
        }
    }
}