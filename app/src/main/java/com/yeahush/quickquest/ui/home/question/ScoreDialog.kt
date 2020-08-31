package com.yeahush.quickquest.ui.home.question

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import com.yeahush.quickquest.MainApplication
import com.yeahush.quickquest.R
import com.yeahush.quickquest.data.local.prefs.AppPreferences
import com.yeahush.quickquest.ui.base.BaseDialog
import com.yeahush.quickquest.utilities.SCORE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_score.*
import javax.inject.Inject

@AndroidEntryPoint
class ScoreDialog : BaseDialog(), View.OnClickListener {
    @Inject
    lateinit var appPreferences: AppPreferences

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(SCORE) }?.apply {
            score = getString(SCORE)!!
        }
        who_scored.text = "${appPreferences.getSignature()} scored: "
        score_text.text = score
        retry_button.setOnClickListener(this)
        review_button.setOnClickListener(this)
        share_score_button.setOnClickListener(this)
    }

    private fun onClickShareScore() {
        val shareIntent = activity?.let {
            ShareCompat.IntentBuilder.from(it)
                .setText("I got the score of $score from a test in Quick Quest application \uD83D\uDC49 https://play.google.com/store/apps/details?id=${it.packageName}")
                .setType("text/plain")
                .createChooserIntent()
                .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        }
        startActivity(shareIntent)
    }

    override fun onClick(v: View?) {
        (activity?.application as MainApplication).showInterstitial()
        when (v?.id) {
            R.id.retry_button -> {
                dismiss()
                listener.onRetry()
            }
            R.id.review_button -> {
                dismiss()
                listener.onModeReview()
            }
            R.id.share_score_button -> onClickShareScore()
        }
    }
}