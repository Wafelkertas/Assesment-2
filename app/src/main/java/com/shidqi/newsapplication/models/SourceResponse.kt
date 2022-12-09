package com.shidqi.newsapplication.models

data class SourceResponse(
    val sources: List<SourceData>,
    val status: String
)

data class SourceData(
    val category: String,
    val country: String,
    val description: String,
    val id: String,
    val language: String,
    val name: String,
    val url: String
)