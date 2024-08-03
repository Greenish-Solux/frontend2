package com.example.greenish.SearchpageANDMypage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

class SearchRepository(private val apiService: ApiService) {
    suspend fun searchItems(query: String): List<SearchResult>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchPlants(query).execute()
                if (response.isSuccessful) {
                    response.body()?.plants
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getPlantDetails(cntntsNo: String): PlantDetails? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPlantDetails(cntntsNo)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getData(id: String): Call<SearchResult>? {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getPlantData(id)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun searchPlantsWithFilter(filters: Map<String, String>): List<SearchResult>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchPlantsWithFilter(filters).execute()
                if (response.isSuccessful) {
                    response.body()?.plants
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}