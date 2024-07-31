package com.example.greenish.StartPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.greenish.StartServer.ErrorResponse
import com.example.greenish.StartServer.RetrofitClient
import com.example.greenish.StartServer.User
import com.example.greenish.databinding.ActivitySignupBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val apiService = RetrofitClient.instance
    private val TAG = "SignUpActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val nickname = binding.editUserName.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString()
            val confirmPassword = binding.editConfirmPassword.text.toString()

            if (validateInputs(nickname, email, password, confirmPassword)) {
                signUp(nickname, email, password)
            }
        }

        binding.backSignup.setOnClickListener {
            finish()
        }
    }

    private fun validateInputs(nickname: String, email: String, password: String, confirmPassword: String): Boolean {
        if (nickname.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            showToast("모든 필드를 입력해주세요.")
            Log.d(TAG, "Input validation failed: One or more fields are blank")
            return false
        }
        if (password != confirmPassword) {
            showToast("비밀번호가 일치하지 않습니다.")
            Log.d(TAG, "Input validation failed: Passwords do not match")
            return false
        }
        Log.d(TAG, "Input validation passed")
        return true
    }

    private fun signUp(nickname: String, email: String, password: String) {
        Log.d(TAG, "Sign up attempt started for email: $email")
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                showToast("회원가입 시도 중...")

                val isEmailDuplicate = withContext(Dispatchers.IO) {
                    apiService.checkEmail(email).body() ?: false
                }
                if (isEmailDuplicate) {
                    Log.d(TAG, "Email duplication check failed")
                    showToast("이미 사용 중인 이메일입니다.")
                    return@launch
                }

                val isNicknameDuplicate = withContext(Dispatchers.IO) {
                    apiService.checkNickname(nickname).body() ?: false
                }
                if (isNicknameDuplicate) {
                    Log.d(TAG, "Nickname duplication check failed")
                    showToast("이미 사용 중인 닉네임입니다.")
                    return@launch
                }

                val response = withContext(Dispatchers.IO) {
                    apiService.signUp(User(email, password, nickname))
                }

                when (response.code()) {
                    200, 201 -> {
                        Log.d(TAG, "Sign up successful")
                        showToast("회원가입이 완료되었습니다.")
                        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else -> {
                        val errorBody = response.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        Log.e(TAG, "Sign up failed. Error: ${errorResponse.error}")
                        showToast(errorResponse.error ?: "회원가입에 실패했습니다. 다시 시도해주세요.")
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "Network error occurred", e)
                showToast("네트워크 오류가 발생했습니다. 인터넷 연결을 확인해 주세요.")
            } catch (e: HttpException) {
                Log.e(TAG, "HTTP exception occurred", e)
                showToast("서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.")
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error occurred", e)
                showToast("오류가 발생했습니다: ${e.message ?: "알 수 없는 오류"}")
            }
        }
    }

    private fun showToast(message: String) {
        Log.d(TAG, "Showing toast: $message")
        runOnUiThread {
            Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}