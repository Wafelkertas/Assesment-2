package com.shidqi.newsapplication.repository


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.shidqi.newsapplication.data.NewsPagingSource
import com.shidqi.newsapplication.database.NewsDatabase
import com.shidqi.newsapplication.models.Article
import com.shidqi.newsapplication.models.SourceResponse
import com.shidqi.newsapplication.models.dbEntity.ArticleEntity
import com.shidqi.newsapplication.models.dbEntity.toArticle
import com.shidqi.newsapplication.models.dbEntity.toArticleEntity
import com.shidqi.newsapplication.service.IRetrofit
import com.shidqi.newsapplication.utils.NEWS_API_PAGE_SIZE

class NewsRepository(
    private val retrofit: IRetrofit,
    private val database: NewsDatabase,
) {


    private val databaseDao = database.newsDao()

    /**
     * call getSource in IRetrofit service
     * */
    suspend fun getSource(category: String): SourceResponse {
        return retrofit.getSources(category = category)
    }

    fun getSearchResultStream(
        sourcesQuery: String,
        searchQuery: String,
        context: Context
    ): LiveData<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = NEWS_API_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(sourcesQuery, searchQuery, context) }
        ).liveData
    }

    suspend fun getNewsFromDatabase(): List<Article> {
        val data = databaseDao.queryArticle()
        return data.map {
            it.toArticle()
        }
    }

    suspend fun insertArticle(article: Article) {
        databaseDao.insertNews(article.toArticleEntity())
    }

    suspend fun deleteArticle(article: Article) {
        databaseDao.deleteNews(article.toArticleEntity())
    }

    suspend fun findNews(article: Article) : Boolean{
        val data= databaseDao.findNews(article.url)
        return data != null
    }


}