package com.yeahush.quickquest.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.yeahush.quickquest.data.local.model.Category
import com.yeahush.quickquest.data.local.model.Question

/**
 * This class captures the relationship between a [Category] and its list of [Question]s, which is
 * used by Room to fetch the related entities.
 */
data class CategoryAndQuestions(
    @Embedded
    val category: Category,

    @Relation(parentColumn = "id", entityColumn = "category_id")
    val questions: List<Question> = emptyList()
)