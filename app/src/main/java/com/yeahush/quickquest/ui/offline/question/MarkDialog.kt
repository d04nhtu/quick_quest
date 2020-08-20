package com.yeahush.quickquest.ui.offline.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeahush.quickquest.R
import com.yeahush.quickquest.utilities.ARG_ITEMS
import kotlinx.android.synthetic.main.dialog_mark.*
import kotlinx.android.synthetic.main.list_item_mark.view.*


class MarkDialog : BottomSheetDialogFragment() {
    private lateinit var listener: OnQuestionSelectedListener

    interface OnQuestionSelectedListener {
        fun onQuestionSelected(number: Int)
    }

    fun setOnQuestionSelectedListener(listener: OnQuestionSelectedListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        bottom sheet round corners can be obtained but the while background appears, add this to remove that.
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_mark, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mark_list.layoutManager = LinearLayoutManager(context)
        mark_list.adapter = arguments?.getIntArray(ARG_ITEMS)?.let { ItemAdapter(it) }
    }

    private inner class ViewHolder internal constructor(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_mark, parent, false)
    ) {
        internal val button: Button = itemView.mark_item_title

        init {
            button.setOnClickListener {
                listener.onQuestionSelected(button.text.toString().toInt() - 1)
                dismiss()
            }
        }
    }

    private inner class ItemAdapter internal constructor(private val items: IntArray) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.button.text = (items[position] + 1).toString()
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

    companion object {
        fun newInstance(items: MutableList<Int>): MarkDialog =
            MarkDialog().apply {
                arguments = Bundle().apply {
                    items.sort()
                    putIntArray(ARG_ITEMS, items.toIntArray())
                }
            }
    }
}
