package com.yeahush.quickquest.ui.offline.question

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yeahush.quickquest.R
import com.yeahush.quickquest.data.local.model.Question
import com.yeahush.quickquest.databinding.FragmentQuestionDetailBinding
import com.yeahush.quickquest.utilities.CHOICE
import com.yeahush.quickquest.utilities.IS_MARKED
import com.yeahush.quickquest.utilities.QUESTION_ORDINAL
import kotlinx.android.synthetic.main.fragment_question_detail.*

class QuestionDetailFragment : Fragment(), View.OnClickListener,
    QuestionListFragment.OnGameFinishedListener {

    private lateinit var viewModel: QuestionListViewModel
    private lateinit var quest: Question
    private var choice: String = ""
    private var qNum: Int = 0
    private var isMarked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireParentFragment())[QuestionListViewModel::class.java]
        quest = viewModel.getQuestion(qNum)!!
        val binding = FragmentQuestionDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@QuestionDetailFragment
            question = quest
        }
        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer {
            binding.isFinished = it
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mark_button.isSelected = isMarked
        question_ordinal.text = (qNum + 1).toString()
        if (choice.isNotEmpty()) {
            setOptionsIsSelected(false)
            setSelectedChoice()
        }
        if (viewModel.getModeReview()) onGameFinished()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            choice = savedInstanceState.getString(CHOICE)!!
            isMarked = savedInstanceState.getBoolean(IS_MARKED)
        }
        qNum = arguments?.takeIf { it.containsKey(QUESTION_ORDINAL) }?.getInt(QUESTION_ORDINAL)!!
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CHOICE, choice)
        outState.putBoolean(IS_MARKED, isMarked)
    }

    fun onClickShowAlert(view: View?) {
        val myAlertBuilder = AlertDialog.Builder(requireContext())

        myAlertBuilder.setTitle("title")
        myAlertBuilder.setMessage("message")

        myAlertBuilder.setPositiveButton(R.string.ok_button) { _, _ -> }
        myAlertBuilder.setNegativeButton(R.string.cancel_button) { _, _ -> }
        myAlertBuilder.show()
    }

    override fun onClick(v: View?) {
        val viewId = v?.id
        if (viewId == R.id.mark_button) {
            if (isMarked) viewModel.removeMarkQuestion(qNum) else viewModel.addMarkQuestion(qNum)
            isMarked = !isMarked
            mark_button.isSelected = isMarked
            return
        }
        setOptionsIsSelected(false)
        when (viewId) {
            R.id.optionA -> choice = "A"
            R.id.optionB -> choice = "B"
            R.id.optionC -> choice = "C"
            R.id.optionD -> choice = "D"
        }
        setSelectedChoice()
        viewModel.setChoiceOfQuestion(qNum, choice)
    }

    override fun onGameFinished() {
        if (quest.answer != choice) {
            setOptionBackground(choice, Color.RED)
        }
        setOptionBackground(quest.answer, Color.GREEN)
    }

    private fun setOptionsIsSelected(isSelected: Boolean) {
        optionA.isSelected = isSelected
        optionB.isSelected = isSelected
        optionC.isSelected = isSelected
        optionD.isSelected = isSelected
    }

    private fun setSelectedChoice() {
        when (choice) {
            "A" -> optionA.isSelected = true
            "B" -> optionB.isSelected = true
            "C" -> optionC.isSelected = true
            "D" -> optionD.isSelected = true
        }
    }

    private fun setOptionBackground(option: String, resId: Int) {
        when (option) {
            "A" -> optionA.setBackgroundColor(resId)
            "B" -> optionB.setBackgroundColor(resId)
            "C" -> optionC.setBackgroundColor(resId)
            "D" -> optionD.setBackgroundColor(resId)
        }
    }
}