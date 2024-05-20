package com.example.calendarapp

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalendarFragmentViewModel(private val calendar: Calendar) : ViewModel() {
    class Factory(private val calendar: Calendar) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return CalendarFragmentViewModel(
                calendar
            ) as T
        }
    }

    companion object {
        // 今月から前後1年分のページ数を設定
        const val NUMBER_OF_COLUMNS = 7
    }

    private val calendarManager = CalendarManager()
    private val _uiState = MutableStateFlow(calendarManager.getDays(calendar))

    val uiState = _uiState.asStateFlow()

    fun getWeeksCount(): Int {
        return calendarManager.getWeeksCount(calendar)
    }
}