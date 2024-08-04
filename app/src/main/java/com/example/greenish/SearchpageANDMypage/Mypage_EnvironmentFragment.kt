package com.example.greenish.SearchpageANDMypage

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale
import androidx.lifecycle.lifecycleScope
import com.example.greenish.R
import kotlinx.coroutines.launch


class Mypage_EnvironmentFragment : Fragment() {

    private lateinit var tempTextViews: List<TextView>
    private lateinit var humidTextViews: List<TextView>
    private lateinit var ivTemp: ImageView
    private lateinit var ivHumid: ImageView
    private lateinit var ivPlace: ImageView
    private lateinit var tvPlace: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImdyZWVuQGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MjI3ODY0MjYsImV4cCI6MTcyMjc5MDAyNn0.C_zE7N0vYIvUEyPxkEajQy1O9GNG3PKysWfknHzeBWc"

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_environment, container, false)

        // Initialize views
        ivTemp = view.findViewById(R.id.iv_environment_temp)
        ivHumid = view.findViewById(R.id.iv_environment_humid)
        ivPlace = view.findViewById(R.id.iv_environment_place)
        tvPlace = view.findViewById(R.id.tv_environment_place)

        tempTextViews = listOf(
            view.findViewById(R.id.tv_environment_temp1),
            view.findViewById(R.id.tv_environment_temp2),
            view.findViewById(R.id.tv_environment_temp3)
        )

        humidTextViews = listOf(
            view.findViewById(R.id.tv_environment_humid1),
            view.findViewById(R.id.tv_environment_humid2),
            view.findViewById(R.id.tv_environment_humid3)
        )

        // Set click listeners
        tempTextViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                handleTempTextViewClick(index)
            }
        }

        humidTextViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                handleHumidTextViewClick(index)
            }
        }

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Set click listener for place ImageView
        ivPlace.setOnClickListener {
            if (checkLocationPermission()) {
                getLocation()
            } else {
                requestLocationPermission()
            }
        }

        return view
    }


    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        Log.d("LocationDebug", "getLocation() 메서드 시작")

        if (!checkLocationPermission()) {
            Log.e("LocationDebug", "위치 권한이 없습니다. 권한을 요청해야 합니다.")
            requestLocationPermission()
            return
        }

        Log.d("LocationDebug", "위치 정보 요청 시작")
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    Log.d("LocationDebug", "위치 정보 가져오기 성공: lat=${location.latitude}, lon=${location.longitude}")

                    try {
                        Log.d("LocationDebug", "Geocoder를 사용하여 주소 변환 시작")
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        if (addresses != null && addresses.isNotEmpty()) {
                            val address = addresses[0]
                            val addressText = address.getAddressLine(0) ?: "주소를 찾을 수 없습니다."
                            Log.d("LocationDebug", "주소 변환 성공: $addressText")

                            tvPlace.text = addressText
                            Log.d("LocationDebug", "TextView에 주소 설정 완료")

                            // 백엔드로 데이터 전송
                            Log.d("LocationDebug", "백엔드로 위치 정보 전송 시작")
                            sendLocationToBackend(location.latitude, location.longitude)
                        } else {
                            Log.w("LocationDebug", "주소를 찾을 수 없습니다.")
                            tvPlace.text = "주소를 찾을 수 없습니다."
                        }
                    } catch (e: Exception) {
                        Log.e("LocationDebug", "Geocoding 과정에서 오류 발생", e)
                        tvPlace.text = "주소 변환 중 오류가 발생했습니다."
                    }
                } else {
                    Log.w("LocationDebug", "위치 정보가 null입니다.")
                    tvPlace.text = "위치 정보를 가져올 수 없습니다."
                }
            }
            .addOnFailureListener { e ->
                Log.e("LocationDebug", "위치 정보 가져오기 실패", e)
                Toast.makeText(requireContext(), "위치를 가져올 수 없습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sendLocationToBackend(latitude: Double, longitude: Double) {
        Log.d("LocationDebug", "sendLocationToBackend 호출됨: lat=$latitude, lon=$longitude")
        val locationData = LocationData(latitude, longitude)

        lifecycleScope.launch {
            try {
                Log.d("LocationDebug", "백엔드 API 호출 시작")
                val response = RetrofitClient.apiService.sendLocation(token, locationData)
                if (response.isSuccessful) {
                    Log.d("LocationDebug", "위치 정보 전송 성공")
                    Toast.makeText(requireContext(), "위치 정보가 성공적으로 전송되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("LocationDebug", "위치 정보 전송 실패: ${response.code()}")
                    Toast.makeText(requireContext(), "위치 정보 전송에 실패했습니다. 코드: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LocationDebug", "백엔드 통신 중 오류 발생", e)
                Toast.makeText(requireContext(), "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    Toast.makeText(requireContext(), "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun handleTempTextViewClick(index: Int) {
        // Change image source
        val tempImageRes = when (index) {
            0 -> R.drawable.ic_environment_selected1
            1 -> R.drawable.ic_environment_selected2
            else -> R.drawable.ic_environment_selected3
        }
        ivTemp.setImageResource(tempImageRes)

        // Update text colors
        tempTextViews.forEachIndexed { i, textView ->
            textView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (i == index) R.color.Greenish_white else R.color.Greenish_green
                )
            )
        }
    }

    private fun handleHumidTextViewClick(index: Int) {
        // Change image source
        val humidImageRes = when (index) {
            0 -> R.drawable.ic_environment_selected1
            1 -> R.drawable.ic_environment_selected2
            else -> R.drawable.ic_environment_selected3
        }
        ivHumid.setImageResource(humidImageRes)

        // Update text colors
        humidTextViews.forEachIndexed { i, textView ->
            textView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (i == index) R.color.Greenish_white else R.color.Greenish_green
                )
            )
        }
    }
}

