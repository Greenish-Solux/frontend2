package com.example.greenish.StartPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.greenish.StartServer.ErrorResponse
import com.example.greenish.StartServer.LoginRequest
import com.example.greenish.StartServer.RetrofitClient
import com.example.greenish.databinding.ActivityLoginBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val apiService = RetrofitClient.instance
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.editTVId.text.toString().trim()
            val password = binding.editTVPw.text.toString().trim()

            if (validateInputs(email, password)) {
                login(email, password)
            }
        }

        binding.backLogin.setOnClickListener {
            val intent = Intent(this@LoginActivity, StartActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            showToast("아이디와 비밀번호를 입력해주세요.")
            Log.d(TAG, "Input validation failed: email or password is blank")
            return false
        }
        Log.d(TAG, "Input validation passed")
        return true
    }

    private fun login(email: String, password: String) {
        Log.d(TAG, "Login attempt started for email: $email")
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                showToast("로그인 시도 중...")
                val response = apiService.login(LoginRequest(email, password))

                when (response.code()) {
                    200 -> {
                        val authToken = response.headers()["Authorization"]
                        if (authToken != null) {
                            Log.d(TAG, "Login successful, token received")
                            saveAuthToken(authToken)
                            showToast("로그인 성공")
                            startActivity(Intent(this@LoginActivity, StartActivity::class.java))
                            finish()
                        } else {
                            Log.e(TAG, "Login successful but no token received")
                            showToast("로그인 성공했지만 인증 토큰을 받지 못했습니다.")
                        }
                    }
                    401, 400 -> {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = try {
                            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                            errorResponse?.error ?: "알 수 없는 오류가 발생했습니다."
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing error response", e)
                            "로그인 실패: ${response.code()}"
                        }
                        Log.e(TAG, "Login failed. Error: $errorMessage")
                        showToast(errorMessage)
                    }
                    else -> {
                        Log.e(TAG, "Unexpected response code: ${response.code()}")
                        showToast("알 수 없는 오류가 발생했습니다. (코드: ${response.code()})")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error occurred", e)
                showToast("오류가 발생했습니다: ${e.message ?: "알 수 없는 오류"}")
            }
        }
    }

    private fun saveAuthToken(token: String) {
        val sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    private fun showToast(message: String) {
        Log.d(TAG, "Showing toast: $message")
        runOnUiThread {
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}