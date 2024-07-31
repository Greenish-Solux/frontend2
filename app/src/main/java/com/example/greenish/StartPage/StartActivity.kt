package com.example.greenish.StartPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.greenish.databinding.ActivityStartBinding
import com.example.greenish.MainActivity

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 버튼 클릭 시
        binding.llStart.setOnClickListener {
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 버튼 클릭 시
        binding.btnStart.setOnClickListener {
            val intent = Intent(this@StartActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    // 로그인 성공 시 MainActivity로 이동하는 메서드
    fun goToMainActivity() {
        val intent = Intent(this@StartActivity, MainActivity::class.java)
        startActivity(intent)
        finish() // StartActivity 종료
    }
}