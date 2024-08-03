package com.example.greenish

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.greenish.databinding.ActivityMyPlantAddBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MyPlantAdd : AppCompatActivity() {

    private lateinit var binding: ActivityMyPlantAddBinding
    private var imageUri: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val TAKE_PICTURE_REQUEST = 2
        private const val token =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImdyZWVuQGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MjI3MDk5NDksImV4cCI6MTcyMjcxMzU0OX0.M4otbCF5e55aRCejeXxvx-J4KABkMf7_jvpG9cw1LCI"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPlantAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addPhotoButton2.setOnClickListener {
            showImageSourceDialog()
        }

        binding.plantinfoAddButton.setOnClickListener {
            val name = binding.nameArea.text.toString()
            val type = binding.plantTypeArea.text.toString()
            val age = binding.ageArea.text.toString()
            val waterAlarm = binding.wateralarmSwitch.isChecked

            if (name.isNotEmpty() && type.isNotEmpty() && age.isNotEmpty()) {
                registerPlant(name, type, age, waterAlarm)
            } else {
                Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.plantinfoCancelButton.setOnClickListener {
            finish()
        }
    }

    private fun registerPlant(name: String, type: String, age: String, waterAlarm: Boolean) {
        //val filename = "${System.currentTimeMillis()}.png"
        val plantRequest = PlantRequest(
            distbNm = type,
            name = name,
            age = age,
            isAlarm = waterAlarm,
            filename = null
        )

        lifecycleScope.launch {
            try {
                //val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImVtYWlsMkBnbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzIyNTIzMzc1LCJleHAiOjE3MjI1MjY5NzV9.9Cnmi4gutKoxNE8ZqccopE8sPXtJZ_wTbRfjv31ddic"
                val response = WateringRetrofitClient.plantApi.registerPlant(token, plantRequest)

                if (response.isSuccessful) {
                    Toast.makeText(this@MyPlantAdd, "식물이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@MyPlantAdd, "식물 등록에 실패했습니다: $errorBody", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MyPlantAdd, "오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun uploadImage(url: String?, filename: String) {
        if (url == null) {
            Log.e("MyPlantAdd", "Upload URL is null")
            Toast.makeText(this@MyPlantAdd, "이미지 업로드 URL이 없습니다", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MyPlantAdd", "Attempting to upload image to URL: $url")

        try {
            val inputStream = contentResolver.openInputStream(imageUri ?: return)
            val file = File(cacheDir, filename)
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", filename, requestBody)

            val response = WateringRetrofitClient.plantApi.uploadPlantImage(url, imagePart)

            if (response.isSuccessful) {
                Log.d("MyPlantAdd", "Image upload successful")
                Toast.makeText(this@MyPlantAdd, "식물과 이미지가 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT)
                    .show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("MyPlantAdd", "Image upload failed. Error: $errorBody")
                Toast.makeText(this@MyPlantAdd, "이미지 업로드에 실패했습니다: $errorBody", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            Log.e("MyPlantAdd", "Exception during image upload", e)
            Toast.makeText(this@MyPlantAdd, "이미지 업로드 오류: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("갤러리에서 사진 가져오기", "사진 촬영")
        AlertDialog.Builder(this)
            .setTitle("이미지 업로드")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> openCamera()
                }
            }
            .show()
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST)
    }

    private fun getImageUri(context: Context, image: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, image, "Title", null)
        return Uri.parse(path)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    imageUri = data?.data
                    binding.addPhotoButton2.setImageURI(imageUri)
                }

                TAKE_PICTURE_REQUEST -> {
                    val photo: Bitmap? = data?.extras?.get("data") as? Bitmap
                    photo?.let {
                        imageUri = getImageUri(this, it)
                        binding.addPhotoButton2.setImageBitmap(it)
                    }
                }
            }
        }
    }


}