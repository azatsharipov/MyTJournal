package com.example.mytjournal.data.model

import com.google.gson.annotations.SerializedName

class PostsResponse (
    val result: MutableList<Post>
)

class Post (
    val title: String,
    val cover: Cover?
)

class Cover (
//    val additionalData: AdditionalData,
    val url: String
)

class AdditionalData (
    val type: String,
    val url: String
)