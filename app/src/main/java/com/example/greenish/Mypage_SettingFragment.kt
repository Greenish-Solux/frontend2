package com.example.greenish

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class Mypage_SettingFragment : Fragment() {

    private var isToggle1On = false
    private var isToggle2On = false
    private var isToggle3On = false
    private var isToggle4On = false
    private var isToggle5On = false
    private var isToggle6On = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mypage_setting, container, false)

        // Find ImageView by ID
        val toggle1: ImageView = view.findViewById(R.id.iv_setting_toggle1)
        val toggle2: ImageView = view.findViewById(R.id.iv_setting_toggle2)
        val toggle3: ImageView = view.findViewById(R.id.iv_setting_toggle3)
        val toggle4: ImageView = view.findViewById(R.id.iv_setting_toggle4)
        val toggle5: ImageView = view.findViewById(R.id.iv_setting_toggle5)
        val toggle6: ImageView = view.findViewById(R.id.iv_setting_toggle6)

        // Toggle functionality for toggle1
        toggle1.setOnClickListener {
            isToggle1On = !isToggle1On
            isToggle2On = isToggle1On
            isToggle3On = isToggle1On
            setToggleImages(toggle1, isToggle1On)
            setToggleImages(toggle2, isToggle1On)
            setToggleImages(toggle3, isToggle1On)
        }

        // Toggle functionality for toggle4
        toggle4.setOnClickListener {
            isToggle4On = !isToggle4On
            isToggle5On = isToggle4On
            isToggle6On = isToggle4On
            setToggleImages(toggle4, isToggle4On)
            setToggleImages(toggle5, isToggle4On)
            setToggleImages(toggle6, isToggle4On)
        }

        // Individual toggle functionality for toggle2
        toggle2.setOnClickListener {
            isToggle2On = !isToggle2On
            setToggleImages(toggle2, isToggle2On)
        }

        // Individual toggle functionality for toggle3
        toggle3.setOnClickListener {
            isToggle3On = !isToggle3On
            setToggleImages(toggle3, isToggle3On)
        }

        // Individual toggle functionality for toggle5
        toggle5.setOnClickListener {
            isToggle5On = !isToggle5On
            setToggleImages(toggle5, isToggle5On)
        }

        // Individual toggle functionality for toggle6
        toggle6.setOnClickListener {
            isToggle6On = !isToggle6On
            setToggleImages(toggle6, isToggle6On)
        }

        // Logout TextView 클릭 리스너 설정
        val logout: TextView = view.findViewById(R.id.tv_setting_logout)
        logout.setOnClickListener {
            showLogoutDialog()
        }

        val delete: TextView = view.findViewById(R.id.tv_setting_delete)
        delete.setOnClickListener {
            showDeleteDialog()
        }

        return view
    }

    private fun setToggleImages(imageView: ImageView, isOn: Boolean) {
        if (isOn) {
            imageView.setImageResource(R.drawable.ic_setting_on)
        } else {
            imageView.setImageResource(R.drawable.ic_setting_off)
        }
    }

    private fun showLogoutDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogView)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val logoutButton: View = dialogView.findViewById(R.id.iv_logout_ok)
        val cancelButton: View = dialogView.findViewById(R.id.iv_logout_cancel)

        logoutButton.setOnClickListener {
            // 여기에 로그아웃 로직 추가
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

    private fun showDeleteDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogView)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val DeleteButton: View = dialogView.findViewById(R.id.iv_delete_ok)
        val cancelButton: View = dialogView.findViewById(R.id.iv_delete_cancel)

        DeleteButton.setOnClickListener {
            dialog.dismiss()
            showTemporaryDialog()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        // 다이얼로그 크기 설정
        dialog.window?.setLayout(
            (342 * resources.displayMetrics.density).toInt(),
            (162 * resources.displayMetrics.density).toInt()
        )
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