package com.example.greenish.StartPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.greenish.MainActivity
import com.example.greenish.StartServer.ErrorResponse
import com.example.greenish.StartServer.LoginRequest
import com.example.greenish.StartServer.RetrofitClient
import com.example.greenish.databinding.ActivityLoginBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

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

                // 실제 로그인 요청은 주석 처리합니다
                // val response = apiService.login(LoginRequest(email, password))

                // 항상 로그인 성공으로 처리합니다
                Log.d(TAG, "Login successful (simulated)")
                showToast("로그인 성공")
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()

            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error occurred", e)
                showToast("오류가 발생했습니다: ${e.message ?: "알 수 없는 오류"}")

                // 오류 발생 시에도 MainActivity로 이동합니다
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun handleLoginError(statusCode: Int, errorBody: String?) {
        Log.e(TAG, "Handling login error. Status code: $statusCode, Error body: $errorBody")
        val errorMessage = when (statusCode) {
            400, 401 -> {
                errorBody?.let {
                    try {
                        val errorResponse = Gson().fromJson(it, ErrorResponse::class.java)
                        Log.d(TAG, "Parsed error response: $errorResponse")
                        errorResponse?.error
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing error response", e)
                        null
                    }
                } ?: "아이디 또는 비밀번호가 올바르지 않습니다."
            }
            else -> "로그인 실패: 알 수 없는 오류가 발생했습니다."
        }
        Log.e(TAG, "Login error message: $errorMessage")
        showToast(errorMessage)
    }


    private fun showToast(message: String) {
        Log.d(TAG, "Showing toast: $message")
        runOnUiThread {
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}