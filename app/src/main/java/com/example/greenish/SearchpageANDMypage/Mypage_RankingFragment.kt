package com.example.greenish.SearchpageANDMypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.greenish.R

class Mypage_RankingFragment : Fragment() {
    private val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImdyZWVuQGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MjI3MDk5NDksImV4cCI6MTcyMjcxMzU0OX0.M4otbCF5e55aRCejeXxvx-J4KABkMf7_jvpG9cw1LCI"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_ranking_20out, container, false)



        return view
    }}
