package com.yeahush.quickquest.ui.home.question

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.tabs.TabLayoutMediator
import com.yeahush.quickquest.R
import com.yeahush.quickquest.databinding.DialogQuestionListBinding
import com.yeahush.quickquest.ui.base.BaseDialog
import com.yeahush.quickquest.utilities.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_question_list.*
import javax.inject.Inject

@AndroidEntryPoint
class QuestionListDialog : BaseDialog(), View.OnClickListener, ScoreDialog.OnAfterScoreListener,
    MarkDialog.OnQuestionSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: QuestionListViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val catId = arguments?.takeIf { it.containsKey(CATEGORY_KEY) }?.getString(CATEGORY_KEY)!!
        viewModel.setCatId(catId)
        val binding = DialogQuestionListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@QuestionListDialog
            viewModel = this@QuestionListDialog.viewModel
        }
        subscribeUi()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.inflateMenu(R.menu.menu_offline)
        val searchView = toolbar.menu.findItem(R.id.jump_action).actionView as SearchView
        searchView.inputType = InputType.TYPE_CLASS_NUMBER
        val searchHint: SearchView.SearchAutoComplete =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchHint.hint = resources.getString(R.string.jump_to)
        val searchIcon: ImageView = searchView.findViewById(androidx.appcompat.R.id.search_button)
        searchIcon.setImageDrawable(
            ResourcesCompat.getDrawable(resources, android.R.drawable.ic_menu_compass, null)
        )
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.onActionViewCollapsed()
                try {
                    val num = query.toInt() - 1
                    if (num in 0..49) pager.currentItem = num
                    else showToast(context, resources.getString(R.string.out_of_bounds))
                } catch (e: NumberFormatException) {
                    showToast(context, resources.getString(R.string.input_number))
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    override fun onModeReview() {
        viewModel.modeReview = true
        viewModel.scoreDialogVisible = false
    }

    override fun onRetry() {
        dismiss()
    }

    override fun onQuestionSelected(number: Int) {
        pager.currentItem = number
    }

    override fun onClick(v: View?) {
        if (viewModel.isSoundEnable()) playResourceSound(context, R.raw.click)
        if (viewModel.isVibrateEnable()) vibrate(context)
        when (v?.id) {
            R.id.marked_questions_button -> {
                if (viewModel.marks.isNotEmpty()) {
                    val fragmentManager = activity?.supportFragmentManager ?: return
                    MarkDialog.newInstance(viewModel.marks).apply {
                        setOnQuestionSelectedListener(this@QuestionListDialog)
                        show(fragmentManager, MARK_DIALOG)
                    }
                } else context?.let { showToast(it, "Mark list's empty.") }

            }
            R.id.finish_button -> onClickFinish()
        }
    }

    private var warningDialog: AlertDialog? = null

    private fun onClickFinish() {
        if (warningDialog == null) {
            warningDialog = context?.let {
                AlertDialog.Builder(it).apply {
                    setTitle("Warning")
                    setMessage("Are you sure want to finish the game?")
                    setPositiveButton(R.string.ok_button) { _, _ -> viewModel.onGameFinish() }
                    setNegativeButton(R.string.cancel_button) { _, _ -> warningDialog?.dismiss() }
                    setCancelable(false)
                }.create()
            }
        }
        warningDialog?.show()
    }

    private fun subscribeUi() {
        viewModel.categoryAndQuestions.observe(viewLifecycleOwner) { questsOfCat ->
            toolbar.title = questsOfCat.category.name
            pager.apply {
                adapter = QuestionListStateAdapter(this@QuestionListDialog)
                // Set the margin between pages in the ViewPager2
                val pageMargin = resources.getDimensionPixelSize(R.dimen.margin_normal)
                setPageTransformer { page, position -> page.translationX = position * pageMargin }
            }
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
            val fragment = manager.findFragmentByTag(SCORE_DIALOG)
            if (fragment != null) (fragment as ScoreDialog).setOnModeReviewListener(this)
        } else {
            if (!viewModel.modeReview) showScoreDialog(viewModel.calculateScore())
        }

        val markDialog = manager.findFragmentByTag(MARK_DIALOG)
        if (markDialog != null) (markDialog as MarkDialog).dismiss()

        if (warningDialog != null && warningDialog!!.isShowing) warningDialog?.dismiss()

        val resultList = viewModel.resultList
        val size = resultList.size
        for (i in 0 until size) {
            val resId = if (resultList[i]) R.drawable.ic_correct else R.drawable.ic_incorrect
            tabs.getTabAt(i)?.setIcon(resId)
        }
    }

    private fun showScoreDialog(score: String) {
        val fragmentManager = activity?.supportFragmentManager ?: return
        ScoreDialog().apply {
            setOnModeReviewListener(this@QuestionListDialog)
            arguments = Bundle().apply { putString(SCORE, score) }
            show(fragmentManager, SCORE_DIALOG)
        }
        viewModel.scoreDialogVisible = true
    }

}
