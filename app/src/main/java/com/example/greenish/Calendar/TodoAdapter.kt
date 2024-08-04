package com.example.greenish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.greenish.databinding.ItemTodoBinding

data class TodoItem(val title: String, var isCompleted: Boolean)

class TodoAdapter(
    private var todoList: MutableList<TodoItem>,
    private val onItemCheckedChanged: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todoItem: TodoItem, onCheckedChanged: (Boolean) -> Unit) {
            binding.todoText.text = todoItem.title
            binding.todoCheckBox.isChecked = todoItem.isCompleted
            binding.todoCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChanged(isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todoList[position]) { isChecked ->
            onItemCheckedChanged(position, isChecked)
        }
    }

    override fun getItemCount() = todoList.size

    fun updateList(newList: List<TodoItem>) {
        todoList.clear()
        todoList.addAll(newList)
        notifyDataSetChanged()
    }
}