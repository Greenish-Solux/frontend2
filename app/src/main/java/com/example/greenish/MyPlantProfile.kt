package com.example.greenish

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.greenish.databinding.ActivityMyPlantProfileBinding
import kotlinx.coroutines.launch
import java.io.File


class MyPlantProfile : AppCompatActivity() {

    private lateinit var binding: ActivityMyPlantProfileBinding
    private var plantId: Int = -1
    private lateinit var diaryAdapter: DiaryAdapter
    private val diaryList = mutableListOf<DiaryPost>()

    companion object {
        const val ADD_DIARY_REQUEST = 1
        private const val token =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IjBAZ21haWwuY29tIiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTcyMjY5OTI4MywiZXhwIjoxNzIyNzAyODgzfQ.BIZB_wiEie8XjHc8PKXOx2FVor2pVoVkwSV_MUpLje8"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMyPlantProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        plantId = intent.getIntExtra("plantId", -1)
        if (plantId != -1) {
            fetchPlantDetail(plantId)
            setupRecyclerView()
            fetchDiaryPosts(plantId)
        } else {
            Toast.makeText(this, "유효하지 않은 식물 ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btProfileBack.setOnClickListener {
            finish()
        }
        binding.btProfileDelete.setOnClickListener {
            deletePlant()
        }
        // 기록하기 버튼 클릭 리스너 설정
        binding.btProfileDiaryadd.setOnClickListener {
            val intent = Intent(this, PlantDiaryAdd::class.java)
            intent.putExtra("plantId", plantId)
            startActivityForResult(intent, ADD_DIARY_REQUEST)
        }
    }

    private fun deletePlant() {
        lifecycleScope.launch {
            try {
                //val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImVtYWlsMkBnbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzIyNTIzMzc1LCJleHAiOjE3MjI1MjY5NzV9.9Cnmi4gutKoxNE8ZqccopE8sPXtJZ_wTbRfjv31ddic" // 실제 토큰으로 교체해야 합니다
                val response = WateringRetrofitClient.plantApi.deletePlant(token, plantId)

                if (response.isSuccessful && response.body()?.status == "200 OK") {
                    Toast.makeText(this@MyPlantProfile, "식물이 삭제되었습니다", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@MyPlantProfile, "식물이 삭제되었습니다", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                //Toast.makeText(this@MyPlantProfile, "오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //개별 식물 정보 조회 기능
    private fun fetchPlantDetail(plantId: Int) {
        lifecycleScope.launch {
            try {
                //val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImVtYWlsMkBnbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzIyNTIzMzc1LCJleHAiOjE3MjI1MjY5NzV9.9Cnmi4gutKoxNE8ZqccopE8sPXtJZ_wTbRfjv31ddic" // 실제 토큰으로 교체해야 합니다
                val response = WateringRetrofitClient.plantApi.getPlantDetail(token, plantId)

                if (response.isSuccessful) {
                    val plantDetailResponse = response.body()
                    plantDetailResponse?.data?.let { plantDetail ->
                        updateUI(plantDetail)
                    }
                    val plantDetail = response.body()?.data
                    plantDetail?.let { updateUI(it) }
                } else {
                    Toast.makeText(this@MyPlantProfile, "식물 정보 조회에 실패했습니다", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MyPlantProfile, "오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(plantDetail: PlantDetail) {
        Log.d("MyPlantProfile", "Plant Detail: $plantDetail")
        binding.plantName.text = plantDetail.name
        binding.waterAlarm.text = if (plantDetail.isAlarm) "물주기 알림 켜짐" else "물주기 알림 켜짐"
        binding.tvProfileDiaryPlantname.text = "${plantDetail.name}의 성장일기"

        // 항상 placeholder 이미지 사용
        binding.plantImage.setImageResource(R.drawable.placeholderimage)
    }

    private fun setupRecyclerView() {
        diaryAdapter = DiaryAdapter(diaryList) { post ->
            val intent = Intent(this, MyPlantDiaryInside::class.java)
            intent.putExtra("postId", post.postId)
            startActivity(intent)
        }
        binding.mRecyclerView.adapter = diaryAdapter
        binding.mRecyclerView.layoutManager = LinearLayoutManager(this)
    }
    private fun fetchDiaryPosts(plantId: Int) {
        lifecycleScope.launch {
            try {
                //val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImVtYWlsMkBnbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzIyNTIzMzc1LCJleHAiOjE3MjI1MjY5NzV9.9Cnmi4gutKoxNE8ZqccopE8sPXtJZ_wTbRfjv31ddic" // 실제 토큰으로 교체해야 합니다
                val response = WateringRetrofitClient.plantApi.getDiaryPosts(token, plantId)

                if (response.isSuccessful) {
                    val posts = response.body()?.data ?: emptyList()
                    diaryList.clear()
                    diaryList.addAll(posts)
                    diaryAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@MyPlantProfile, "다이어리 목록 조회에 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MyPlantProfile, "오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_DIARY_REQUEST && resultCode == RESULT_OK) {
            fetchDiaryPosts(plantId)
        }
    }

}