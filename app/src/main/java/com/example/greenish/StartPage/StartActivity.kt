package com.example.greenish.StartPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.greenish.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 바인딩 객체 초기화
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 클릭 리스너 설정
        binding.llStart.setOnClickListener {
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnStart.setOnClickListener {
            val intent = Intent(this@StartActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
