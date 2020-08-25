package com.yeahush.quickquest.ui.trivia

import android.os.CountDownTimer
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yeahush.quickquest.data.local.prefs.AppPreferences
import com.yeahush.quickquest.data.remote.TriviaQuestion
import com.yeahush.quickquest.utilities.NONE
import com.yeahush.quickquest.utilities.ONE_SECOND
import com.yeahush.quickquest.utilities.TIME_PER_QUESTION
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class TriviaPlayViewModel @Inject constructor(
    private val repository: TriviaRepository,
    private val appPreferences: AppPreferences
) : ViewModel() {
    private var timer: CountDownTimer
    private var handler = Handler()

    // The current score
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private var _currentQuestion = MutableLiveData<TriviaQuestion>()
    val currentQuestion: LiveData<TriviaQuestion>
        get() = _currentQuestion

    val options = Transformations.map(_currentQuestion) {
        it.incorrectAnswers.toMutableList().apply {
            add(it.correctAnswer)
            shuffle()
        }
    }

    private val _currentTime = MutableLiveData<Int>()
    val currentTime: LiveData<Int>
        get() = _currentTime

    private var _eventLoaded = MutableLiveData<Boolean>(false)
    val eventLoaded: LiveData<Boolean>
        get() = _eventLoaded

    private var _eventQueryEmpty = MutableLiveData<Boolean>(false)
    val eventQueryEmpty: LiveData<Boolean>
        get() = _eventQueryEmpty

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _eventStart = MutableLiveData<Boolean>(false)
    val eventStart: LiveData<Boolean>
        get() = _eventStart

    private val _eventFinish = MutableLiveData<Boolean>()
    val eventFinish: LiveData<Boolean>
        get() = _eventFinish

    private val _questionNumber = MutableLiveData(0)
    val questionNumber: LiveData<Int>
        get() = _questionNumber

    private lateinit var questionList: MutableList<TriviaQuestion>

    private var _params = emptyArray<String>()

    init {
        timer = object : CountDownTimer(TIME_PER_QUESTION, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND).toInt()
            }

            override fun onFinish() {
                _eventStart.value = false
                handler.postDelayed({ nextQuestion() }, 600)
            }
        }
    }

    suspend fun setParams(params: Array<String>) {
        if (!_params.contentEquals(params)) {
            _params = params
            try {
                withContext(NonCancellable) {
                    val res =
                        repository.getQuestionResponse(params[0].toInt(), params[1], params[2])
                    Timber.d(res.toString())
                    if (res.isSuccessful) {
                        questionList = res.body()?.results?.toMutableList()!!
                        if (questionList.isEmpty()) {
                            _eventQueryEmpty.value = true
                        } else {
                            _eventLoaded.value = true
                            nextQuestion()
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error get Trivia questions")
                _eventNetworkError.value = true
            }
        }
    }

    /**
     * Callback called when the ViewModel is destroyed
     */
    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        handler.removeCallbacksAndMessages(null)
    }

    private fun nextQuestion() {
        if (questionList.isEmpty()) {
            _eventFinish.value = true
        } else {
            _eventStart.value = true
            _questionNumber.value = _questionNumber.value?.plus(1)
            _currentQuestion.value = questionList.removeAt(questionList.size - 1)
            timer.start()
        }
    }

    fun onOptionClick(pos: Int) {
        _eventStart.value = false
        if (pos != NONE && options.value!![pos] == _currentQuestion.value?.correctAnswer) {
            _score.value = (_score.value)?.plus(1)
        }
        timer.cancel()
        Handler().postDelayed({ nextQuestion() }, 600)
    }

    fun isSoundEnable() = appPreferences.isSoundEnable()
    fun isVibrateEnable() = appPreferences.isVibrateEnable()
    fun getSignature() = appPreferences.getSignature()
}