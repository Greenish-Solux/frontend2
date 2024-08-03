package com.example.greenish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.greenish.databinding.FragmentCalendarBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {
    private lateinit var todoDecorator: TodoDayDecorator
    private lateinit var wateringDecorator: WateringDayDecorator
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CalViewModel
    private val token =
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IjBAZ21haWwuY29tIiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTcyMjY5OTI4MywiZXhwIjoxNzIyNzAyODgzfQ.BIZB_wiEie8XjHc8PKXOx2FVor2pVoVkwSV_MUpLje8"
    private var wateringDates: Set<CalendarDay> = emptySet()
    private var todoDates: Set<CalendarDay> = emptySet()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[CalViewModel::class.java]

        setupCalendarView()
        setupViewPager()
        fetchUserWaterings()
        observeViewModel()
    }

    private fun setupCalendarView() {
        binding.calendarView.setOnDateChangedListener { _, date, selected ->
            if (selected) {
                viewModel.setSelectedDate(date)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.datesWithTodos.observe(viewLifecycleOwner) { dates ->
            todoDates = dates
            updateCalendarDecorators()
        }
    }

    private fun setupViewPager() {
        val viewPager2Adapter = CalTabAdapter(requireActivity())
        viewPager2Adapter.addFragment(CalWaterFragment())
        viewPager2Adapter.addFragment(CalTodoFragment())

        binding.viewPagerCalendarFragment.adapter = viewPager2Adapter

        TabLayoutMediator(
            binding.tabLayoutCalendar,
            binding.viewPagerCalendarFragment
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "물주기"
                1 -> tab.text = "할 일"
            }
        }.attach()
    }

    private fun fetchUserWaterings() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    WateringRetrofitClient.wateringApi.getUserWaterings(token)
                }
                if (response.success) {
                    wateringDates = response.data.map { watering ->
                        val date = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).parse(watering.scheduleDate)
                        CalendarDay.from(date)
                    }.toSet()
                    updateCalendarDecorators()
                }
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    private fun updateCalendarDecorators() {
        binding.calendarView.removeDecorators()

        if (todoDates.isNotEmpty()) {
            binding.calendarView.addDecorator(TodoDayDecorator(todoDates))
        }

        if (wateringDates.isNotEmpty()) {
            binding.calendarView.addDecorator(WateringDayDecorator(wateringDates))
        }

        binding.calendarView.invalidateDecorators()
    }


    private fun addWateringDecorator() {
        if (wateringDates.isNotEmpty()) {
            wateringDecorator = WateringDayDecorator(wateringDates)
            binding.calendarView.addDecorator(wateringDecorator)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}