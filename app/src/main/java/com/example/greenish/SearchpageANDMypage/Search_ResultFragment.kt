package com.example.greenish.SearchpageANDMypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenish.databinding.FragmentSearchResultBinding

class Search_ResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchResultAdapter

    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModel.Factory(SearchRepository(RetrofitClient.apiService))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeSearchResults()

        arguments?.let { args ->
            val query = args.getString("searchQuery") ?: ""
            val results = args.getParcelableArrayList<SearchResult>("searchResults")
            val searchType = args.getString("searchType") ?: ""

            when (searchType) {
                "plantsearch" -> {
                    binding.tvSearchResultText1.text = query
                    if (results != null && results.isNotEmpty()) {
                        adapter.submitList(results)
                    } else if (query.isNotEmpty()) {
                        performSearch(query)
                    }
                }
                "filter" -> {
                    val filterOptions = args.getParcelable<FilterOptions>("filterOptions")
                    binding.tvSearchResultText1.text = "설정하신 필터 내용"
                    if (results != null && results.isNotEmpty()) {
                        adapter.submitList(results)
                    }
                }
            }
        }
    }


    private fun setupRecyclerView() {
        adapter = SearchResultAdapter { result ->
            searchViewModel.setSelectedCntntsNo(result.cntntsNo)
            navigateToResultPage(result.cntntsNo)
        }
        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSearchResults.adapter = adapter
    }

    private fun navigateToResultPage(cntntsNo: String) {
        val resultPageFragment = Search_ResultPageFragment.newInstance(cntntsNo)
        (parentFragment as? SearchFragment)?.showFragment(resultPageFragment)
    }

    private fun observeSearchResults() {
        searchViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            adapter.submitList(results)
        }
    }

    private fun performSearch(query: String) {
        searchViewModel.searchPlants(query)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(searchQuery: String): Search_ResultFragment {
            val fragment = Search_ResultFragment()
            val args = Bundle().apply {
                putString("searchQuery", searchQuery)
            }
            fragment.arguments = args
            return fragment
        }
    }
}