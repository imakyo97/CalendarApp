package com.example.calendarapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.calendarapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
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

        // 初期表示をPagerの中央にする
        val centerPosition = (CalendarPagerAdapter.PAGE_COUNT - 1) / 2
        activityMainBinding.calendarPager.setCurrentItem(centerPosition, false)
    }

    private inner class CalendarOnPageChanger: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            activityMainBinding.tvMonth.text = calendarPagerAdapter.getTitle(position)
        }
    }

    private inner class CalendarHeaderListener: View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(v: View?) {
            when(v?.id) {
                // < ボタン
                activityMainBinding.btPreviousMonth.id -> {
                    val previousPosition = activityMainBinding.calendarPager.currentItem - 1
                    activityMainBinding.calendarPager.setCurrentItem(previousPosition, true)
                }
                // > ボタン
                activityMainBinding.btNextMonth.id -> {
                    val nextPosition = activityMainBinding.calendarPager.currentItem + 1
                    activityMainBinding.calendarPager.setCurrentItem(nextPosition, true)
                }
            }
        }
    }
}