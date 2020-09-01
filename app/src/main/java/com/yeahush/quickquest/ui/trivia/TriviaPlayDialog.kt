package com.yeahush.quickquest.ui.trivia

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yeahush.quickquest.MainApplication
import com.yeahush.quickquest.R
import com.yeahush.quickquest.databinding.DialogTriviaPlayBinding
import com.yeahush.quickquest.ui.base.BaseDialog
import com.yeahush.quickquest.ui.trivia.TriviaOptionAdapter.OptionClick
import com.yeahush.quickquest.utilities.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_trivia_play.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TriviaPlayDialog : BaseDialog() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TriviaPlayViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var animCheck: Animation
    private lateinit var animFadeIn: Animation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogTriviaPlayBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAnim()
        subscribeUi()
    }

    private fun initToolbar() {
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.inflateMenu(R.menu.menu_play)
        toolbar.menu.findItem(R.id.action_skip).setOnMenuItemClickListener {
            if (viewModel.eventStart.value!!) {
                viewModel.onOptionClick(NONE)
            }
            true
        }
    }

    private fun initAnim() {
        animCheck = AnimationUtils.loadAnimation(activity, R.anim.anim_check)
        animFadeIn = AnimationUtils.loadAnimation(activity, R.anim.anim_fade_in)
    }

    private fun subscribeUi() {
        val optionAdapter = TriviaOptionAdapter(OptionClick { onOptionClick(it) })
        option_list.adapter = optionAdapter
        option_list.layoutManager = LinearLayoutManager(context)

        viewModel.currentQuestion.observe(viewLifecycleOwner, {
            question_text.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(it.question, Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(it.question)
            }
            question_text.startAnimation(animFadeIn)
        })

        viewModel.eventStart.observe(viewLifecycleOwner, {
            val options = (option_list.adapter as TriviaOptionAdapter).items
            for (i in options.indices) {
                option_list.layoutManager?.findViewByPosition(i)?.isClickable = it
            }
        })

        viewModel.eventNetworkError.observe(viewLifecycleOwner, {
            if (it) {
                showToast(context, resources.getString(R.string.check_internet))
                dismiss()
            }
        })

        viewModel.eventQueryEmpty.observe(viewLifecycleOwner, {
            if (it) {
                showToast(context, resources.getString(R.string.choose_other_category))
                dismiss()
            }
        })

        viewModel.eventFinish.observe(viewLifecycleOwner, { if (it) onFinish() })
        viewModel.options.observe(viewLifecycleOwner, {
            optionAdapter.setOptions(it)
        })
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setParams(
                arrayOf(
                    arguments?.getInt(TRIVIA_CATEGORY)!!.toString(),
                    arguments?.getString(TRIVIA_DIFFICULTY)!!,
                    arguments?.getString(TRIVIA_TYPE)!!
                )
            )
        }
    }

    private fun onFinish() {
        viewModel.score.value?.let { showScoreDialog(it) }
    }

    private fun onOptionClick(pos: Int) {
        if (viewModel.isVibrateEnable()) vibrate(context)
        viewModel.onOptionClick(pos)
        val options = ((option_list.adapter) as TriviaOptionAdapter).items
        val correct = viewModel.currentQuestion.value?.correctAnswer
        val layoutManager = option_list.layoutManager
        for (i in options.indices) {
            val option = layoutManager?.findViewByPosition(i)
            if (i == pos) {
                option?.startAnimation(animCheck)
                if (options[i] != correct) {
                    if (viewModel.isSoundEnable()) playResourceSound(context, R.raw.wrong)
                    option?.setBackgroundColor(resources.getColor(R.color.red, null))
                } else {
                    if (viewModel.isSoundEnable()) playResourceSound(context, R.raw.right)
                    option?.setBackgroundColor(resources.getColor(R.color.green, null))
                    break
                }
            } else {
                if (options[i] == correct) {
                    option?.startAnimation(animCheck)
                    option?.setBackgroundColor(resources.getColor(R.color.green, null))
                }
            }
        }
    }

    private fun showScoreDialog(score: Int) {
        val alertBuilder = context?.let { AlertDialog.Builder(it) }
        if (alertBuilder != null) {
            alertBuilder.setTitle("Result")
            alertBuilder.setMessage("${viewModel.getSignature()} scored $score")
            alertBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
                (activity?.application as MainApplication).showInterstitial()
                dismiss()
            }
            alertBuilder.show()
        }
    }
}