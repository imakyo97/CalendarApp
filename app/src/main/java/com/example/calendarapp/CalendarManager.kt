package com.example.calendarapp

import android.icu.util.Calendar

class CalendarManager() {
    fun getDays(calendar: Calendar): IntArray {
        // 日付配列の作成用にカレンダーを複製
        val temporaryCalendar = calendar.clone() as Calendar

        // 曜日の数
        val dayOfWeekCount = 7
        // カレンダーに表示する日付の数
        val calendarDaysCount = getWeeksCount(calendar) * dayOfWeekCount
        // カレンダーの日付を1日にする
        temporaryCalendar.set(Calendar.DATE, 1)
        // 曜日を取得（日曜日の1から土曜日の7までの数字）
        val dayOfWeek = temporaryCalendar.get(Calendar.DAY_OF_WEEK)
        // カレンダーに表示する最初の日付にする
        temporaryCalendar.add(Calendar.DATE, -dayOfWeek + 1)

        // カレンダーの日付リストを作成
        val days: MutableList<Int> = mutableListOf()
        repeat(calendarDaysCount) {
            days.add(temporaryCalendar.get(Calendar.DATE))
            temporaryCalendar.add(Calendar.DATE, 1)
        }

        return days.toIntArray()
    }

    // カレンダーに表示される週の数
    fun getWeeksCount(calendar: Calendar): Int {
        return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)
    }
}