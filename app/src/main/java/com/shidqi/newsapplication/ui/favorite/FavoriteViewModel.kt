package com.shidqi.newsapplication.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shidqi.newsapplication.models.Article
import com.shidqi.newsapplication.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {
    val listOfNews: MutableLiveData<List<Article>> by lazy { MutableLiveData<List<Article>>() }

    init {
        queryAllNews()
    }

    fun deleteNews(article: Article) {
        viewModelScope.launch {
            newsRepository.deleteArticle(article)
        }
        queryAllNews()
    }

    private fun queryAllNews() {

        CoroutineScope(Dispatchers.IO).launch {
            val data = newsRepository.getNewsFromDatabase()

            listOfNews.postValue(data)
        }
    }
}