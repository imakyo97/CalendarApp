package com.example.calendarapp

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
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            activityMainBinding.tvMonth.text = calendarPagerAdapter.getTitle(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            // Page移動が完了した時
            if (ViewPager2.SCROLL_STATE_IDLE == state) {
                val currentPosition = activityMainBinding.calendarPager.currentItem
                val centerPosition = (CalendarPagerAdapter.PAGE_COUNT - 1) / 2
                when (currentPosition) {
                    // 表示がPagerの左端まできた時
                    0 -> {
                        // 左端の年月が中央にくるようにする
                        calendarPagerAdapter.addCalendarMonth(-centerPosition)
                        activityMainBinding.calendarPager.setCurrentItem(centerPosition, false)
                    }
                    // 表示がPagerの右端まできた時
                    CalendarPagerAdapter.PAGE_COUNT - 1 -> {
                        calendarPagerAdapter.addCalendarMonth(centerPosition)
                        activityMainBinding.calendarPager.setCurrentItem(centerPosition, false)
                    }
                }
            }
        }
    }

    private inner class CalendarHeaderListener: View.OnClickListener {
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