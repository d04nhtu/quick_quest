package com.yeahush.quickquest.ui.trivia

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.yeahush.quickquest.R
import kotlinx.android.synthetic.main.list_item_option.view.*

class TriviaOptionAdapter(val callback: OptionClick) :
    RecyclerView.Adapter<ViewHolder>() {
    var items: List<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = items[position]
        holder.button.text = option
        holder.button.background = ResourcesCompat.getDrawable(
            holder.itemView.resources, R.drawable.bg_option, null
        )
        holder.button.setOnClickListener { callback.onClick(position) }
        holder.button.isEnabled = true
        holder.button.isClickable = true
    }

    override fun getItemCount(): Int = items.size

    fun setOptions(options: List<String>) {
        items = options
        notifyDataSetChanged()
    }

    class OptionClick(val block: (Int) -> Unit) {
        fun onClick(option: Int) = block(option)
    }
}

class ViewHolder internal constructor(
    inflater: LayoutInflater,
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    inflater.inflate(R.layout.list_item_option, parent, false)
) {
    internal val button: Button = itemView.option_item_title
}
