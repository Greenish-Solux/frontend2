package com.example.greenish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class Search_ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 결과 박스 이미지뷰들에 클릭 리스너 설정
        val resultBoxIds = listOf(
            R.id.iv_search_result_box1,
            R.id.iv_search_result_box2,
            R.id.iv_search_result_box3
        )

        resultBoxIds.forEach { boxId ->
            view.findViewById<ImageView>(boxId).setOnClickListener {
                navigateToResultPage()
            }
        }
    }

    private fun navigateToResultPage() {
        // Search_ResultPageFragment로 전환
        val resultPageFragment = Search_ResultPageFragment()
        (parentFragment as? SearchFragment)?.showFragment(resultPageFragment)
    }
}