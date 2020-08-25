package com.yeahush.quickquest.ui.trivia

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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
        holder.button.apply {
            text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(items[position], Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(items[position])
            }
            background = ResourcesCompat.getDrawable(resources, R.drawable.bg_button, null)
            setOnClickListener { callback.onClick(position) }
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_right_swipe))
        }
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
