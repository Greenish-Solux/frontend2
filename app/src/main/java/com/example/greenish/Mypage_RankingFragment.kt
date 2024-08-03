package com.example.greenish
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class Mypage_RankingFragment : Fragment() {
    private val token =
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IjBAZ21haWwuY29tIiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTcyMjY5OTI4MywiZXhwIjoxNzIyNzAyODgzfQ.BIZB_wiEie8XjHc8PKXOx2FVor2pVoVkwSV_MUpLje8"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_ranking_20out, container, false)



        return view
    }}

