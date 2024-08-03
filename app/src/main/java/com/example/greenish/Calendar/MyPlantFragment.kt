package com.example.greenish

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.greenish.databinding.FragmentMyPlantBinding
import kotlinx.coroutines.launch

class MyPlantFragment : Fragment() {

    private lateinit var binding: FragmentMyPlantBinding
    //private val plantList = mutableListOf<Plant>()
    private lateinit var adapter: PlantGridAdapter

    companion object {
        const val ADD_PLANT_REQUEST = 1
        const val PLANT_PROFILE_REQUEST = 2
        private const val token =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImdyZWVuQGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MjI3MDk5NDksImV4cCI6MTcyMjcxMzU0OX0.M4otbCF5e55aRCejeXxvx-J4KABkMf7_jvpG9cw1LCI"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPlantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlantGridAdapter(requireContext())
        binding.plantGridView.adapter = adapter

        binding.ivPlantaddAddbtn.setOnClickListener {
            val intent = Intent(requireContext(), MyPlantAdd::class.java)
            startActivityForResult(intent, ADD_PLANT_REQUEST)
        }

        binding.plantGridView.setOnItemClickListener { _, _, position, _ ->
            val plant = adapter.getItem(position) as PlantListItem
            val intent = Intent(requireContext(), MyPlantProfile::class.java).apply {
                putExtra("plantId", plant.plantId)
            }
            //startActivity(intent)
            startActivityForResult(intent, PLANT_PROFILE_REQUEST)
        }

        fetchPlantList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLANT_PROFILE_REQUEST && resultCode == Activity.RESULT_OK) {
            fetchPlantList() // GridView 업데이트
        }
    }

    //식물 목록 조회 기능
    private fun fetchPlantList() {
        lifecycleScope.launch {
            try {
                //val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImVtYWlsMkBnbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzIyNDg1ODk1LCJleHAiOjE3MjI0ODk0OTV9.riFI73Cq7cHt4y_qb7MHCbm7rycguet17fBVQl08iDY" // 실제 토큰으로 교체해야 합니다
                val response = WateringRetrofitClient.plantApi.getPlantList(token)

                if (response.isSuccessful) {
                    val plantListResponse = response.body()
                    plantListResponse?.data?.let { plantList ->
                        adapter.updatePlants(plantList)
                    }
                } else {
                    Toast.makeText(context, "식물 목록 조회에 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchPlantList()
    }


}
