package com.example.mytjournal.data.model

import com.google.gson.annotations.SerializedName

class UserResponse (
    val result: User
)

class User (
    val name: String,
    val avatar_url: String
)