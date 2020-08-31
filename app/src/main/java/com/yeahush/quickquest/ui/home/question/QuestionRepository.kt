package com.yeahush.quickquest.ui.home.question

import androidx.lifecycle.LiveData
import com.yeahush.quickquest.data.local.db.QuestionDao
import com.yeahush.quickquest.data.local.model.CategoryAndQuestions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(private val questionDao: QuestionDao) {
    fun getQuestionsOfCategory(categoryId: String): LiveData<CategoryAndQuestions> =
        questionDao.getQuestionsOfCategory(categoryId)
}