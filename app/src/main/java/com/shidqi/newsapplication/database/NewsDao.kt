package com.shidqi.newsapplication.database

import androidx.room.*
import com.shidqi.newsapplication.models.dbEntity.ArticleEntity

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: ArticleEntity)

    @Query("SELECT * FROM articleEntity")
    suspend fun queryArticle(): List<ArticleEntity>

    @Delete
    suspend fun deleteNews(vararg news: ArticleEntity)

    @Query("SELECT * FROM articleEntity WHERE url LIKE :newsUrl")
    suspend fun findNews(newsUrl : String): ArticleEntity?
}