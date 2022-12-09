package com.shidqi.newsapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.shidqi.newsapplication.data.NewsPagingSource
import com.shidqi.newsapplication.models.Article
import com.shidqi.newsapplication.models.NewsResponse
import com.shidqi.newsapplication.models.SourceResponse
import com.shidqi.newsapplication.service.IRetrofit
import com.shidqi.newsapplication.utils.NEWS_API_PAGE_SIZE

class NewsRepository(private val retrofit: IRetrofit) {

    /**
     * call getNews in IRetrofit service
     * */
    suspend fun getNews(category: String, page:Int, query:String): NewsResponse {
        return retrofit.getNews(category = category, page = page, query = query)
    }

    /**
     * call getAllNews in IRetrofit service
     * */
    suspend fun getAllNews( page:Int, query:String): NewsResponse {
        return retrofit.getAllNews( page = page, query = "",query, pageSize = 30)
    }

    /**
     * call getSource in IRetrofit service
     * */
    suspend fun getSource(category: String): SourceResponse {
        return retrofit.getSources(category = category)
    }

    fun getSearchResultStream(sourcesQuery: String, searchQuery: String,context : Context): LiveData<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = NEWS_API_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(sourcesQuery, searchQuery, context) }
        ).liveData
    }



}