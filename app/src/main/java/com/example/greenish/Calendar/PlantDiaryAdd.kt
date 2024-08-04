package com.example.greenish

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.greenish.databinding.ActivityPlantDiaryAddBinding
import kotlinx.coroutines.launch

class PlantDiaryAdd : AppCompatActivity() {
    private lateinit var binding: ActivityPlantDiaryAddBinding
    private var plantId: Int = -1
    companion object {
        private const val token =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImdyZWVuQGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MjI3ODE4NTEsImV4cCI6MTcyMjc4NTQ1MX0.oJ3LXqtH4Oh96fvGKRzixQesYTURkNr5zSLwjP1KIig"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantDiaryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        plantId = intent.getIntExtra("plantId", -1)
        if (plantId == -1) {
            Toast.makeText(this, "유효하지 않은 식물 ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btPlantdiaryBack.setOnClickListener { finish() }
        binding.btPlantdiaryWrite.setOnClickListener { createDiary() }
    }

    private fun createDiary() {
        val title = binding.editTextTitle.text.toString()
        val content = binding.editTextContent.text.toString()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val request = CreateDiaryRequest(plantId, title, content, null) // 파일 이름은 null로 설정

        lifecycleScope.launch {
            try {
                //val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImVtYWlsMkBnbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzIyNTIzMzc1LCJleHAiOjE3MjI1MjY5NzV9.9Cnmi4gutKoxNE8ZqccopE8sPXtJZ_wTbRfjv31ddic" // 실제 토큰으로 교체해야 합니다
                val response = WateringRetrofitClient.plantApi.createDiary(token, request)

                if (response.isSuccessful) {
                    Toast.makeText(this@PlantDiaryAdd, "다이어리가 생성되었습니다.", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@PlantDiaryAdd, "다이어리 생성에 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@PlantDiaryAdd, "오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}