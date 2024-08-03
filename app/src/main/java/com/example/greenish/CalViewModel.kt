package com.example.greenish

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.Locale

class CalViewModel : ViewModel() {

    private val _selectedDate = MutableLiveData<CalendarDay>()
    private val _selectedDateFormatted = MutableLiveData<String>()
    val selectedDateFormatted: LiveData<String> = _selectedDateFormatted

    private val todoMap = mutableMapOf<CalendarDay, MutableList<TodoItem>>()

    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList: LiveData<List<TodoItem>> = _todoList

    private val _datesWithTodos = MutableLiveData<Set<CalendarDay>>()
    val datesWithTodos: LiveData<Set<CalendarDay>> = _datesWithTodos

    fun setSelectedDate(date: CalendarDay) {
        _selectedDate.value = date
        updateTodoList()
        updateFormattedDate(date)
    }
    fun getSelectedDate(): CalendarDay {
        return _selectedDate.value ?: CalendarDay.today()
    }

    private fun updateFormattedDate(date: CalendarDay) {
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        _selectedDateFormatted.value = formatter.format(date.date)
    }

    private fun updateTodoList() {
        val date = _selectedDate.value ?: return
        _todoList.value = todoMap.getOrPut(date) { mutableListOf() }
    }

    fun addTodo(title: String) {
        val date = _selectedDate.value ?: return
        val list = todoMap.getOrPut(date) { mutableListOf() }
        list.add(TodoItem(title, false))
        _todoList.value = list
        updateDatesWithTodos()
    }

    fun updateTodoStatus(position: Int, isCompleted: Boolean) {
        val date = _selectedDate.value ?: return
        val list = todoMap[date] ?: return
        if (position in list.indices) {
            list[position].isCompleted = isCompleted
            _todoList.value = list
        }
    }

    fun updateDatesWithTodos() {
        _datesWithTodos.value = todoMap.keys.filter { todoMap[it]?.isNotEmpty() == true }.toSet()
    }

}