package com.example.greenish
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Search_RecommendFragment를 초기 화면으로 설정
        showFragment(Search_RecommendFragment())

        // 뒤로 가기 화살표에 클릭 리스너 설정
        view.findViewById<ImageView>(R.id.iv_search_main_arrow)?.setOnClickListener {
            navigateToPreviousPage()
        }
    }

    fun showFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fl_search_main_recommendFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToPreviousPage() {
        if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager.popBackStack()
        }
        // 초기 화면인 경우 아무 동작도 하지 않음
    }
}
