package com.example.greenish

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MypageFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var profileImage: ImageView

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            profileImage.setImageURI(it)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(requireContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.mypage_main, container, false)

        profileImage = view.findViewById(R.id.iv_mypage_main_profile)
        profileImage.setOnClickListener {
            requestStoragePermission()
        }

        viewPager = view.findViewById(R.id.vp_mypage_viewPager)
        viewPager.adapter = MypageFragmentAdapter(this)

        tabLayout = view.findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "내 계정"
                1 -> "환경 등록"
                2 -> "내 랭킹"
                else -> throw IllegalArgumentException("Invalid position")
            }
        }.attach()

        val settingTextView: TextView = view.findViewById(R.id.tv_mypage_text4)
        val environmentTextView: TextView = view.findViewById(R.id.tv_mypage_text5)
        val rankingTextView: TextView = view.findViewById(R.id.tv_mypage_text6)

        settingTextView.setOnClickListener { selectPage(0) }
        environmentTextView.setOnClickListener { selectPage(1) }
        rankingTextView.setOnClickListener { selectPage(2) }

        updateTextViewColors(0)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTextViewColors(position)
            }
        })

        return view
    }

    private fun requestStoragePermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                // 사용자에게 권한이 필요한 이유를 설명
                Toast.makeText(requireContext(), "Storage permission is needed to select profile image", Toast.LENGTH_LONG).show()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }

    private fun selectPage(index: Int) {
        viewPager.setCurrentItem(index, false)
    }

    private fun updateTextViewColors(selectedPage: Int) {
        view?.let { view ->
            val settingTextView: TextView = view.findViewById(R.id.tv_mypage_text4)
            val environmentTextView: TextView = view.findViewById(R.id.tv_mypage_text5)
            val rankingTextView: TextView = view.findViewById(R.id.tv_mypage_text6)

            settingTextView.setTextColor(if (selectedPage == 0) 0xFF000000.toInt() else 0xFF989898.toInt())
            environmentTextView.setTextColor(if (selectedPage == 1) 0xFF000000.toInt() else 0xFF989898.toInt())
            rankingTextView.setTextColor(if (selectedPage == 2) 0xFF000000.toInt() else 0xFF989898.toInt())
        }
    }
}