package com.example.mytjournal.data.api

import com.example.mytjournal.data.model.PostsResponse
import com.example.mytjournal.data.model.UserResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TJournalApi {
    @GET("subsite/{id}/timeline/{sorting}")
    fun getPosts(
        @Path("id") id: String,
        @Path("sorting") sorting: String,
        @Query("count") count: Int,
        @Query("offset") offset: Int
    ): Deferred<Response<PostsResponse>>

    @POST("auth_qr")
    fun auth(
        @Query("token") token: String
    ): Deferred<Response<UserResponse>>
}