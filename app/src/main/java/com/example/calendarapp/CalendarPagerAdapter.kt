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
        // 今日の月から前後1年分のページ数を設定
        const val PAGE_COUNT = 25
    }

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        val calendar = calendar.clone() as Calendar
        // Pagerの中央に今日の月がくるようにカレンダーを作成
        val calendarAddAmount = position - ((PAGE_COUNT - 1) / 2)
        calendar.add(Calendar.MONTH, calendarAddAmount)
        return CalendarFragment(calendar)
    }

    // カレンダーのタイトル
    fun getTitle(position: Int): String {
        // タイトル作成用にカレンダーを複製
        val temporaryCalendar = calendar.clone() as Calendar
        // Pagerの中央に今日の月がくるようにカレンダーを作成
        val calendarAddAmount = position - ((PAGE_COUNT - 1) / 2)
        // positionのカレンダーにする
        temporaryCalendar.add(Calendar.MONTH, calendarAddAmount)

        // 年月にフォーマットして返す
        val locale = Locale("ja", "JP", "JP")
        val dateFormat = SimpleDateFormat("yyyy年M月", locale)
        return dateFormat.format(temporaryCalendar.time)
    }
}