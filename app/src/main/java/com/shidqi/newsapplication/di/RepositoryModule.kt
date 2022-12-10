package com.shidqi.newsapplication.di

import com.shidqi.newsapplication.database.NewsDatabase
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
    fun provideNewsRepository(retrofitService: IRetrofit, database: NewsDatabase): NewsRepository {
        return NewsRepository(retrofitService, database)
    }


}