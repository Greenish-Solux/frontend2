package com.example.greenish.SearchpageANDMypage

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> = _searchResults

    private val _selectedCntntsNo = MutableLiveData<String>()
    val selectedCntntsNo: LiveData<String> = _selectedCntntsNo

    private val _plantDetails = MutableLiveData<PlantDetails?>()
    val plantDetails: LiveData<PlantDetails?> = _plantDetails

    fun searchPlants(query: String) {
        viewModelScope.launch {
            val results = repository.searchItems(query)
            _searchResults.value = results ?: emptyList()
        }
    }

    fun setSelectedCntntsNo(cntntsNo: String) {
        _selectedCntntsNo.value = cntntsNo
    }

    fun setFilteredResults(results: List<SearchResult>) {
        _searchResults.value = results
    }

    fun getPlantDetails(cntntsNo: String) = viewModelScope.launch {
        try {
            val result = repository.getPlantDetails(cntntsNo)
            _plantDetails.value = result
        } catch (e: Exception) {
            // Handle exception
            _plantDetails.value = null
        }
    }

    fun setSearchResults(results: List<SearchResult>) {
        _searchResults.value = results
    }

    // Factory는 그대로 유지
    class Factory(private val repository: SearchRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}