package com.example.calendarapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.calendarapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: CalendarViewModel by viewModels()

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var calendarPagerAdapter: CalendarPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val activityMainView = activityMainBinding.root
        setContentView(activityMainView)

        calendarPagerAdapter = CalendarPagerAdapter(this@MainActivity)
        activityMainBinding.calendarPager.adapter = calendarPagerAdapter
        val calendarOnPageChanger = CalendarOnPageChanger()
        activityMainBinding.calendarPager.registerOnPageChangeCallback(calendarOnPageChanger)

        val calendarHeaderListener = CalendarHeaderListener()
        activityMainBinding.btPreviousMonth.setOnClickListener(calendarHeaderListener)
        activityMainBinding.btNextMonth.setOnClickListener(calendarHeaderListener)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    activityMainBinding.tvMonth.text = uiState.date
                    activityMainBinding.calendarPager.setCurrentItem(
                        uiState.position,
                        uiState.hasAnimation
                    )
                }
            }
        }
    }

    private inner class CalendarOnPageChanger : ViewPager2.OnPageChangeCallback() {
        /* ドラッグ時のイベント呼び出し順
        1. onPageScrollStateChanged state:1
        2. onPageScrolled（複数回）
        3. onPageScrollStateChanged state:2
        4. onPageSelected
        5. onPageScrolled（複数回）
        6. onPageScrollStateChanged state:0
        */

        /* setCurrentItem(item:, smoothScroll)でのイベント呼び出し順
        1. onPageScrollStateChanged state:2
        2. onPageSelected
        3. onPageScrolled
        4. onPageScrollStateChanged state:0
        */
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            // Page移動が完了した時
            if (ViewPager2.SCROLL_STATE_IDLE == state) {
                val currentPosition = activityMainBinding.calendarPager.currentItem
                viewModel.didChangePage(currentPosition)
            }
        }
    }

    private inner class CalendarHeaderListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                // < ボタン
                activityMainBinding.btPreviousMonth.id -> {
                    viewModel.previousMonth()
                }
                // > ボタン
                activityMainBinding.btNextMonth.id -> {
                    viewModel.nextMonth()
                }
            }
        }
    }

    private inner class CalendarPagerAdapter(
        activity: FragmentActivity,
    ) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return CalendarViewModel.PAGE_COUNT
        }

        override fun createFragment(position: Int): Fragment {
            val calendar = viewModel.cloneCalendarMonth(position)
            return CalendarFragment.newInstance(calendar)
        }
    }
}