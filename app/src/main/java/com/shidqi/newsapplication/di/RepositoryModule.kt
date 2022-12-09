package com.shidqi.newsapplication.di

import com.shidqi.newsapplication.data.NewsPagingSource
import com.shidqi.newsapplication.repository.NewsRepository
import com.shidqi.newsapplication.service.IRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Repository module instantiate as singleton so it will live as long the app running
     * **/
    @Singleton
    @Provides
    fun provideNewsRepository(retrofitService: IRetrofit,): NewsRepository {
        return NewsRepository(retrofitService)
    }


}