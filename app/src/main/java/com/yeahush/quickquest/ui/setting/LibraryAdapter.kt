package com.yeahush.quickquest.ui.setting

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.yeahush.quickquest.R
import com.yeahush.quickquest.data.local.model.Library

/**
 * Adapter that holds libraries.
 */
internal class LibraryAdapter(
    private val onClick: OnClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var libraries: List<Library>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        LibraryHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_library, parent, false),
            onClick
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LibraryHolder).bind(libraries[position])
    }

    override fun getItemCount() = libraries.size

    fun setLibraries(libs: List<Library>) {
        libraries = libs
        notifyDataSetChanged()
    }
}

internal typealias OnClick = (library: Library) -> Unit

internal class LibraryHolder(
    view: View,
    private val onClick: OnClick
) : RecyclerView.ViewHolder(view) {
    private var lib: Library? = null

    private var image: ImageView = view.findViewById(R.id.library_image)
    private var name: TextView = view.findViewById(R.id.library_name)
    private var description: TextView = view.findViewById(R.id.library_description)

    init {
        View.OnClickListener { lib?.let { onClick(it) } }.apply { view.setOnClickListener(this) }
    }

    @SuppressLint("CheckResult")
    fun bind(lib: Library) {
        this.lib = lib
        name.text = lib.name
        description.text = lib.description
        val request = Glide.with(image.context)
            .load(lib.imageUrl)
            .transition(withCrossFade())
            .placeholder(R.drawable.avatar_placeholder)
        if (lib.circleCrop) request.circleCrop()
        request.into(image)
    }
}
