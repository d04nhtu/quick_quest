package com.yeahush.quickquest.ui.trivia

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yeahush.quickquest.databinding.DialogTriviaQuestionBinding
import com.yeahush.quickquest.ui.base.BaseDialog
import com.yeahush.quickquest.ui.trivia.TriviaOptionAdapter.OptionClick
import com.yeahush.quickquest.utilities.TRIVIA_CATEGORY
import com.yeahush.quickquest.utilities.TRIVIA_DIFFICULTY
import com.yeahush.quickquest.utilities.TRIVIA_TYPE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_trivia_question.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TriviaPlayDialog : BaseDialog() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TriviaPlayViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogTriviaQuestionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val optionAdapter = TriviaOptionAdapter(OptionClick { onOptionClick(it) })
        option_list.adapter = optionAdapter
        option_list.layoutManager = LinearLayoutManager(context)

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer<Boolean> { hasFinished ->
            if (hasFinished) gameFinished()
        })

        viewModel.options.observe(viewLifecycleOwner, Observer { optionAdapter.setOptions(it) })

        val category = arguments?.getInt(TRIVIA_CATEGORY)!!
        val difficulty = arguments?.getString(TRIVIA_DIFFICULTY)!!
        val type = arguments?.getString(TRIVIA_TYPE)!!
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setParams(arrayOf(category.toString(), difficulty, type))
        }
    }

    private fun gameFinished() {
        showScoreDialog(viewModel.score.value!!)
    }

    private fun onOptionClick(pos: Int) {
        viewModel.onOptionClick(pos)
        val options = ((option_list.adapter) as TriviaOptionAdapter).items
        val correct = viewModel.currentQuestion.value?.correctAnswer
        val layoutManager = option_list.layoutManager
        for (i in options.indices) {
            val option = layoutManager?.findViewByPosition(i)
            option?.isEnabled = false
            option?.isClickable = false
            if (i == pos) {
                if (options[i] != correct) option?.setBackgroundColor(Color.RED)
                else {
                    option?.setBackgroundColor(Color.GREEN)
                    break
                }
            } else {
                if (options[i] == correct) option?.setBackgroundColor(Color.GREEN)
            }
        }
    }

    private fun showScoreDialog(score: Int) {
        val myAlertBuilder = AlertDialog.Builder(requireContext())
        myAlertBuilder.setTitle("Result")
        myAlertBuilder.setMessage("You scored $score")
        myAlertBuilder.setPositiveButton(android.R.string.ok) { _, _ -> dismiss() }
        myAlertBuilder.show()
    }
}