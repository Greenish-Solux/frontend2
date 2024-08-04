package com.example.greenish.SearchpageANDMypage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greenish.R
import kotlinx.coroutines.launch
import retrofit2.HttpException

class Mypage_RankingFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RankingAdapter
    private lateinit var myRankingContainer: LinearLayout
    private val token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImdyZWVuQGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MjI3ODY0MjYsImV4cCI6MTcyMjc5MDAyNn0.C_zE7N0vYIvUEyPxkEajQy1O9GNG3PKysWfknHzeBWc"



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_ranking, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewRankingResults)
        recyclerView.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean = false
        }
        recyclerView.isNestedScrollingEnabled = false
        adapter = RankingAdapter()
        recyclerView.adapter = adapter

        myRankingContainer = view.findViewById(R.id.myRankingContainer)

        fetchRankingData()

        return view
    }

    private fun fetchRankingData() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getRanking(token)
                if (response.isSuccessful) {
                    val rankingData = response.body()
                    rankingData?.let {
                        updateUI(it)
                    } ?: run {
                        Toast.makeText(context, "랭킹 데이터가 비어있습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "랭킹 데이터를 가져오는데 실패했습니다. 에러 코드: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Toast.makeText(context, "네트워크 오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "알 수 없는 오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(rankingData: ApiService.RankingResponse) {
        updateTopThree(rankingData.TopUsers.take(3))
        adapter.submitList(rankingData.TopUsers)

        val myRank = rankingData.MyRank
        if (myRank.rank > 20) {
            myRankingContainer.visibility = View.VISIBLE
            myRankingContainer.findViewById<TextView>(R.id.myRankingNumber).text = myRank.rank.toString()
            myRankingContainer.findViewById<TextView>(R.id.user).text = myRank.nickname
            myRankingContainer.findViewById<TextView>(R.id.num).text = myRank.recordCount.toString()
        } else {
            myRankingContainer.visibility = View.GONE
            adapter.setMyRank(myRank.rank)
        }
    }

    private fun updateTopThree(topThree: List<ApiService.RankingUser>) {
        view?.let { view ->
            topThree.forEachIndexed { index, user ->
                val ivMain = view.findViewById<ImageView>(resources.getIdentifier("iv_ranking_main${index + 1}", "id", requireContext().packageName))
                val tvMain = view.findViewById<TextView>(resources.getIdentifier("tv_ranking_main${index + 1}", "id", requireContext().packageName))

                Glide.with(requireContext())
                    .load(user.profileImageUrl)
                    .error(R.drawable.ic_mypage_profile)
                    .placeholder(R.drawable.ic_mypage_profile)
                    .into(ivMain)
                tvMain.text = user.nickname
            }
        }
    }
}

class RankingAdapter : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {
    private var items: List<ApiService.RankingUser> = emptyList()
    private var myRank: Int = -1

    fun submitList(newItems: List<ApiService.RankingUser>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun setMyRank(rank: Int) {
        myRank = rank
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking_result, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvRanking: TextView = itemView.findViewById(R.id.tv_ranking)
        private val ivRanking: ImageView = itemView.findViewById(R.id.iv_ranking)
        private val ivRankingCircle: ImageView = itemView.findViewById(R.id.iv_ranking_circle)
        private val tvRankingName: TextView = itemView.findViewById(R.id.tv_ranking_name)
        private val tvRankingNum2: TextView = itemView.findViewById(R.id.tv_ranking_num2)

        fun bind(user: ApiService.RankingUser) {
            when (user.rank) {
                1 -> {
                    ivRanking.setImageResource(R.drawable.ic_ranking_first)
                    tvRanking.text = ""
                }
                2 -> {
                    ivRanking.setImageResource(R.drawable.ic_ranking_second)
                    tvRanking.text = ""
                }
                3 -> {
                    ivRanking.setImageResource(R.drawable.ic_ranking_third)
                    tvRanking.text = ""
                }
                else -> {
                    ivRanking.setImageResource(R.drawable.ic_ranking_num_circle)
                    tvRanking.text = user.rank.toString()
                }
            }

            Glide.with(itemView.context)
                .load(user.profileImageUrl)
                .error(R.drawable.ic_mypage_profile)
                .placeholder(R.drawable.ic_mypage_profile)
                .into(ivRankingCircle)
            tvRankingName.text = user.nickname
            tvRankingNum2.text = user.recordCount.toString()

            if (user.rank == myRank) {
                itemView.setBackgroundColor(0xFFAFEEC1.toInt())
            } else {
                itemView.setBackgroundColor(0xFFFFFFFF.toInt())
            }
        }
    }
}