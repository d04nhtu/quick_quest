package com.yeahush.quickquest.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["category_id"])
    ],
    indices = [Index("category_id")]
)
data class Question(
    @ColumnInfo(name = "category_id") val categoryId: String,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val answer: String

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var questionId: Long = 0

}