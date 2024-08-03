package com.example.greenish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.greenish.databinding.FragmentTodoPopupBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class TodoPopupFragment(private val onTodoAdded: (String) -> Unit) : BottomSheetDialogFragment() {
    private var _binding: FragmentTodoPopupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTodoPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btPoptodoDone.setOnClickListener {
            val todoTitle = binding.tvPoptodoEditText.text.toString()
            if (todoTitle.isNotEmpty()) {
                onTodoAdded(todoTitle)
                dismiss()
            }
        }

        binding.btPoptodoCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
