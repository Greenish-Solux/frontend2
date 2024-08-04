package com.example.greenish.SearchpageANDMypage

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
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.greenish.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class MypageFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var profileImage: ImageView
    private lateinit var nicknameTextView: TextView
    private lateinit var emailTextView: TextView
    private val PERMISSION_REQUEST_CODE = 100

    private val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImdyZWVuQGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MjI3ODY0MjYsImV4cCI6MTcyMjc5MDAyNn0.C_zE7N0vYIvUEyPxkEajQy1O9GNG3PKysWfknHzeBWc"

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            uploadImage(it)
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

        nicknameTextView = view.findViewById(R.id.tv_mypage_text2)
        emailTextView = view.findViewById(R.id.tv_mypage_text3)

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

        fetchUserInfo()

        return view
    }

    private fun requestStoragePermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                getRequiredPermission()
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            shouldShowRequestPermissionRationale(getRequiredPermission()) -> {
                Toast.makeText(requireContext(), "Storage permission is needed to select profile image", Toast.LENGTH_LONG).show()
            }
            else -> {
                requestPermissions(arrayOf(getRequiredPermission()), PERMISSION_REQUEST_CODE)
            }
        }
    }

    private fun getRequiredPermission(): String {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openGallery()
                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
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

    private fun fetchUserInfo() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserInfo(token)
                if (response.isSuccessful) {
                    val userInfo = response.body()
                    userInfo?.let { updateUI(it) }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch user info", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(userInfo: UserInfo) {
        nicknameTextView.text = userInfo.nickname
        emailTextView.text = userInfo.email

        if (userInfo.photo?.url != null && userInfo.photo.url.isNotBlank()) {
            fetchPlantInfo()
        } else {
            // 기본 이미지 설정

        }

        // photoId를 사용해야 하는 경우 (예: 로그 기록이나 다른 용도)
        userInfo.photo?.photoId?.let { id ->
            // photoId를 사용하는 로직
            println("Photo ID: $id")
        }
    }

    private fun fetchPlantInfo() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getPlantInfo(token, 2)
                if (response.isSuccessful) {
                    val plantInfo = response.body()
                    plantInfo?.let {
                        updateProfileImage(it.data.photoUrl)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch plant info", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateProfileImage(photoUrl: String) {
        Glide.with(this)
            .load(photoUrl)
            .into(profileImage)
    }

    private fun uploadImage(uri: Uri) {
        lifecycleScope.launch {
            try {
                val file = getFileFromUri(uri)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("photoImage", file.name, requestFile)

                val response = RetrofitClient.apiService.updateProfileImage(token, body)

                if (response.isSuccessful) {
                    val profileImageResponse = response.body()
                    profileImageResponse?.let {
                        updateProfileImage(it.photoUrl)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to update profile image", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "temp_image")
        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }
}