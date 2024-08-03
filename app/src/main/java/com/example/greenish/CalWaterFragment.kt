package com.example.greenish

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenish.databinding.FragmentCalWaterBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class CalWaterFragment : Fragment() {
    private var _binding: FragmentCalWaterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CalViewModel
    private lateinit var wateringAdapter: WateringAdapter
    private val token =
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IjBAZ21haWwuY29tIiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTcyMjY5OTI4MywiZXhwIjoxNzIyNzAyODgzfQ.BIZB_wiEie8XjHc8PKXOx2FVor2pVoVkwSV_MUpLje8"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalWaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(CalViewModel::class.java)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        wateringAdapter = WateringAdapter { wateringId ->
            completeWatering(wateringId)
        }
        binding.waterRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = wateringAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.selectedDateFormatted.observe(viewLifecycleOwner) { formattedDate ->
            binding.tvWaterDateinfo.text = formattedDate
            updateWateringInfo(viewModel.getSelectedDate())
        }
    }

    private fun updateWateringInfo(date: CalendarDay) {
        val apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val apiFormattedDate = apiDateFormat.format(date.date)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d("CalWaterFragment", "Requesting data for date: $apiFormattedDate")
                val response = withContext(Dispatchers.IO) {
                    WateringRetrofitClient.wateringApi.getWateringSchedule(token, apiFormattedDate)
                }
                if (response.success) {
                    Log.d("CalWaterFragment", "Received data: ${response.data}")
                    wateringAdapter.submitList(response.data)
                } else {
                    //Log.d("CalWaterFragment", "Failed to receive data: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("CalWaterFragment", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun completeWatering(wateringId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    WateringRetrofitClient.wateringApi.completeWatering(token, wateringId)
                }
                if (response.success) {
                    Log.d("CalWaterFragment", "Watering completed: ${response.data.id}")
                    // 완료 후 리스트를 새로고침합니다.
                    updateWateringInfo(viewModel.getSelectedDate())
                } else {
                    Log.d("CalWaterFragment", "Failed to complete watering")
                }
            } catch (e: Exception) {
                Log.e("CalWaterFragment", "Error completing watering: ${e.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}