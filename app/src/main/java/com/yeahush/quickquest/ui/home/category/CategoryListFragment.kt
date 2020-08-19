package com.yeahush.quickquest.ui.home.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.yeahush.quickquest.databinding.FragmentCategoryListBinding
import com.yeahush.quickquest.ui.home.question.QuestionListFragment
import com.yeahush.quickquest.utilities.CATEGORY_KEY
import com.yeahush.quickquest.utilities.QUESTION_LIST_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryListFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CategoryListViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = CategoryAdapter(
            CategoryAdapter.CategoryClick {
                showDialog(it.categoryId)
            })
        binding.categoryList.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    private fun showDialog(productId: String) {
        val fragmentManager = childFragmentManager
        QuestionListFragment().apply {
            arguments = Bundle().apply {
                putString(CATEGORY_KEY, productId)
            }
            show(fragmentManager, QUESTION_LIST_FRAGMENT)
        }
    }

    private fun subscribeUi(adapter: CategoryAdapter) {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            adapter.submitList(categories)
        }
    }
}