package com.example.greenish
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class Mypage_EnvironmentFragment : Fragment() {

    private lateinit var tempTextViews: List<TextView>
    private lateinit var humidTextViews: List<TextView>
    private lateinit var ivTemp: ImageView
    private lateinit var ivHumid: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mypage_environment, container, false)

        // Initialize views
        ivTemp = view.findViewById(R.id.iv_environment_temp)
        ivHumid = view.findViewById(R.id.iv_environment_humid)

        tempTextViews = listOf(
            view.findViewById(R.id.tv_environment_temp1),
            view.findViewById(R.id.tv_environment_temp2),
            view.findViewById(R.id.tv_environment_temp3)
        )

        humidTextViews = listOf(
            view.findViewById(R.id.tv_environment_humid1),
            view.findViewById(R.id.tv_environment_humid2),
            view.findViewById(R.id.tv_environment_humid3)
        )

        // Set click listeners
        tempTextViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                handleTempTextViewClick(index)
            }
        }

        humidTextViews.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                handleHumidTextViewClick(index)
            }
        }

        return view
    }

    private fun handleTempTextViewClick(index: Int) {
        // Change image source
        val tempImageRes = when (index) {
            0 -> R.drawable.ic_environment_selected1
            1 -> R.drawable.ic_environment_selected2
            else -> R.drawable.ic_environment_selected3
        }
        ivTemp.setImageResource(tempImageRes)

        // Update text colors
        tempTextViews.forEachIndexed { i, textView ->
            textView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (i == index) R.color.Greenish_white else R.color.Greenish_green
                )
            )
        }
    }

    private fun handleHumidTextViewClick(index: Int) {
        // Change image source
        val humidImageRes = when (index) {
            0 -> R.drawable.ic_environment_selected1
            1 -> R.drawable.ic_environment_selected2
            else -> R.drawable.ic_environment_selected3
        }
        ivHumid.setImageResource(humidImageRes)

        // Update text colors
        humidTextViews.forEachIndexed { i, textView ->
            textView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (i == index) R.color.Greenish_white else R.color.Greenish_green
                )
            )
        }
    }
}
