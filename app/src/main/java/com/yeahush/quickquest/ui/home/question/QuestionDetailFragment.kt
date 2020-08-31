package com.yeahush.quickquest.ui.home.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yeahush.quickquest.R
import com.yeahush.quickquest.data.local.model.Question
import com.yeahush.quickquest.databinding.FragmentQuestionDetailBinding
import com.yeahush.quickquest.utilities.*
import kotlinx.android.synthetic.main.fragment_question_detail.*

class QuestionDetailFragment : Fragment(), View.OnClickListener {

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
        viewModel.eventGameFinish.observe(viewLifecycleOwner, {
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

        viewModel.eventGameFinish.observe(viewLifecycleOwner, { hasFinished ->
            if (hasFinished) gameFinished()
        })
    }

    private fun gameFinished() {
        if (quest.answer != choice) {
            setOptionBackground(choice, resources.getColor(R.color.red, null))
        }
        setOptionBackground(quest.answer, resources.getColor(R.color.green, null))
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

    override fun onClick(v: View?) {
        if (viewModel.isSoundEnable()) playResourceSound(context, R.raw.click)
        if (viewModel.isVibrateEnable()) vibrate(context)
        val viewId = v?.id
        if (viewId == R.id.mark_button) {
            if (isMarked) viewModel.marks.remove(qNum) else viewModel.marks.add(qNum)
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
        viewModel.choices[qNum] = choice
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
        var button: Button? = null
        when (option) {
            "A" -> button = optionA
            "B" -> button = optionB
            "C" -> button = optionC
            "D" -> button = optionD
        }
        button?.setBackgroundColor(resId)
        button?.setTextColor(resources.getColor(android.R.color.white, null))
    }
}