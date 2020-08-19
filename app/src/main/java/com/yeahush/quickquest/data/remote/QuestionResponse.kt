package com.yeahush.quickquest.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionsResponse(
    val results: List<OnlineQuestion>
)