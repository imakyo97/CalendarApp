package com.example.calendarapp

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Locale

data class CalendarUiState(
    val date: String,
    val position: Int,
    val hasAnimation: Boolean
)

class CalendarViewModel : ViewModel() {
    companion object {
        // 今月から前後1年分のページ数を設定
        const val PAGE_COUNT = 25
    }

    private val calendar = Calendar.getInstance()

    private val _uiState = MutableStateFlow(
        CalendarUiState(
            date = formattedDate((PAGE_COUNT - 1) / 2),
            position = (PAGE_COUNT - 1) / 2,
            hasAnimation = false
        )
    )

    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    fun cloneCalendarMonth(position: Int): Calendar {
        val calendar = calendar.clone() as Calendar
        // Pagerの中央に今月がくるようにカレンダーを作成
        val calendarAddAmount = position - ((PAGE_COUNT - 1) / 2)
        calendar.add(Calendar.MONTH, calendarAddAmount)
        return calendar
    }

    private fun formattedDate(position: Int): String {
        // 日付作成用にカレンダーを複製
        val temporaryCalendar = calendar.clone() as Calendar
        // Pagerの中央に今月がくるようにカレンダーを編集
        val calendarAddAmount = position - ((PAGE_COUNT - 1) / 2)
        temporaryCalendar.add(Calendar.MONTH, calendarAddAmount)

        // 年月にフォーマットして返す
        val locale = Locale("ja", "JP", "JP")
        val dateFormat = SimpleDateFormat("yyyy年M月", locale)
        return dateFormat.format(temporaryCalendar.time)
    }

    fun nextMonth() {
        _uiState.update { uiState ->
            uiState.copy(
                position = uiState.position + 1,
                hasAnimation = true
            )
        }
    }

    fun previousMonth() {
        _uiState.update { uiState ->
            uiState.copy(
                position = uiState.position - 1,
                hasAnimation = true
            )
        }
    }

    fun didChangePage(position: Int) {
        val centerPosition = (PAGE_COUNT - 1) / 2
        when (position) {
            // 表示がPagerの左端まできた時
            0 -> {
                calendar.add(Calendar.MONTH, -centerPosition)
                _uiState.update { uiState ->
                    uiState.copy(
                        date = formattedDate(centerPosition),
                        position = centerPosition,
                        hasAnimation = false
                    )
                }
            }
            // 表示がPagerの右端まできた時
            CalendarViewModel.PAGE_COUNT - 1 -> {
                calendar.add(Calendar.MONTH, centerPosition)
                _uiState.update { uiState ->
                    uiState.copy(
                        date = formattedDate(centerPosition),
                        position = centerPosition,
                        hasAnimation = false
                    )
                }
            }
            else -> {
                _uiState.update { uiState ->
                    uiState.copy(
                        date = formattedDate(position),
                        position = position,
                        hasAnimation = false
                    )
                }
            }
        }
    }
}