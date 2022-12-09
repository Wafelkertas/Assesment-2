package com.shidqi.newsapplication.di

import android.content.Context
import com.shidqi.newsapplication.network.NetworkConnectionInterceptor
import com.shidqi.newsapplication.utils.BASE_URL
import com.shidqi.newsapplication.service.IRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Providing retrofit instance for repositories to use
     *  **/
    @Singleton
    @Provides
    fun provideRetrofitService(@ApplicationContext context : Context ): IRetrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().connectTimeout(150, TimeUnit.SECONDS).readTimeout(150, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(Interceptor {
                val builder = it
                    .request()
                    .newBuilder()
                    .addHeader("Accept", "application/json")



                return@Interceptor it.proceed(
                    builder.build()
                )
            }).addInterceptor(Interceptor { chain ->
                // Inject query parameters
                val original: Request = chain.request()
                val originalHttpUrl: HttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apiKey", "aa52150edc55464fa3db911cc8fac360")
                    .build()

                // Request customization: add request headers
                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(url)
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            }).addInterceptor(NetworkConnectionInterceptor(context))
            .build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build().create(IRetrofit::class.java)
    }

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface IRetrofitEntryPoint {
        fun retrofitInstance(): IRetrofit
    }


}

