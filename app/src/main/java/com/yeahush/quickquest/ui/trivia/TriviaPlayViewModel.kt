package com.yeahush.quickquest.ui.trivia

import android.os.CountDownTimer
import android.os.Handler
import androidx.lifecycle.*
import com.yeahush.quickquest.data.remote.OnlineQuestion
import com.yeahush.quickquest.utilities.ONE_SECOND
import com.yeahush.quickquest.utilities.TIME_PER_QUESTION
import timber.log.Timber
import javax.inject.Inject

class TriviaPlayViewModel @Inject constructor(
    private val repository: TriviaRepository
) : ViewModel() {
    private lateinit var timer: CountDownTimer
    private lateinit var handler: Handler

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private var _currentQuestion = MutableLiveData<OnlineQuestion>()

    val currentQuestion: LiveData<OnlineQuestion>
        get() = _currentQuestion

    private val _currentTime = MutableLiveData<Int>()
    val currentTime: LiveData<Int>
        get() = _currentTime

    val options = Transformations.map(_currentQuestion) {
        it.incorrectAnswers.toMutableList().apply {
            add(it.correctAnswer)
            shuffle()
        }
    }

    // Event which triggers the end of the game
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private var _params = emptyArray<String>()

    // The list of words - the front of the list is the next _word to guess
    private lateinit var questionList: MutableList<OnlineQuestion>

    init {
        handler = Handler()
        _score.value = 0
    }

    suspend fun setParams(params: Array<String>) {
        if (!_params.contentEquals(params)) {
            _params = params

            val response = repository.getQuestionResponse(params[0].toInt(), params[1], params[2])
            Timber.d("mess ${response.message()}")
            if (response.isSuccessful) {
//                _onlineQuestions.value = response.body()?.results

                questionList = response.body()?.results?.toMutableList()!!
                nextQuestion()
            } else {

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
            Timber.d("Run out of quests")
            _eventGameFinish.value = true
        } else {
            _currentQuestion.value = questionList.removeAt(questionList.size - 1)
            timer = object : CountDownTimer(TIME_PER_QUESTION, ONE_SECOND) {
                override fun onTick(millisUntilFinished: Long) {
                    _currentTime.value = (millisUntilFinished / ONE_SECOND).toInt()
                }

                override fun onFinish() {
                    handler.postDelayed({
                        nextQuestion()
                    }, 800)

                }
            }
            timer.start()
        }
    }

    fun onOptionClick(pos: Int) {
        if (options.value!![pos] == _currentQuestion.value?.correctAnswer) {
            _score.value = (_score.value)?.plus(1)
        }
        timer.cancel()
        Handler().postDelayed({ nextQuestion() }, 800)
    }
}