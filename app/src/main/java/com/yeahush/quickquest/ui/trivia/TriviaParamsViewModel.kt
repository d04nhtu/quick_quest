package com.yeahush.quickquest.ui.trivia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeahush.quickquest.data.local.prefs.AppPreferences
import com.yeahush.quickquest.data.remote.TriviaCategory
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class TriviaParamsViewModel @Inject constructor(
    private val repository: TriviaRepository,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _categories = MutableLiveData<List<TriviaCategory>>()
    val categories: LiveData<List<TriviaCategory>>
        get() = _categories

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            try {
                withContext(NonCancellable) {
                    val response = repository.getCategoryResponse()
                    Timber.d(response.toString())
                    if (response.isSuccessful) {
                        _categories.value = response.body()?.results
                        _eventNetworkError.value = false
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error get Trivia categories")
                _eventNetworkError.value = true
            }
        }
    }

    fun isSoundEnable() = appPreferences.isSoundEnable()
    fun isVibrateEnable() = appPreferences.isVibrateEnable()
}