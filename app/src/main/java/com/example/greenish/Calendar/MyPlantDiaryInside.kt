package com.example.greenish

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.greenish.databinding.ActivityMyPlantDiaryInsideBinding
import kotlinx.coroutines.launch

class MyPlantDiaryInside : AppCompatActivity() {
    private lateinit var binding: ActivityMyPlantDiaryInsideBinding

    companion object {
        private const val token =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImVtYWlsNkBnbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzIyNzE1Nzc2LCJleHAiOjE3MjI3MTkzNzZ9.xEVSUJY0JXSB1LZ1bz5Ul7jCftKa-3ZvB4H4lnBbdXY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPlantDiaryInsideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postId = intent.getIntExtra("postId", -1)
        if (postId != -1) {
            fetchDiaryPost(postId)
        } else {
            Toast.makeText(this, "유효하지 않은 다이어리 ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btPlantdiaryBack.setOnClickListener { finish() }
    }

    private fun fetchDiaryPost(postId: Int) {
        lifecycleScope.launch {
            try {
                //val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImVtYWlsMkBnbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzIyNTIzMzc1LCJleHAiOjE3MjI1MjY5NzV9.9Cnmi4gutKoxNE8ZqccopE8sPXtJZ_wTbRfjv31ddic" // 실제 토큰으로 교체해야 합니다
                val response = WateringRetrofitClient.plantApi.getDiaryPost(token, postId)

                if (response.isSuccessful) {
                    val post = response.body()?.data
                    post?.let { updateUI(it) }
                } else {
                    Toast.makeText(this@MyPlantDiaryInside, "다이어리 조회에 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MyPlantDiaryInside, "오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(post: DiaryPost) {
        binding.titleArea.text = post.title
        binding.timeArea.text = post.createdAt
        binding.contentArea.text = post.content
    }
}