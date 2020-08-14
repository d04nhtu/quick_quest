package com.yeahush.quickquest.ui.home.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeahush.quickquest.data.ChoicesPreferenceStorage
import com.yeahush.quickquest.data.CommonPreferenceStorage
import com.yeahush.quickquest.data.QuestionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionListViewModelFactory @Inject constructor(
    private val repository: QuestionRepository,
    private val commonPreference: CommonPreferenceStorage,
    private val choicesPreference: ChoicesPreferenceStorage
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionListViewModel(repository, commonPreference, choicesPreference) as T
    }
}