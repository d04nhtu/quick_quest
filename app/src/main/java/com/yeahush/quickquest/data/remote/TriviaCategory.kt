package com.yeahush.quickquest.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TriviaCategory(val id: Int, val name: String)