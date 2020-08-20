package com.yeahush.quickquest.ui.offline.question

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yeahush.quickquest.data.local.model.CategoryAndQuestions
import com.yeahush.quickquest.data.local.prefs.ChoicesPreferenceStorage
import com.yeahush.quickquest.data.local.prefs.CommonPreferenceStorage
import timber.log.Timber
import javax.inject.Inject

class QuestionListViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val commonPreference: CommonPreferenceStorage,
    private val choicesPreference: ChoicesPreferenceStorage
) : ViewModel() {
    private val timer: CountDownTimer

    companion object {
        // Time when the game is over
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L

        // Total time for the game
        private const val COUNTDOWN_TIME = 600000L
    }

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

    var scoreDialogVisible = false

    // The list of answers to questions in the selected category
    var answerList = mutableListOf<String>()
    var resultList = mutableListOf<Boolean>()
    var markList = mutableListOf<Int>()

    init {
        Timber.d("QuestionListViewModel created!")
        // Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
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
        choicesPreference.removeChoices()
        setModeReview(false)
    }

    fun setCatId(catId: String?) {
        if (_catId.value != catId) {
            _catId.value = catId
        }
    }

    fun onGameFinishComplete() {
//        _eventGameFinish.value = false
        timer.cancel()
    }

    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    fun calculateScore(): String {
        val total = answerList.size
        var correct = 0
        for (i in 0 until total) {
            val choice = choicesPreference.getChoice(i.toString())
            if (choice == answerList[i]) {
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

    fun addMarkQuestion(qNum: Int) {
        markList.add(qNum)
    }

    fun removeMarkQuestion(qNum: Int) = markList.remove(qNum)

    fun setChoiceOfQuestion(qNum: Int, choice: String) {
        choicesPreference.setChoice(qNum.toString(), choice)
    }

    fun setModeReview(modeReview: Boolean) {
        commonPreference.modeReview = modeReview
    }

    fun getModeReview() = commonPreference.modeReview
}