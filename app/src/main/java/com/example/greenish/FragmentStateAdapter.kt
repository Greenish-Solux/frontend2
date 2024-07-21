package com.example.greenish

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MypageFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Mypage_SettingFragment()
            1 -> Mypage_EnvironmentFragment()
            2 -> Mypage_RankingFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}