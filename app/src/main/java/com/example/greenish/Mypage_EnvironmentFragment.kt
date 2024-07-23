package com.example.greenish

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class Mypage_EnvironmentFragment : Fragment() {

    private lateinit var tempTextViews: List<TextView>
    private lateinit var humidTextViews: List<TextView>
    private lateinit var ivTemp: ImageView
    private lateinit var ivHumid: ImageView
    private lateinit var ivPlace: ImageView
    private lateinit var tvPlace: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var apiService: ApiService

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

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://your-api-base-url.com/")  // TODO: 실제 API 기본 URL로 변경해주세요
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        return view
    }

    private fun sendLocationToBackend(latitude: Double, longitude: Double, address: String) {
        val locationData = LocationData(latitude, longitude, address)

        // 코루틴 스코프 내에서 API 호출
        lifecycleScope.launch {
            try {
                val response = apiService.sendLocation(locationData)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "위치 정보가 성공적으로 전송되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "위치 정보 전송에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
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
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let { loc ->
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                    addresses?.let { addressList ->
                        if (addressList.isNotEmpty()) {
                            val address = addressList[0]
                            val addressText = address.getAddressLine(0) ?: "주소를 찾을 수 없습니다."
                            tvPlace.text = addressText

                            // 백엔드로 데이터 전송
                            sendLocationToBackend(loc.latitude, loc.longitude, addressText)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "위치를 가져올 수 없습니다: ${e.message}", Toast.LENGTH_SHORT).show()
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

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String
)

interface ApiService {
    @POST("location")  // 실제 엔드포인트로 변경해주세요
    suspend fun sendLocation(@Body locationData: LocationData): retrofit2.Response<Unit>
}