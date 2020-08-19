package com.yeahush.quickquest.ui.home.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yeahush.quickquest.data.local.model.Category
import com.yeahush.quickquest.databinding.ListItemCategoryBinding

class CategoryAdapter(val callback: CategoryClick) :
    ListAdapter<Category, RecyclerView.ViewHolder>(CategoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryViewHolder(
            ListItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            callback
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val category = getItem(position)
        (holder as CategoryViewHolder).bind(category)
    }

    class CategoryClick(val block: (Category) -> Unit) {
        fun onClick(category: Category) = block(category)
    }

    class CategoryViewHolder(
        val binding: ListItemCategoryBinding,
        val callback: CategoryClick
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Category) {
            binding.apply {
                categoryCallback = callback
                category = item
                executePendingBindings()
            }
        }
    }
}

private class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {

    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.categoryId == newItem.categoryId
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}