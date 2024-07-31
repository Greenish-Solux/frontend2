package com.example.greenish.StartPage

// SignUpActivity.kt
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.greenish.StartServer.RetrofitClient
import com.example.greenish.StartServer.User
import com.example.greenish.databinding.ActivitySignupBinding
import kotlinx.coroutines.launch


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val apiService = RetrofitClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val nickname = binding.editUserName.text.toString()
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            val confirmPassword = binding.editConfirmPassword.text.toString()

            if (validateInputs(nickname, email, password, confirmPassword)) {
                lifecycleScope.launch {
                    signUp(nickname, email, password)
                }
            }
        }

        binding.backSignup.setOnClickListener {
            finish()
        }
    }

    private fun validateInputs(nickname: String, email: String, password: String, confirmPassword: String): Boolean {
        if (nickname.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            showToast("모든 필드를 입력해주세요.")
            return false
        }
        if (password != confirmPassword) {
            showToast("비밀번호가 일치하지 않습니다.")
            return false
        }
        return true
    }

    private suspend fun signUp(nickname: String, email: String, password: String) {
        try {
            val isEmailDuplicate = apiService.checkEmail(email).body() ?: false
            if (isEmailDuplicate) {
                showToast("이미 사용 중인 이메일입니다.")
                return
            }

            val isNicknameDuplicate = apiService.checkNickname(nickname).body() ?: false
            if (isNicknameDuplicate) {
                showToast("이미 사용 중인 닉네임입니다.")
                return
            }

            val response = apiService.signUp(User(email, password, nickname))
            if (response.isSuccessful) {
                showToast("회원가입이 완료되었습니다.")
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                showToast("회원가입에 실패했습니다. 다시 시도해주세요.")
            }
        } catch (e: Exception) {
            showToast("오류가 발생했습니다: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}