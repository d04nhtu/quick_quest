package com.yeahush.quickquest.ui.trivia

import androidx.lifecycle.*
import com.yeahush.quickquest.data.remote.TriviaCategory
import kotlinx.coroutines.launch
import javax.inject.Inject

class TriviaParamsViewModel @Inject constructor(
    private val repository: TriviaRepository
) : ViewModel() {

    private var _categories = MutableLiveData<List<TriviaCategory>>()

    val categories: LiveData<List<TriviaCategory>>
        get() = _categories

    init {
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            val response = repository.getCategoryResponse()
            if (response.isSuccessful) {
                _categories.value = response.body()?.results
            } else {

            }
        }
    }
}