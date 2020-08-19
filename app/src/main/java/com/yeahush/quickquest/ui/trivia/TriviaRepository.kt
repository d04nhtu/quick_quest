package com.yeahush.quickquest.ui.trivia

import com.yeahush.quickquest.data.remote.ApiService
import javax.inject.Inject

class TriviaRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getQuestionResponse( category: Int, difficulty: String, type: String) =
        apiService.getQuestions( category, difficulty, type)

    suspend fun getCategoryResponse() = apiService.getTriviaCategories()

}