package com.yeahush.quickquest.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yeahush.quickquest.data.local.model.CategoryAndQuestions
import com.yeahush.quickquest.data.local.model.Question

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions")
    fun getQuestions(): LiveData<List<Question>>

    /**
     * This query will tell Room to query both the [Category] and [Question] tables and handle
     * the object mapping.
     */
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getQuestionsOfCategory(categoryId: String): LiveData<CategoryAndQuestions>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(plants: List<Question>)

    @Insert
    suspend fun insertQuestion(question: Question): Long

    @Delete
    suspend fun deleteQuestion(question: Question)
}