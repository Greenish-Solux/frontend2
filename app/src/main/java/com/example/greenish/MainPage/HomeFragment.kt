package com.example.greenish.MainPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.greenish.R
import com.example.greenish.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var imagePagerAdapter: ImagePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the image pager adapter with a list of images
        val images = listOf(R.drawable.ic_banner1, R.drawable.ic_banner2, R.drawable.ic_banner3, R.drawable.ic_banner4, R.drawable.ic_banner5)
        imagePagerAdapter = ImagePagerAdapter(images)
        binding.viewPager.adapter = imagePagerAdapter

        // Set up click listener for the iv_home_bookmark_empty image
        binding.ivHomeBookmarkEmpty.setOnClickListener {
            val intent = Intent(context, GuidebookActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
