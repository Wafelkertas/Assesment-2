package com.shidqi.newsapplication.ui.search

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.shidqi.newsapplication.models.Article
import com.shidqi.newsapplication.models.SourceData
import com.shidqi.newsapplication.utils.Resource
import com.shidqi.newsapplication.repository.NewsRepository
import com.shidqi.newsapplication.utils.NEWS_API_PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(val newsRepository: NewsRepository, @ApplicationContext context: Context) : ViewModel() {

    var selectedTab = 0
    var searchQuery: String = "United States"
    var isLoading: Boolean = false
    val listOfNews: MutableLiveData<Resource<List<Article>>> by lazy { MutableLiveData<Resource<List<Article>>>() }
    val listOfSources: MutableLiveData<Resource<List<SourceData>>> by lazy { MutableLiveData<Resource<List<SourceData>>>() }
    var allListOfSource: List<SourceData> = listOf()
    var page = 1
    val currentQuery = MutableLiveData("abc-news")
    val newsListWithPaging = currentQuery.switchMap {
        newsRepository.getSearchResultStream("",it, context).cachedIn(viewModelScope)
    }

    init {
        getAllSource()
    }



    private fun getAllSource() {
        viewModelScope.launch {
            listOfSources.value = Resource.Loading()
            try {
                val response = newsRepository.getSource("")

                if (response.sources.isNotEmpty()) {
                    listOfSources.value = Resource.Success(response.sources)
                    allListOfSource = response.sources
                } else {
                    listOfSources.value = Resource.Error("List is Empty")
                }
            } catch (e: HttpException) {
                listOfSources.value = Resource.Error(e.message())

            }
        }
    }

    fun filter(query: String) {
        if (query.isNotEmpty()) {
            val filtered = allListOfSource.filter {
                it.name.contains(query, true)
            }
            if (filtered.isNotEmpty()){
                listOfSources.value = Resource.Success(filtered)
            }else{
                listOfSources.value = Resource.Error("List of Empty")
            }
        } else {
            listOfSources.value = Resource.Success(allListOfSource)
        }

    }


}