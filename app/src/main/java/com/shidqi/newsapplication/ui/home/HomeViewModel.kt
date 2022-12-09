package com.shidqi.newsapplication.ui.home

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shidqi.newsapplication.data.NewsPagingSource
import com.shidqi.newsapplication.models.Article
import com.shidqi.newsapplication.models.SourceData
import com.shidqi.newsapplication.utils.Resource
import com.shidqi.newsapplication.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    /**
     * Variable to hold some api parameter or loading state
     * **/
    var page: Int = 1
    var category = "business"
    var categoryPosition = 0
    var isLoading: Boolean = false

    /**
     * Function to call news api
     * **/
    val currentQuery = MutableLiveData("abc-news")
    val newsListWithPaging = currentQuery.switchMap {
        newsRepository.getSearchResultStream(it,"", context).cachedIn(viewModelScope)
    }

    /**
     * LiveData objects as container for data that being used in the view
     * **/
    val listOfSource: MutableLiveData<Resource<List<SourceData>>> by lazy { MutableLiveData<Resource<List<SourceData>>>() }
    val showingNews: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    /**
     * This function is called when viewModel instantiate
     * **/
    init {

        getSources()
    }

    /**
     * Function to call source api
     * **/
    fun getSources() {
        viewModelScope.launch {
            listOfSource.value = Resource.Loading()
            isLoading = true
            delay(2000)
            try {
                val response = newsRepository.getSource(category = category)
                if (response.sources.isNotEmpty()) {
                    listOfSource.value = Resource.Success(response.sources)
                    showingNews.value = true
                } else {
                    listOfSource.value = Resource.Error("List Is Empty")
                }
                isLoading = false
            } catch (e: HttpException) {
                listOfSource.value = Resource.Error(message = e.message())
                isLoading = false
            } catch (e: IOException) {
                listOfSource.value = Resource.Error(message = "No Network")
                isLoading = false
            }
        }
    }





}