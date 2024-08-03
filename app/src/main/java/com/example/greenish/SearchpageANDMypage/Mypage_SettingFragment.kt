package com.example.greenish.SearchpageANDMypage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.greenish.R
import com.example.greenish.StartPage.StartActivity
import kotlinx.coroutines.launch
import retrofit2.HttpException

class Mypage_SettingFragment : Fragment() {

    private lateinit var toggle1: ImageView
    private lateinit var toggle2: ImageView
    private lateinit var toggle3: ImageView
    private lateinit var toggle4: ImageView

    private var isToggle1On = false
    private var isToggle2On = false
    private var isToggle3On = false
    private var isToggle4On = false

    private val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImdyZWVuQGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MjI3MDk5NDksImV4cCI6MTcyMjcxMzU0OX0.M4otbCF5e55aRCejeXxvx-J4KABkMf7_jvpG9cw1LCI"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_setting, container, false)

        toggle1 = view.findViewById(R.id.iv_setting_toggle1)
        toggle2 = view.findViewById(R.id.iv_setting_toggle2)
        toggle3 = view.findViewById(R.id.iv_setting_toggle3)
        toggle4 = view.findViewById(R.id.iv_setting_toggle4)

        setupToggleListeners()
        setupLogoutAndDeleteListeners(view)

        fetchAlarmSettings()

        return view
    }

    private fun setupToggleListeners() {
        toggle1.setOnClickListener { updateToggle(0) }
        toggle2.setOnClickListener { updateToggle(1) }
        toggle3.setOnClickListener { updateToggle(2) }
        toggle4.setOnClickListener { updateToggle(3) }
    }

    private fun updateToggle(index: Int) {
        when (index) {
            0 -> {
                isToggle1On = !isToggle1On
                isToggle2On = isToggle1On
                isToggle3On = isToggle1On
                isToggle4On = isToggle1On
                setToggleImages(toggle1, isToggle1On)
                setToggleImages(toggle2, isToggle2On)
                setToggleImages(toggle3, isToggle3On)
                setToggleImages(toggle4, isToggle4On)
            }
            1 -> {
                isToggle2On = !isToggle2On
                setToggleImages(toggle2, isToggle2On)
            }
            2 -> {
                isToggle3On = !isToggle3On
                setToggleImages(toggle3, isToggle3On)
            }
            3 -> {
                isToggle4On = !isToggle4On
                setToggleImages(toggle4, isToggle4On)
            }
        }
        updateAlarmSettings()
    }

    private fun setToggleImages(imageView: ImageView, isOn: Boolean) {
        imageView.setImageResource(if (isOn) R.drawable.ic_setting_on else R.drawable.ic_setting_off)
    }

    private fun fetchAlarmSettings() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getAlarmSettings(token)
                if (response.isSuccessful) {
                    response.body()?.let { settings ->
                        isToggle1On = settings.all
                        isToggle2On = settings.hapticMode
                        isToggle3On = settings.preview
                        isToggle4On = settings.allPlantWatering
                        updateToggleUI()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch alarm settings", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAlarmSettings() {
        lifecycleScope.launch {
            try {
                val settings =
                    ApiService.AlarmSettings(isToggle1On, isToggle2On, isToggle3On, isToggle4On)
                val response = RetrofitClient.apiService.updateAlarmSettings(token, settings)
                if (!response.isSuccessful) {
                    Toast.makeText(requireContext(), "Failed to update alarm settings", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateToggleUI() {
        setToggleImages(toggle1, isToggle1On)
        setToggleImages(toggle2, isToggle2On)
        setToggleImages(toggle3, isToggle3On)
        setToggleImages(toggle4, isToggle4On)
    }

    // 기존의 setupLogoutAndDeleteListeners, showLogoutDialog, showDeleteDialog, showTemporaryDialog 메서드들은 그대로 유지
    private fun setupLogoutAndDeleteListeners(view: View) {
        val logout: TextView = view.findViewById(R.id.tv_setting_logout)
        logout.setOnClickListener {
            showLogoutDialog()
        }

        val delete: TextView = view.findViewById(R.id.tv_setting_delete)
        delete.setOnClickListener {
            showDeleteDialog()
        }
    }

    // showLogoutDialog, showDeleteDialog, showTemporaryDialog 메서드들은 그대로 유지

    private fun showLogoutDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogView)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val logoutButton: View = dialogView.findViewById(R.id.iv_logout_ok)
        val cancelButton: View = dialogView.findViewById(R.id.iv_logout_cancel)

        logoutButton.setOnClickListener {
            performLogout()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        dialog.window?.setLayout(
            (342 * resources.displayMetrics.density).toInt(),
            (162 * resources.displayMetrics.density).toInt()
        )
    }

    private fun performLogout() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.logout(token)
                // 백엔드 응답과 관계없이 StartActivity로 이동
                navigateToStartActivity()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "로그아웃 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogView)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val DeleteButton: View = dialogView.findViewById(R.id.iv_delete_ok)
        val cancelButton: View = dialogView.findViewById(R.id.iv_delete_cancel)

        DeleteButton.setOnClickListener {
            performDeleteAccount()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        dialog.window?.setLayout(
            (342 * resources.displayMetrics.density).toInt(),
            (162 * resources.displayMetrics.density).toInt()
        )
    }

    private fun performDeleteAccount() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.deleteAccount(token)
                showTemporaryDialog()
                // 3초 후 StartActivity로 이동
                Handler(Looper.getMainLooper()).postDelayed({
                    navigateToStartActivity()
                }, 3000)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "계정 삭제 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToStartActivity() {
        val intent = Intent(requireContext(), StartActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }


    // 3초 동안 보여주는 임시 다이얼로그를 표시하는 함수
    private fun showTemporaryDialog() {
        val tempDialogView = layoutInflater.inflate(R.layout.dialog_delete_complete, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(tempDialogView)

        val tempDialog = builder.create()
        tempDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        tempDialog.show()

        tempDialog.window?.setLayout(
            (342 * resources.displayMetrics.density).toInt(),
            (162 * resources.displayMetrics.density).toInt()
        )

        Handler(Looper.getMainLooper()).postDelayed({
            tempDialog.dismiss()
        }, 3000)
    }
}