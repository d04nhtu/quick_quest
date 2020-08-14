package com.yeahush.quickquest.data

import androidx.lifecycle.LiveData

class QuestionRepository private constructor(
    private val questionDao: QuestionDao
) {
    fun getQuestionsOfCategory(categoryId: String): LiveData<CategoryAndQuestions> =
        questionDao.getQuestionsOfCategory(categoryId)

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: QuestionRepository? = null

        fun getInstance(questionDao: QuestionDao) =
            instance ?: synchronized(this) {
                instance ?: QuestionRepository(questionDao).also { instance = it }
            }
    }
}