package com.yeahush.quickquest.ui.home.question

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import com.yeahush.quickquest.R
import com.yeahush.quickquest.ui.base.BaseDialog
import com.yeahush.quickquest.utilities.SCORE
import kotlinx.android.synthetic.main.dialog_score.*

class ScoreDialog : BaseDialog() {

    private var score = ""

    private lateinit var listener: OnAfterScoreListener

    interface OnAfterScoreListener {
        fun onModeReview()
        fun onRetry()
    }

    fun setOnModeReviewListener(listener: OnAfterScoreListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_score, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(SCORE) }?.apply {
            score = getString(SCORE)!!
        }
        score_text.text = score
        retry_button.setOnClickListener { onClickRetry() }
        review_button.setOnClickListener { onClickReview() }
        share_score_button.setOnClickListener { onClickShareScore() }
    }

    private fun onClickShareScore() {
        val shareIntent = activity?.let {
            ShareCompat.IntentBuilder.from(it)
                .setText("I got the score of $score from a test in Quick Quest application.")
                .setType("text/plain")
                .createChooserIntent()
                .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        }
        startActivity(shareIntent)
    }

    private fun onClickReview() {
        dismiss()
        listener.onModeReview()
    }

    private fun onClickRetry() {
        dismiss()
        listener.onRetry()
    }
}