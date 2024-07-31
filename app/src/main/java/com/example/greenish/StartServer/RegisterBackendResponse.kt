package com.example.greenish.StartServer

data class RegisterBackendResponse(
    val id: Long,
    val message: String // 서버에서 오는 message 필드 추가
)
