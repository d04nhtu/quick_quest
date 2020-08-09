package com.yeahush.quickquest.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions")
    fun getQuestions(): LiveData<List<Question>>

    /**
     * This query will tell Room to query both the [Category] and [Question] tables and handle
     * the object mapping.
     */
    @Transaction
    @Query("SELECT * FROM categories WHERE id IN (SELECT DISTINCT(category_id) FROM questions)")
    fun getCategoryAndQuestions(): LiveData<List<CategoryAndQuestions>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<Question>)

    @Insert
    suspend fun insertQuestion(question: Question): Long

    @Delete
    suspend fun deleteQuestion(question: Question)
}