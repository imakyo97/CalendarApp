package com.example.calendarapp

import android.icu.util.Calendar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val calendar = Calendar.getInstance()

    companion object {
        const val PAGE_COUNT = 200
    }

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        val calendar = calendar.clone() as Calendar
        calendar.add(Calendar.MONTH, position)
        return CalendarFragment(calendar)
    }

    // カレンダーのタイトル
    fun getTitle(position: Int): String {
        // タイトル作成用にカレンダーを複製
        val temporaryCalendar = calendar.clone() as Calendar
        // positionのカレンダーにする
        temporaryCalendar.add(Calendar.MONTH, position)

        // 年月にフォーマットして返す
        val locale = Locale("ja", "JP", "JP")
        val dateFormat = SimpleDateFormat("yyyy年M月", locale)
        return dateFormat.format(calendar.time)
    }
}