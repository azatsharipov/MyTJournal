package com.example.mytjournal.data.repository

import com.example.mytjournal.data.api.TJournalApiFactory
import com.example.mytjournal.data.model.Post
import com.example.mytjournal.data.model.PostsResponse
import com.example.mytjournal.data.model.User
import com.example.mytjournal.data.model.UserResponse
import retrofit2.Response

class TJournalRepository {
    suspend fun getPosts(count: Int = 15, offset: Int = 0): Response<PostsResponse> {
        val api = TJournalApiFactory.api
        val result = api
            .getPosts("237832", "new", count, offset)
            .await()
        return result
    }

    suspend fun auth(token: String): Response<UserResponse> {
        val api = TJournalApiFactory.api
        val result = api
            .auth(token)
            .await()
        return result
    }
}