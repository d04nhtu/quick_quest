package com.yeahush.quickquest.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OnlineQuestion(
    val category: String,
    var difficulty: String,
    val question: String,
    @Json(name = "correct_answer")
    val correctAnswer: String,
    @Json(name = "incorrect_answers")
    val incorrectAnswers: List<String>
)