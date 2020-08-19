package com.yeahush.quickquest.ui.home.question

import androidx.lifecycle.LiveData
import com.yeahush.quickquest.data.local.model.CategoryAndQuestions
import com.yeahush.quickquest.data.local.db.QuestionDao

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