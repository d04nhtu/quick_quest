package com.yeahush.quickquest.data

class QuestionRepository private constructor(
    private val questionDao: QuestionDao
) {

    fun getCategoryAndQuestions() = questionDao.getCategoryAndQuestions()

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