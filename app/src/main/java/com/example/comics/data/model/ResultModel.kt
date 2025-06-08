package com.example.comics.data.model

data class ResultModel(
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnail: ThumbnailModel
)
