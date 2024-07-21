package com.example.greenish

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.greenish.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setBottomNavigationView()

        // 앱 초기 실행 시 홈화면으로 설정 -> calendar에서 home으로 변경 필요
        if (savedInstanceState == null) {
            binding.navView.selectedItemId = R.id.navigation_home
        }
    }

    fun setBottomNavigationView() {
        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
//                R.id.navigation_home -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.main_container, HomeFragment()).commit()
//                    true
//                }
//                R.id.navigation_calendar -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.main_container, CalendarFragment()).commit()
//                    true
//                }
//                R.id.navigation_diary -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.main_container, MyPlantFragment()).commit()
//                    true
//                }
//                R.id.navigation_search -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.main_container, SearchFragment()).commit()
//                    true
//                }
//                R.id.navigation_extra -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.main_container, SettingsFragment()).commit()
//                    true
//                }
                else -> false
            }
        }
    }
}