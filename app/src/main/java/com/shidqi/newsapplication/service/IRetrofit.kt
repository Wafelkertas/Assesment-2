package com.shidqi.newsapplication.service


import com.shidqi.newsapplication.models.NewsResponse
import com.shidqi.newsapplication.models.SourceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IRetrofit {

    /**
     * Api getting top headlines
     * **/
    @GET("top-headlines")
    suspend fun getNews(
        @Query("category") category: String ,
        @Query("page") page: Int ,
        @Query("q") query: String
    ): NewsResponse

    /**
     * Api for Get All Sources available
     * **/
    @GET("top-headlines/sources")
    suspend fun getSources(@Query("category") category: String): SourceResponse

    /**
     * Api For Search news
     * **/
    @GET("everything")
    suspend fun getAllNews(
        @Query("page") page: Int ,
        @Query("q") query: String,
        @Query("sources") sourcesQuery:String,
        @Query("pageSize") pageSize : Int
    ): NewsResponse
}