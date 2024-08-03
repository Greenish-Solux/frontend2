package com.example.greenish.SearchpageANDMypage

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