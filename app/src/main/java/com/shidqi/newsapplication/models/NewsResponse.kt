package com.shidqi.newsapplication.models

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)

data class SourceReference(
    val id: String,
    val name: String
)

data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: SourceReference,
    val title: String,
    val url: String,
    val urlToImage: String
)