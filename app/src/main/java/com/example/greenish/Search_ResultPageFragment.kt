package com.example.greenish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.greenish.databinding.FragmentSearchResultPageBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Search_ResultPageFragment : Fragment() {

    private var _binding: FragmentSearchResultPageBinding? = null
    private val binding get() = _binding!!

    // Initialize ApiService and repository here
    private val apiService: ApiService by lazy {
        RetrofitClient.apiService
    }


    private val repository: SearchRepository by lazy {
        SearchRepository(apiService)
    }

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.Factory(repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchResultPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cntntsNo = arguments?.getString(ARG_CNTNTS_NO) ?: return

        viewModel.getPlantDetails(cntntsNo)
        viewModel.plantDetails.observe(viewLifecycleOwner) { plantDetails ->
            plantDetails?.let { updateUI(it) }
        }
    }

    private fun updateUI(plantDetails: PlantDetails) {
        binding.apply {
            tvSearchResultPageTitle.text = plantDetails.distbNm
            Glide.with(this@Search_ResultPageFragment)
                .load(plantDetails.rtnFileUrl)
                .into(ivSearchResultPageImage)

        binding.tvSearchResultPageAdviseInfo.text = plantDetails.adviseInfo
        binding.tvSearchResultPageClCodeNm.text = plantDetails.clCodeNm
        binding.tvSearchResultPageDlthtsCodeNm.text = plantDetails.dlthtsCodeNm
        binding.tvSearchResultPageFmlCodeNm.text = plantDetails.fmlCodeNm
        binding.tvSearchResultPageFmldecolrCodeNm.text = plantDetails.fmldecolrCodeNm
        binding.tvSearchResultPageFmldeSeasonCodeNm.text = plantDetails.fmldeSeasonCodeNm
        binding.tvSearchResultPageFncltyInfo.text = plantDetails.fncltyInfo
        binding.tvSearchResultPageFrtlzrInfo.text = plantDetails.frtlzrInfo
        binding.tvSearchResultPageGrowthAraInfo.text = plantDetails.growthAraInfo
        binding.tvSearchResultPageGrowthHgInfo.text = plantDetails.growthHgInfo
        binding.tvSearchResultPageGrwhstleCodeNm.text = plantDetails.grwhstleCodeNm
        binding.tvSearchResultPageGrwhTpCodeNm.text = plantDetails.grwhTpCodeNm
        binding.tvSearchResultPageGrwtveCodeNm.text = plantDetails.grwtveCodeNm
        binding.tvSearchResultPageHdCodeNm.text = plantDetails.hdCodeNm
        binding.tvSearchResultPageIgnSeasonCodeNm.text = plantDetails.ignSeasonCodeNm
        binding.tvSearchResultPageLefcolrCodeNm.text = plantDetails.lefcolrCodeNm
        binding.tvSearchResultPageLefStleInfo.text = plantDetails.lefStleInfo
        binding.tvSearchResultPageLefmrkCodeNm.text = plantDetails.lefmrkCodeNm
        binding.tvSearchResultPageLighttdemanddoCodeNm.text = plantDetails.lighttdemanddoCodeNm
        binding.tvSearchResultPageManagedemanddoCodeNm.text = plantDetails.managedemanddoCodeNm
        binding.tvSearchResultPageManagelevelCodeNm.text = plantDetails.managelevelCodeNm
        binding.tvSearchResultPageOrgplceInfo.text = plantDetails.orgplceInfo
        binding.tvSearchResultPagePlntbneNm.text = plantDetails.plntbneNm
        binding.tvSearchResultPagePlntzrNm.text = plantDetails.plntzrNm
    }}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CNTNTS_NO = "cntntsNo"

        fun newInstance(cntntsNo: String): Search_ResultPageFragment {
            return Search_ResultPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CNTNTS_NO, cntntsNo)
                }
            }
        }
    }
}
