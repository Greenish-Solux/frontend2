package com.example.greenish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenish.databinding.FragmentCalTodoBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prolificinteractive.materialcalendarview.CalendarDay

class CalTodoFragment : Fragment() {

    private var _binding: FragmentCalTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CalViewModel
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCalTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(CalViewModel::class.java)

        setupRecyclerView()
        observeViewModel()

        binding.calendarDialogButton.setOnClickListener {
            showTodoPopup()
        }
    }

    private fun setupRecyclerView() {
        todoAdapter = TodoAdapter(mutableListOf()) { position, isCompleted ->
            viewModel.updateTodoStatus(position, isCompleted)
        }
        binding.todoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todoAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.todoList.observe(viewLifecycleOwner) { todoList ->
            todoAdapter.updateList(todoList)
        }
        viewModel.selectedDateFormatted.observe(viewLifecycleOwner) { formattedDate ->
            binding.tvTodoDateinfo.text = formattedDate
        }
    }

    private fun showTodoPopup() {
        val bottomSheet = TodoPopupFragment { newTodoTitle ->
            viewModel.addTodo(newTodoTitle)
        }
        bottomSheet.show(parentFragmentManager, "TodoPopup")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}