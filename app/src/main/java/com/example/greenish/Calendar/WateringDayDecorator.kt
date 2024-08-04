package com.example.greenish

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class WateringDayDecorator(private val dates: Set<CalendarDay>) : DayViewDecorator {
    private val color = Color.parseColor("#38D0FF") // 물주기 점의 색상

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(6f, color)) // 6f는 점의 반지름
    }
}