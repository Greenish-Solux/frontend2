package com.example.greenish.ui.home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("formattedDate")fun TextView.setFormattedDate(date: String?) {
    if (date.isNullOrEmpty()) {
        text = ""
        return
    }

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val parsedDate = dateFormatter.parse(date) ?: return

    val dayFormatter = SimpleDateFormat("d", Locale.getDefault())  // Day of the month
    val monthFormatter = SimpleDateFormat("MMM", Locale.getDefault())  // Abbreviated month
    val formattedDate = "${dayFormatter.format(parsedDate)} ${monthFormatter.format(parsedDate)}"
    text = formattedDate
}

@BindingAdapter("dayOfWeek")fun TextView.setDayOfWeek(date: String?) {
    if (date.isNullOrEmpty()) {
        text = ""
        return
    }

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val parsedDate = dateFormatter.parse(date) ?: return

    val dayFormatter = SimpleDateFormat("EEEE", Locale.getDefault())  // Full day of the week
    text = dayFormatter.format(parsedDate)
}