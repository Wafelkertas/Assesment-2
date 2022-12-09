package com.shidqi.newsapplication.data

import android.content.Context
import android.util.Log
import androidx.paging.*
import com.shidqi.newsapplication.di.NetworkModule
import com.shidqi.newsapplication.models.Article
import com.shidqi.newsapplication.service.IRetrofit
import com.shidqi.newsapplication.utils.NEWS_API_PAGE_SIZE
import com.shidqi.newsapplication.utils.NEWS_API_STARTING_PAGE_INDEX
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class NewsPagingSource(
    private val sourcesQuery:String,
    private val searchQuery:String,
     private val context: Context
) :PagingSource<Int, Article>() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RetrofitInstance {
        fun retrofit(): IRetrofit
    }
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: NEWS_API_STARTING_PAGE_INDEX
         return try {
                 val retrofitInstance = EntryPointAccessors.fromApplication(context = context, NetworkModule.IRetrofitEntryPoint::class.java)

                val response = retrofitInstance.retrofitInstance().getAllNews(position, searchQuery,sourcesQuery,NEWS_API_PAGE_SIZE)
                val repos = response.articles
                val nextKey = if (repos.isEmpty()) {
                    null
                } else {
                    // initial load size = 3 * NETWORK_PAGE_SIZE
                    // ensure we're not requesting duplicating items, at the 2nd request
                    position + (params.loadSize / NEWS_API_PAGE_SIZE)
                }
                 LoadResult.Page(
                    data = repos,
                    prevKey = if (position == NEWS_API_STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = nextKey
                )

        }catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }



}



