package com.example.greenish

// WateringModels.kt
data class WateringResponse(
    val success: Boolean,
    val count: Int,
    val data: List<WateringData>
)

data class WateringData(
    val wateringId: Int,
    val plantName: String,
    val status: String,
    val scheduleDate: String,
    val completeDate: String?,
    val overdays: Int
)

data class WateringCompleteResponse(
    val success: Boolean,
    val count: Int,
    val data: WateringCompleteData
)

data class WateringCompleteData(
    val id: Int
)

data class UserWateringData(
    val wateringId: Int,
    val plantId: Int,
    val status: String,
    val scheduleDate: String,
    val completeDate: String?,
    val overdays: Int
)
