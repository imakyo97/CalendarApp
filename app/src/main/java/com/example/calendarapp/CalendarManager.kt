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
        // 最後にカレンダーを元に戻すためのDate
        val temporaryTime = calendar.time

        // 曜日の数
        val dayOfWeekCount = 7
        // カレンダーに表示する日付の数
        val calendarDaysCount = getWeeksCount() * dayOfWeekCount
        // カレンダーの日付を1日にする
        calendar.set(Calendar.DATE, 1)
        // 曜日を取得（日曜日の1から土曜日の7までの数字）
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        // カレンダーに表示する最初の日付にする
        calendar.add(Calendar.DATE, -dayOfWeek + 1)

        // カレンダーの日付リストを作成
        val days: MutableList<Int> = mutableListOf()
        repeat(calendarDaysCount) {
            days.add(calendar.get(Calendar.DATE))
            calendar.add(Calendar.DATE, 1)
        }
        // カレンダーを元に戻す
        calendar.time = temporaryTime

        return days.toIntArray()
    }

    // カレンダーに表示される週の数
    fun getWeeksCount(): Int {
        return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)
    }

    // 前の月
    fun makePreviousMonth() {
        calendar.add(Calendar.MONTH, -1)
    }

    // 次の月
    fun makeNextMonth() {
        calendar.add(Calendar.MONTH, 1)
    }
}