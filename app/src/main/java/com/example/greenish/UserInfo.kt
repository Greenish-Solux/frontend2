package com.example.greenish

data class UserInfo(
    val id: Int,
    val nickname: String,
    val email: String,
    val photo: Photo?
)

data class Photo(
    val photoId: Int?,
    val url: String?
)