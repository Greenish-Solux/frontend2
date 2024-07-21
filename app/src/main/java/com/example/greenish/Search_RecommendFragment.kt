package com.example.greenish

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog

class Search_RecommendFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var searchGlassIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_recommend, container, false)

        // SearchView와 검색 아이콘 초기화
        searchView = view.findViewById(R.id.sv_search_recommend_searchview)
        searchGlassIcon = view.findViewById(R.id.iv_search_recommend_searchglass)

        // 식물 이미지 클릭 이벤트 설정
        val plantImageIds = listOf(
            R.id.iv_search_recommend_plant1,
            R.id.iv_search_recommend_plant2,
            R.id.iv_search_recommend_plant3,
            R.id.iv_search_recommend_plant4
        )

        plantImageIds.forEach { imageId ->
            view.findViewById<ImageView>(imageId)?.setOnClickListener {
                navigateToResultPage()
            }
        }

        // 필터 이미지 클릭 이벤트 설정
        view.findViewById<ImageView>(R.id.iv_search_recommend_filter)?.setOnClickListener {
            showFilterBottomSheet()
        }

        // SearchView 및 검색 아이콘 설정
        setupSearch()

        return view
    }

    private fun setupSearch() {
        // SearchManager 설정
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))

        // 최근 검색어 제안 활성화
        searchView.isSubmitButtonEnabled = true
        searchView.isSaveEnabled = true

        // SearchView 리스너 설정
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        // 검색어 저장
                        saveRecentQuery(it)
                        performSearch()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어 변경 시 처리 (필요한 경우)
                return true
            }
        })

        // 검색 아이콘 클릭 이벤트 설정
        searchGlassIcon.setOnClickListener {
            performSearch()
        }
    }

    private fun saveRecentQuery(query: String) {
        val suggestions = SearchRecentSuggestions(
            requireContext(),
            MySuggestionProvider.AUTHORITY,
            MySuggestionProvider.MODE
        )
        suggestions.saveRecentQuery(query, null)
    }

    private fun performSearch() {
        val query = searchView.query.toString()
        if (query.isNotEmpty()) {
            // 검색어 저장
            saveRecentQuery(query)
            // 검색 결과 화면으로 이동
            navigateToSearchResult()
        }
    }

    private fun navigateToResultPage() {
        // ResultPageFragment로 전환
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

        bottomSheetDialog.show()
    }

    private fun navigateToSearchResult() {
        val searchResultFragment = Search_ResultFragment()
        (parentFragment as? SearchFragment)?.showFragment(searchResultFragment)
    }
}
