package com.yeahush.quickquest.ui.home.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.yeahush.quickquest.adapters.CategoryAdapter
import com.yeahush.quickquest.databinding.FragmentCategoryListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryListFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
//    @Inject
//    lateinit var viewModelFactory: CategoryListViewModelFactory

//    private val viewModel: CategoryListViewModel by lazy {
//        ViewModelProvider(this, viewModelFactory).get(CategoryListViewModel::class.java)
//    }

    private val viewModel: CategoryListViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCategoryListBinding.inflate(inflater, container, false)

        val adapter = CategoryAdapter()
        binding.categoryList.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: CategoryAdapter) {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            adapter.submitList(categories)
        }
    }
}