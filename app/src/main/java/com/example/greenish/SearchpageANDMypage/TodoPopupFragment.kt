package com.example.greenish.SearchpageANDMypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.greenish.databinding.FragmentTodoPopupBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TodoPopupFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTodoPopupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btPoptodoCancel.setOnClickListener {
            dismiss()
        }
        // Handle done button click if needed
        binding.btPoptodoDone.setOnClickListener {
            // Perform action
            dismiss()
        }
    }
    override fun onStart() {
        super.onStart()
        val dialog = dialog as? com.google.android.material.bottomsheet.BottomSheetDialog
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let { bottomSheet ->
            val behavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            bottomSheet.layoutParams = bottomSheet.layoutParams
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
