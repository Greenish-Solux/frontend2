package com.example.greenish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.greenish.databinding.FragmentCalTodoBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CalTodoFragment : Fragment() {

    private var _binding: FragmentCalTodoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_cal_todo, container, false)
        _binding = FragmentCalTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val floatingActionButton: FloatingActionButton = binding.calendarDialogButton
        floatingActionButton.setOnClickListener {
            showTodoPopup()
        }
    }

    private fun showTodoPopup() {
        val bottomSheet = TodoPopupFragment()
        bottomSheet.show(parentFragmentManager, "TodoPopup")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}