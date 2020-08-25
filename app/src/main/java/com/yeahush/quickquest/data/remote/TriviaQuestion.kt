package com.yeahush.quickquest.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TriviaQuestion(
    val category: String,
    var difficulty: String,
    val question: String,
    @Json(name = "correct_answer")
    val correctAnswer: String,
    @Json(name = "incorrect_answers")
    val incorrectAnswers: List<String>
)

@JsonClass(generateAdapter = true)
data class QuestionsResponse(
    val results: List<TriviaQuestion>
)