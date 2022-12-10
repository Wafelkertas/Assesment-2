package com.shidqi.newsapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shidqi.newsapplication.models.dbEntity.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1,exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}