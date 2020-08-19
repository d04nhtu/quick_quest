package com.yeahush.quickquest.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeahush.quickquest.ui.trivia.TriviaParamsViewModel
import com.yeahush.quickquest.ui.trivia.TriviaPlayViewModel
import com.yeahush.quickquest.ui.home.category.CategoryListViewModel
import com.yeahush.quickquest.ui.home.question.QuestionListViewModel
import com.yeahush.quickquest.utilities.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.IntoMap

@InstallIn(ApplicationComponent::class)
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CategoryListViewModel::class)
    abstract fun bindCategoryListViewModel(categoryListViewModel: CategoryListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuestionListViewModel::class)
    abstract fun bindQuestionListViewModel(questionListViewModel: QuestionListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TriviaPlayViewModel::class)
    abstract fun bindOnlineQuestionViewModel(viewModel: TriviaPlayViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TriviaParamsViewModel::class)
    abstract fun bindTriviaParamsViewModel(viewModel: TriviaParamsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelProviderFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

}