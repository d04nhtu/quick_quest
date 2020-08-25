package com.yeahush.quickquest.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TriviaCategory(val id: Int, val name: String)

@JsonClass(generateAdapter = true)
data class TriviaCategoryResponse(
    @Json(name = "trivia_categories")
    val results: List<TriviaCategory>
)