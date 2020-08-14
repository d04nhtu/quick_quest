package com.yeahush.quickquest.ui.home.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.tabs.TabLayoutMediator
import com.yeahush.quickquest.R
import com.yeahush.quickquest.databinding.FragmentQuestionListBinding
import com.yeahush.quickquest.utilities.CATEGORY_KEY
import com.yeahush.quickquest.utilities.MARK_DIALOG
import com.yeahush.quickquest.utilities.SCORE
import com.yeahush.quickquest.utilities.SCORE_DIALOG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_question_list.*
import javax.inject.Inject


@AndroidEntryPoint
class QuestionListFragment : Fragment(), View.OnClickListener, ScoreDialog.OnModeReviewListener,
    MarkDialog.OnQuestionSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: QuestionListViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var listener: OnGameFinishedListener

    interface OnGameFinishedListener {
        fun onGameFinished()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is QuestionDetailFragment) {
            listener = childFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val catId = arguments?.takeIf { it.containsKey(CATEGORY_KEY) }?.getString(CATEGORY_KEY)!!
        viewModel.setCatId(catId)
        val binding = FragmentQuestionListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@QuestionListFragment
            viewModel = this@QuestionListFragment.viewModel
        }

        subscribeUi()
        return binding.root
    }

    override fun onModeReview() {
        viewModel.setModeReview(true)
        viewModel.scoreDialogVisible = false
    }

    override fun onQuestionSelected(number: Int) {
        pager.currentItem = number
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.marked_questions_button -> {
                if (viewModel.markList.isNotEmpty()) {
                    val fragmentManager = activity?.supportFragmentManager ?: return
                    MarkDialog.newInstance(viewModel.markList).apply {
                        setOnQuestionSelectedListener(this@QuestionListFragment)
                        show(fragmentManager, MARK_DIALOG)
                    }
                } else Toast.makeText(context, "Mark list's empty.", Toast.LENGTH_SHORT).show()
            }
            R.id.finish_button -> viewModel.onGameFinish()
        }
    }

    private fun subscribeUi() {
        viewModel.categoryAndQuestions.observe(viewLifecycleOwner) { questsOfCat ->
            pager.adapter = QuestionListStateAdapter(this)
            TabLayoutMediator(tabs, pager) { tab, pos -> tab.text = "${(pos + 1)}" }.attach()
            if (viewModel.answerList.isEmpty()) {
                viewModel.saveAnswers(questsOfCat)
            }
        }

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer<Boolean> { hasFinished ->
            if (hasFinished) gameFinished()
        })
    }

    private fun gameFinished() {
        viewModel.onGameFinishComplete()

        val manager = activity?.supportFragmentManager ?: return
        if (viewModel.scoreDialogVisible) {
            (manager.findFragmentByTag(SCORE_DIALOG) as ScoreDialog).setOnModeReviewListener(this)
        } else if (!viewModel.getModeReview()) showScoreDialog(viewModel.calculateScore())

        val markDialog = manager.findFragmentByTag(MARK_DIALOG)
        if (markDialog != null) (markDialog as MarkDialog).dismiss()

        val resultList = viewModel.resultList
        val size = resultList.size
        for (i in 0 until size) {
            val resId = if (resultList[i]) R.drawable.ic_correct else R.drawable.ic_incorrect
            tabs.getTabAt(i)?.setIcon(resId)
        }

        listener.onGameFinished()
    }

    private fun showScoreDialog(score: String) {
        val fragmentManager = activity?.supportFragmentManager ?: return
        ScoreDialog().apply {
            setOnModeReviewListener(this@QuestionListFragment)
            arguments = Bundle().apply { putString(SCORE, score) }
            show(fragmentManager, SCORE_DIALOG)
        }
        viewModel.scoreDialogVisible = true
    }

}
