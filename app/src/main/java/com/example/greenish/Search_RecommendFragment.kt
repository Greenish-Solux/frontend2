package com.example.greenish

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class Search_RecommendFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModel.Factory(SearchRepository(RetrofitClient.apiService))
    }


    private lateinit var searchView: SearchView
    private lateinit var searchGlassIcon: ImageView
    private var filterOptions = FilterOptions()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_recommend, container, false)

        searchView = view.findViewById(R.id.sv_search_recommend_searchview)
        searchGlassIcon = view.findViewById(R.id.iv_search_recommend_searchglass)

        val plantImageIdsWithCntntsNo = mapOf(
            R.id.iv_search_recommend_plant1 to "12938",
            R.id.iv_search_recommend_plant2 to "12998",
            R.id.iv_search_recommend_plant3 to "18576",
            R.id.iv_search_recommend_plant4 to "17741",
            R.id.iv_search_recommend_plant5 to "12901",
            R.id.iv_search_recommend_plant6 to "12987",
            R.id.iv_search_recommend_plant7 to "12996",
            R.id.iv_search_recommend_plant8 to "14675"
        )

        plantImageIdsWithCntntsNo.forEach { (imageId, cntntsNo) ->
            view.findViewById<ImageView>(imageId)?.setOnClickListener {
                navigateToResultPage(cntntsNo)
            }
        }


        view.findViewById<ImageView>(R.id.iv_search_recommend_filter)?.setOnClickListener {
            showFilterBottomSheet()
        }

        setupSearch()

        return view
    }

    val plantImageIdsWithCntntsNo = mapOf(
        R.id.iv_search_recommend_plant1 to "12938",
        R.id.iv_search_recommend_plant2 to "12998",
        R.id.iv_search_recommend_plant3 to "18576",
        R.id.iv_search_recommend_plant4 to "17741",
        R.id.iv_search_recommend_plant5 to "12901",
        R.id.iv_search_recommend_plant6 to "12987",
        R.id.iv_search_recommend_plant7 to "12996",
        R.id.iv_search_recommend_plant8 to "14675"
    )

    private fun navigateToResultPage(cntntsNo: String) {
        val resultFragment = Search_ResultPageFragment.newInstance(cntntsNo)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fl_search_main_recommendFragmentContainer, resultFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupSearch() {
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        performSearch(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        searchGlassIcon.setOnClickListener {
            val query = searchView.query.toString()
            if (query.isNotEmpty()) {
                performSearch(query)
            }
        }
    }

    private fun performSearch(query: String) {
        val apiService = RetrofitClient.apiService
        val call = apiService.searchPlants(query)

        call.enqueue(object : Callback<PlantResponse> {
            override fun onResponse(call: Call<PlantResponse>, response: Response<PlantResponse>) {
                if (response.isSuccessful) {
                    val plantResponse = response.body()
                    val searchResults = plantResponse?.plants
                    Log.d("API_RESPONSE", "Success: ${searchResults}")
                    navigateToSearchResult(query, searchResults)
                } else {
                    Log.e("API_RESPONSE", "Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PlantResponse>, t: Throwable) {
                Log.e("API_RESPONSE", "Failure: ${t.message}", t)
            }
        })
    }

    private fun navigateToResultPage() {
        val resultFragment = Search_ResultPageFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fl_search_main_recommendFragmentContainer, resultFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showFilterBottomSheet() {
        val bottomSheetView = layoutInflater.inflate(R.layout.search_filter, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)

        // Setup SeekBars
        setupSeekBars(bottomSheetView)

        // Setup other filter options (예: CheckBox, RadioButton 등)
        setupOtherFilterOptions(bottomSheetView)

        // Setup close button
        val closeButton = bottomSheetView.findViewById<ImageView>(R.id.ib_filter_closebutton)
        closeButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Setup apply button
        val applyButton = bottomSheetView.findViewById<Button>(R.id.wb_filter_apply)
        applyButton.setOnClickListener {
            applyFilters()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun setupOtherFilterOptions(view: View) {
        filterOptions = FilterOptions()

        // 1. managedemanddoCode 설정
        val managedemandSeekBar = view.findViewById<SeekBar>(R.id.sb_filter_managedemanddoCode)
        managedemandSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                filterOptions.managedemanddoCode = when (progress) {
                    0 -> "초보자"
                    50 -> "초중급자"
                    100 -> "고급자"
                    else -> ""
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 2. winterLwetTpCodeNm 설정
        val winterLwetSeekBar = view.findViewById<SeekBar>(R.id.sb_filter_winterLwetTpCodeNm)
        winterLwetSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                filterOptions.winterLwetTpCodeNm = when (progress) {
                    0 -> "0"
                    25 -> "5"
                    50 -> "7"
                    75 -> "10"
                    100 -> "13"
                    else -> ""
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 3. lighttdemanddoCodeNm 설정
        val lightDemandButtons = listOf(
            R.id.ib_filter_lighttdemanddoCodeNm_office to "사무실",
            R.id.ib_filter_lighttdemanddoCodeNm_veranda to "베란다",
            R.id.ib_filter_lighttdemanddoCodeNm_bathroom to "욕실",
            R.id.ib_filter_lighttdemanddoCodeNm_brightoffice to "밝은 사무실",
            R.id.ib_filter_lighttdemanddoCodeNm_terrace to "테라스",
            R.id.ib_filter_lighttdemanddoCodeNm_garden to "정원"
        )

        lightDemandButtons.forEach { (id, value) ->
            view.findViewById<Button>(id).setOnClickListener {
                filterOptions.lighttdemanddoCodeNm = value
            }
        }

        // 4. flclrCodeNm 설정
        val flowerColorButtons = listOf(
            R.id.ib_filter_flclrCodeNm_red to "빨강색",
            R.id.ib_filter_flclrCodeNm_orange to "오렌지색",
            R.id.ib_filter_flclrCodeNm_yellow to "노랑색",
            R.id.ib_filter_flclrCodeNm_pink to "분홍색",
            R.id.ib_filter_flclrCodeNm_purple to "보라색",
            R.id.ib_filter_flclrCodeNm_blue to "파랑색",
            R.id.ib_filter_flclrCodeNm_white to "흰색",
            R.id.ib_filter_flclrCodeNm_mix to "혼합색",
            R.id.ib_filter_flclrCodeNm_etc to "기타"
        )

        flowerColorButtons.forEach { (id, value) ->
            view.findViewById<ImageButton>(id).setOnClickListener {
                filterOptions.flclrCodeNm = value
            }
        }

        // 5. ignSeasonCodeNm 설정
        val ignSeasonButtons = listOf(
            R.id.ib_filter_ignSeasonCodeNm_spring to "봄",
            R.id.ib_filter_ignSeasonCodeNm_summer to "여름",
            R.id.ib_filter_ignSeasonCodeNm_fall to "가을",
            R.id.ib_filter_ignSeasonCodeNm_winter to "겨울"
        )

        ignSeasonButtons.forEach { (id, value) ->
            view.findViewById<Button>(id).setOnClickListener {
                filterOptions.ignSeasonCodeNm = value
            }
        }

        // 6. grwhstleCodeNm 설정
        val grwhstleButtons = listOf(
            R.id.ib_filter_grwhstleCodeNm_type1 to "직립형",
            R.id.ib_filter_grwhstleCodeNm_type2 to "관목형",
            R.id.ib_filter_grwhstleCodeNm_type3 to "덩굴성",
            R.id.ib_filter_grwhstleCodeNm_type4 to "로제트형",
            R.id.ib_filter_grwhstleCodeNm_type5 to "다육형",
            R.id.ib_filter_grwhstleCodeNm_type6 to "풀모양"
        )

        grwhstleButtons.forEach { (id, value) ->
            view.findViewById<ImageButton>(id).setOnClickListener {
                filterOptions.grwhstleCodeNm = value
            }
        }

        // 7. lefmrkCodeNm 설정
        val lefmrkeButtons = listOf(
            R.id.ib_filter_lefmrkCodeNm_stripe to "줄무늬",
            R.id.ib_filter_lefmrkCodeNm_dot to "점무늬",
            R.id.ib_filter_lefmrkCodeNm_etc to "기타",
            R.id.ib_filter_lefmrkCodeNm_reap to "잎 가장자리 무늬"
        )

        lefmrkeButtons.forEach { (id, value) ->
            view.findViewById<ImageButton>(id).setOnClickListener {
                filterOptions.lefmrkCodeNm = value
            }
        }

        // 8. fruit 설정
        val fruitButtons = listOf(
            R.id.ib_filter_fruit_true to "true",
            R.id.ib_filter_fruit_false to "false"
        )

        fruitButtons.forEach { (id, value) ->
            view.findViewById<Button>(id).setOnClickListener {
                filterOptions.fruit = value
            }
        }
    }

    private fun applyFilters() {
        val filters = mutableMapOf<String, String>()

        if (filterOptions.managedemanddoCode.isNotEmpty()) {
            filters["managedemanddoCode"] = filterOptions.managedemanddoCode
        }
        if (filterOptions.winterLwetTpCodeNm.isNotEmpty()) {
            filters["winterLwetTpCodeNm"] = filterOptions.winterLwetTpCodeNm
        }
        if (filterOptions.lighttdemanddoCodeNm.isNotEmpty()) {
            filters["lighttdemanddoCodeNm"] = filterOptions.lighttdemanddoCodeNm
        }
        if (filterOptions.flclrCodeNm.isNotEmpty()) {
            filters["flclrCodeNm"] = filterOptions.flclrCodeNm
        }
        if (filterOptions.ignSeasonCodeNm.isNotEmpty()) {
            filters["ignSeasonCodeNm"] = filterOptions.ignSeasonCodeNm
        }
        if (filterOptions.grwhstleCodeNm.isNotEmpty()) {
            filters["grwhstleCodeNm"] = filterOptions.grwhstleCodeNm
        }
        if (filterOptions.lefmrkCodeNm.isNotEmpty()) {
            filters["lefmrkCodeNm"] = filterOptions.lefmrkCodeNm
        }
        filters["fruit"] = filterOptions.fruit.toString()

        performFilteredSearch(filters)
    }

    private fun performFilteredSearch(filters: Map<String, String>) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val results =
                    SearchRepository(RetrofitClient.apiService).searchPlantsWithFilter(filters)
                results?.let {
                    showFilteredResults(it)
                }
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    private fun performFilteredSearch() {
        val filters = mutableMapOf<String, String>()

        if (filterOptions.managedemanddoCode.isNotEmpty()) {
            filters["managedemanddoCode"] = filterOptions.managedemanddoCode
        }
        if (filterOptions.winterLwetTpCodeNm.isNotEmpty()) {
            filters["winterLwetTpCodeNm"] = filterOptions.winterLwetTpCodeNm
        }
        if (filterOptions.lighttdemanddoCodeNm.isNotEmpty()) {
            filters["lighttdemanddoCodeNm"] = filterOptions.lighttdemanddoCodeNm
        }
        if (filterOptions.flclrCodeNm.isNotEmpty()) {
            filters["flclrCodeNm"] = filterOptions.flclrCodeNm
        }
        if (filterOptions.ignSeasonCodeNm.isNotEmpty()) {
            filters["ignSeasonCodeNm"] = filterOptions.ignSeasonCodeNm
        }
        if (filterOptions.fruit.isNotEmpty()) {
            filters["fruit"] = filterOptions.fruit
        }
        if (filterOptions.lefmrkCodeNm.isNotEmpty()) {
            filters["lefmrkCodeNm"] = filterOptions.lefmrkCodeNm
        }
        if (filterOptions.grwhstleCodeNm.isNotEmpty()) {
            filters["grwhstleCodeNm"] = filterOptions.grwhstleCodeNm
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val results =
                    SearchRepository(RetrofitClient.apiService).searchPlantsWithFilter(filters)
                results?.let {
                    // 여기서 바로 결과를 표시하도록 수정
                    showFilteredResults(it)
                }
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    private fun showFilteredResults(results: List<SearchResult>) {
        val searchResultFragment = Search_ResultFragment.newInstance("").apply {
            arguments = Bundle().apply {
                putParcelableArrayList("searchResults", ArrayList(results))
                putString("searchType", "filter")  // 검색 타입 추가
                putParcelable("filterOptions", filterOptions)  // 필터 옵션 추가
            }
        }
        (parentFragment as? SearchFragment)?.showFragment(searchResultFragment)
    }


    private fun setupSeekBars(view: View) {
        val seekBar1 = view.findViewById<SeekBar>(R.id.sb_filter_managedemanddoCode)
        val seekBar2 = view.findViewById<SeekBar>(R.id.sb_filter_winterLwetTpCodeNm)

        val textView1_1 = view.findViewById<TextView>(R.id.tv_filter_managedemanddoCode_1)
        val textView1_2 = view.findViewById<TextView>(R.id.tv_filter_managedemanddoCode_2)
        val textView1_3 = view.findViewById<TextView>(R.id.tv_filter_managedemanddoCode_3)

        val textView2_1 = view.findViewById<TextView>(R.id.tv_filter_winterLwetTpCodeNm_1)
        val textView2_2 = view.findViewById<TextView>(R.id.tv_filter_winterLwetTpCodeNm_2)
        val textView2_3 = view.findViewById<TextView>(R.id.tv_filter_winterLwetTpCodeNm_3)
        val textView2_4 = view.findViewById<TextView>(R.id.tv_filter_winterLwetTpCodeNm_4)
        val textView2_5 = view.findViewById<TextView>(R.id.tv_filter_winterLwetTpCodeNm_5)

        // SeekBar 1 setup
        seekBar1.max = 2
        seekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (progress) {
                    0 -> updateTextView(textView1_1, textView1_2, textView1_3)
                    1 -> updateTextView(textView1_2, textView1_1, textView1_3)
                    2 -> updateTextView(textView1_3, textView1_1, textView1_2)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // SeekBar 2 setup
        seekBar2.max = 4
        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (progress) {
                    0 -> updateTextView(
                        textView2_1,
                        textView2_2,
                        textView2_3,
                        textView2_4,
                        textView2_5
                    )

                    1 -> updateTextView(
                        textView2_2,
                        textView2_1,
                        textView2_3,
                        textView2_4,
                        textView2_5
                    )

                    2 -> updateTextView(
                        textView2_3,
                        textView2_1,
                        textView2_2,
                        textView2_4,
                        textView2_5
                    )

                    3 -> updateTextView(
                        textView2_4,
                        textView2_1,
                        textView2_2,
                        textView2_3,
                        textView2_5
                    )

                    4 -> updateTextView(
                        textView2_5,
                        textView2_1,
                        textView2_2,
                        textView2_3,
                        textView2_4
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun updateTextView(mainTextView: TextView, vararg otherTextViews: TextView) {
        mainTextView.setTextColor(resources.getColor(R.color.Greenish_green, null))
        mainTextView.textSize = 14f

        otherTextViews.forEach {
            it.setTextColor(resources.getColor(R.color.Greenish_darkgrey, null))
            it.textSize = 12f
        }
    }

    private fun navigateToSearchResult(query: String, results: List<SearchResult>?) {
        val searchResultFragment = Search_ResultFragment().apply {
            arguments = Bundle().apply {
                putString("searchQuery", query)
                putParcelableArrayList("searchResults", ArrayList(results ?: emptyList()))
                putString("searchType", "plantsearch")  // 검색 타입 추가
            }
        }
        (parentFragment as? SearchFragment)?.showFragment(searchResultFragment)
    }
}