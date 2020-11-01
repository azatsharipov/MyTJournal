package com.example.mytjournal.data.model

import com.google.gson.annotations.SerializedName

class UserResponse (
    val result: User
)

class User (
    val name: String,
    @SerializedName("avatar_url")
    val ava: String
)