package com.yeahush.quickquest.ui.offline.question

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yeahush.quickquest.utilities.QUESTION_ORDINAL

class QuestionListStateAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 50

    override fun createFragment(position: Int): Fragment {
        return QuestionDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(QUESTION_ORDINAL, position)
            }
        }
    }
}
