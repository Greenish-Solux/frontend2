package com.example.greenish

data class PlantListResponse(
    val success: Boolean,
    val count: Int,
    val data: List<PlantListItem>
)

data class PlantListItem(
    val plantId: Int,
    val photo: Photo?
)

data class Photoz(
    val photoId: Int,
    val url: String
)

data class PlantDetailResponse(
    val success: Boolean,
    val count: Int,
    val data: PlantDetail
)

data class PlantDetail(
    val id: Int,
    val name: String,
    val distbNm: String, // 식물 종류(type)
    val age: String,
    val isAlarm: Boolean,
    val photo: Photo?
)
data class PlantRequest(
    val distbNm: String,
    val name: String,
    val age: String,
    val isAlarm: Boolean,
    val filename: String?  // 추가된 부분
)

data class PlantResponsez(
    val status: String?,
    val plant_id: Int?,
    val url: String?  // 추가된 부분
)

data class DeletePlantResponse(
    val status: String
)

data class DiaryPost(
    val postId: Int,
    val userId: Int,
    val plantId: Int,
    val title: String,
    val content: String,
    val createdAt: String,
    val photo: Photo?
)

data class CreateDiaryRequest(
    val plantId: Int,
    val title: String,
    val content: String,
    val fileName: String?
)

data class CreateDiaryResponse(
    val postId: Int,
    val photo: Photo?
)
data class DiaryPostsResponse(
    val success: Boolean,
    val data: List<DiaryPost>
)
data class DiaryPostResponse(
    val success: Boolean,
    val data: DiaryPost
)