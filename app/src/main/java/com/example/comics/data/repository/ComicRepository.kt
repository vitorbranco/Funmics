package com.example.comics.data.repository

import com.example.comics.data.remote.Api
import retrofit2.await

class ComicRepository(
    private val api: Api,
) {

    suspend fun getComics() = api.getComics(
        apikey = "b7e14bab409c70a5c4e7c2b319c09d7b",
        ts = "1682982412",
        hash = "3482f01e9bf207a437a4b621c91339ad"
    ).await()
}