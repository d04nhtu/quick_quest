package com.yeahush.quickquest.ui.home.question

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yeahush.quickquest.data.local.model.CategoryAndQuestions
import com.yeahush.quickquest.data.local.prefs.AppPreferences
import com.yeahush.quickquest.utilities.DONE
import com.yeahush.quickquest.utilities.ONE_SECOND
import com.yeahush.quickquest.utilities.TIME_PER_GAME
import timber.log.Timber
import javax.inject.Inject

class QuestionListViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val appPreferences: AppPreferences
) : ViewModel() {
    private val timer: CountDownTimer

    private val _catId = MutableLiveData<String?>()

    val categoryAndQuestions: LiveData<CategoryAndQuestions> =
        Transformations.switchMap(_catId) { catId ->
            catId?.let {
                questionRepository.getQuestionsOfCategory(it)
            }
        }

    // Countdown time
    private val _currentTime = MutableLiveData<Long>()

    // The String version of the current time
    val currentTimeString = Transformations.map(_currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // Event which triggers the end of the game
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    // The list of answers to questions in the selected category
    var answerList = mutableListOf<String>()
    var choices = Array(50) { "" }
    var resultList = mutableListOf<Boolean>()
    var marks = mutableListOf<Int>()
    var modeReview = false
    var scoreDialogVisible = false

    init {
        Timber.d("QuestionListViewModel created!")
        // Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(TIME_PER_GAME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }
        }
        timer.start()
    }

    /**
     * Callback called when the ViewModel is destroyed
     */
    override fun onCleared() {
        super.onCleared()
        Timber.d("QuestionListViewModel destroyed!")
        timer.cancel()
    }

    fun setCatId(catId: String?) {
        if (_catId.value != catId) {
            _catId.value = catId
        }
    }

    fun onGameFinishComplete() {
        timer.cancel()
    }

    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    fun calculateScore(): String {
        val total = answerList.size
        var correct = 0
        for (i in 0 until total) {
            if (choices[i] == answerList[i]) {
                correct++
                resultList.add(true)
            } else {
                resultList.add(false)
            }
        }
        return "${correct * 100 / total}% ($correct/$total)"
    }

    fun saveAnswers(questionsOfCategory: CategoryAndQuestions) {
        for (i in questionsOfCategory.questions) {
            answerList.add(i.answer)
        }
    }

    fun getQuestion(qNum: Int) = categoryAndQuestions.value?.questions?.get(qNum)
    fun isSoundEnable() = appPreferences.isSoundEnable()
    fun isVibrateEnable() = appPreferences.isVibrateEnable()
}