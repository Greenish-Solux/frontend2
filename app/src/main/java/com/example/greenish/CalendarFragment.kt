package com.example.greenish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.greenish.databinding.FragmentCalendarBinding
import com.google.android.material.tabs.TabLayoutMediator

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager2Adapter = CalTabAdapter(requireActivity())
        viewPager2Adapter.addFragment(CalWaterFragment())
        viewPager2Adapter.addFragment(CalTodoFragment())

        binding.viewPagerCalendarFragment.adapter = viewPager2Adapter

        TabLayoutMediator(binding.tabLayoutCalendar, binding.viewPagerCalendarFragment) { tab, position ->
            when (position) {
                0 -> tab.text = "물주기"
                1 -> tab.text = "할 일"
            }
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}