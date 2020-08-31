package com.yeahush.quickquest.ui.trivia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.yeahush.quickquest.R
import com.yeahush.quickquest.utilities.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_trivia_params.*
import javax.inject.Inject

@AndroidEntryPoint
class TriviaParamsFragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TriviaParamsViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trivia_params, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (category_spinner != null) {
            outState.putString(CATEGORY_KEY, category_spinner.selectedItem as String?)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.eventNetworkError.observe(viewLifecycleOwner, {
            if (it) {
                progress_bar.visibility = View.GONE
                retry.visibility = View.VISIBLE
                error_msg.visibility = View.VISIBLE
            }
        })

        viewModel.categories.observe(viewLifecycleOwner, { triviaCategories ->
            val categories: List<String> = triviaCategories.map { it.name }
            val arrayAdapter = context?.let {
                ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, categories)
            }
            arrayAdapter?.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            category_spinner.adapter = arrayAdapter
            val category = savedInstanceState?.getString(CATEGORY_KEY)
            if (category != null) {
                arrayAdapter?.getPosition(category)
                    ?.let { category_spinner.setSelection(it, false) }
            }
            loading_layout.visibility = View.GONE
            params_layout.visibility = View.VISIBLE
        })

        submit.setOnClickListener(this)
        retry.setOnClickListener(this)
    }

    private fun onSubmit() {
        val type: String = when (type_group.checkedRadioButtonId) {
            R.id.true_false -> "boolean"
            R.id.multiple_choice -> "multiple"
            else -> ""
        }
        val difficulty: String = when (difficulty_group.checkedRadioButtonId) {
            R.id.easy -> "easy"
            R.id.medium -> "medium"
            R.id.hard -> "hard"
            else -> ""
        }
        val selected = category_spinner.selectedItem as String
        val categoryId = viewModel.categories.value?.find { selected == it.name }?.id

        val fragmentManager = activity?.supportFragmentManager ?: return
        TriviaPlayDialog().apply {
            arguments = Bundle().apply {
                categoryId?.let { putInt(TRIVIA_CATEGORY, it) }
                putString(TRIVIA_DIFFICULTY, difficulty)
                putString(TRIVIA_TYPE, type)
            }
            show(fragmentManager, TRIVIA_QUESTION_DIALOG)
        }
    }

    private fun onRetry() {
        progress_bar.visibility = View.VISIBLE
        retry.visibility = View.GONE
        error_msg.visibility = View.GONE
        viewModel.getCategories()
    }

    override fun onClick(v: View?) {
        if (viewModel.isSoundEnable()) playResourceSound(context, R.raw.click)
        if (viewModel.isVibrateEnable()) vibrate(context)
        when (v?.id) {
            R.id.retry -> onRetry()
            R.id.submit -> onSubmit()
        }
    }
}