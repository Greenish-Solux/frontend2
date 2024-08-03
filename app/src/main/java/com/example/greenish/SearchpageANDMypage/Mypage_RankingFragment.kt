package com.example.greenish.SearchpageANDMypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.greenish.R

class Mypage_RankingFragment : Fragment() {
    private val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImVtYWlsNkBnbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzIyNzE1Nzc2LCJleHAiOjE3MjI3MTkzNzZ9.xEVSUJY0JXSB1LZ1bz5Ul7jCftKa-3ZvB4H4lnBbdXY"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_ranking_20out, container, false)



        return view
    }}
