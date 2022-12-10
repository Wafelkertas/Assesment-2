package com.shidqi.newsapplication.models.dbEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shidqi.newsapplication.models.Article
import com.shidqi.newsapplication.models.SourceReference

@Entity
data class ArticleEntity(
    @PrimaryKey @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "published_at")  val publishedAt: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "source_reference_id") val source_id: String,
    @ColumnInfo(name = "source_reference_name") val source_name: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "url_to_image") val urlToImage: String
)

fun ArticleEntity.toArticle() : Article{
    return Article(
        author = this.author,
        content = this.content,
        description = this.description,
        publishedAt = this.publishedAt,
        source = SourceReference(id = this.source_id, name = this.source_name),
        title = this.title,
        url = this.url,
        urlToImage = this.urlToImage
    )
}

fun Article.toArticleEntity() :ArticleEntity{
    return ArticleEntity(
        publishedAt = this.publishedAt,
        author = this.author,
        content = this.content,
        description = this.description,
        source_id = this.source.id,
        source_name = this.source.name,
        title = this.title,
        url = this.url,
        urlToImage = this.urlToImage
    )
}