package com.example.comics.ui.mapper

import com.example.comics.data.model.ComicModel
import com.example.comics.ui.ComicItemVO

fun ComicModel.toVOList(): List<ComicItemVO> {
    return data.results.map { result ->
        ComicItemVO(
            id = result.id,
            image = "${result.thumbnail.path}.${result.thumbnail.extension}".replace(
                "http://",
                "https://"
            ),
            title = result.title,
            subtitle = if (result.description.isNullOrEmpty()) "No description available" else result.description
        )
    }
}