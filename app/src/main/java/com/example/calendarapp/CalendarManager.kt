package com.example.calendarapp

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarManager {
    private val calendar = Calendar.getInstance()

    // カレンダーのタイトル
    fun getTitle(): String {
        val locale = Locale("ja", "JP", "JP")
        val dateFormat = SimpleDateFormat("yyyy年M月", locale)
        return dateFormat.format(calendar.time)
    }

    fun getDays(): IntArray {
        // メソッド内で一時的に使用するCalendar
        val temporaryCalendar = calendar
        // 曜日の数
        val dayOfWeekCount = 7
        // カレンダーに表示する日付の数
        val calendarDaysCount = getWeeksCount() * dayOfWeekCount
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
    fun getWeeksCount(): Int {
        return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)
    }
}