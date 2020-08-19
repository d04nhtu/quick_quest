package com.yeahush.quickquest.data.local.model

import androidx.room.*
import com.squareup.moshi.JsonClass
import com.yeahush.quickquest.data.local.model.Category

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["category_id"])
    ],
    indices = [Index("category_id")]
)
@JsonClass(generateAdapter = true)
data class Question(
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val answer: String,
    @ColumnInfo(name = "category_id") val categoryId: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var questionId: Long = 0

}