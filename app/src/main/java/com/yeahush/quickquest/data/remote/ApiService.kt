package com.yeahush.quickquest.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api_category.php")
    suspend fun getTriviaCategories(): Response<TriviaCategoryResponse>

    @GET("api.php?amount=20")
    suspend fun getQuestions(
        @Query("category") category: Int,
        @Query("difficulty") difficulty: String,
        @Query("type") type: String
    ): Response<QuestionsResponse>
}