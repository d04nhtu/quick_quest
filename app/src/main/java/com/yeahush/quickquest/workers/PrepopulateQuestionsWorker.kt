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

class PrepopulateQuestionsWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(QUESTION_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val questionType = object : TypeToken<List<Question>>() {}.type
                    val questionList: List<Question> = Gson().fromJson(jsonReader, questionType)

                    val database = AppDatabase.getInstance(applicationContext)
                    database.questionDao().insertAll(questionList)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Timber.e(ex, "Error prepopulate questions")
            Result.failure()
        }
    }
}