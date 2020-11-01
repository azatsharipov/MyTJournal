package com.example.mytjournal.data.repository

import com.example.mytjournal.data.api.TJournalApiFactory
import com.example.mytjournal.data.model.Post
import com.example.mytjournal.data.model.User

class TJournalRepository {
    suspend fun getPosts(count: Int = 15, offset: Int = 0): MutableList<Post>? {
        val api = TJournalApiFactory.api
        val result = api
            .getPosts("1", "top/all", count, offset)
            .await()
        return result.body()?.result
    }

    suspend fun auth(token: String): User? {
        val api = TJournalApiFactory.api
        val result = api
            .auth(token)
            .await()
        return result.body()?.result
    }
}