package com.example.greenish.MainPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.greenish.MainActivity
import com.example.greenish.R
import com.example.greenish.databinding.ActivityGuidebookBinding

class GuidebookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuidebookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuidebookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ic_back 이미지뷰 클릭 리스너 설정
        findViewById<ImageView>(R.id.ic_back).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // Set click listeners for the banners and pass the corresponding layout resource
        binding.clGuidebookBanner1.setOnClickListener {
            showGuidebookDialog(R.layout.guidebook1, R.id.btn_guidebook1_close)
        }

        binding.clGuidebookBanner2.setOnClickListener {
            showGuidebookDialog(R.layout.guidebook2, R.id.btn_guidebook2_close)
        }
        binding.clGuidebookBanner3.setOnClickListener {
            showGuidebookDialog(R.layout.guidebook3, R.id.btn_guidebook3_close)
        }
        binding.clGuidebookBanner4.setOnClickListener {
            showGuidebookDialog(R.layout.guidebook4, R.id.btn_guidebook4_close)
        }
        binding.clGuidebookBanner5.setOnClickListener {
            showGuidebookDialog(R.layout.guidebook5, R.id.btn_guidebook5_close)
        }
        binding.clGuidebookBanner6.setOnClickListener {
            showGuidebookDialog(R.layout.guidebook6, R.id.btn_guidebook6_close)
        }
    }

    private fun showGuidebookDialog(layoutResId: Int, closeBtnId: Int) {
        // Inflate the layout based on the passed layout resource ID
        val dialogView = LayoutInflater.from(this).inflate(layoutResId, null)

        // Create the AlertDialog
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)

        val alertDialog = dialogBuilder.create()

        // Set up the close button listener
        dialogView.findViewById<View>(closeBtnId).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
