package com.example.calendarapp

import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.calendarapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    companion object {
        // 今日の月から前後1年分のページ数を設定
        const val PAGE_COUNT = 25
    }

    private val calendar = Calendar.getInstance()

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var calendarPagerAdapter: CalendarPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val activityMainView = activityMainBinding.root
        setContentView(activityMainView)

        calendarPagerAdapter = CalendarPagerAdapter(this@MainActivity, calendar)
        activityMainBinding.calendarPager.adapter = calendarPagerAdapter
        val calendarOnPageChanger = CalendarOnPageChanger()
        activityMainBinding.calendarPager.registerOnPageChangeCallback(calendarOnPageChanger)

        val calendarHeaderListener = CalendarHeaderListener()
        activityMainBinding.btPreviousMonth.setOnClickListener(calendarHeaderListener)
        activityMainBinding.btNextMonth.setOnClickListener(calendarHeaderListener)

        // 初期表示をPagerの中央にする
        val centerPosition = (PAGE_COUNT - 1) / 2
        activityMainBinding.calendarPager.setCurrentItem(centerPosition, false)
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
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            activityMainBinding.tvMonth.text = getTitle(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            // Page移動が完了した時
            if (ViewPager2.SCROLL_STATE_IDLE == state) {
                val currentPosition = activityMainBinding.calendarPager.currentItem
                val centerPosition = (PAGE_COUNT - 1) / 2
                when (currentPosition) {
                    // 表示がPagerの左端まできた時
                    0 -> {
                        // 左端の年月が中央にくるようにする
                        calendar.add(Calendar.MONTH, -centerPosition)
                        activityMainBinding.calendarPager.setCurrentItem(centerPosition, false)
                    }
                    // 表示がPagerの右端まできた時
                    PAGE_COUNT - 1 -> {
                        calendar.add(Calendar.MONTH, centerPosition)
                        activityMainBinding.calendarPager.setCurrentItem(centerPosition, false)
                    }
                }
            }
        }
    }

    private inner class CalendarHeaderListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
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

    private inner class CalendarPagerAdapter(
        activity: FragmentActivity,
        private val calendar: Calendar
    ) : FragmentStateAdapter(activity) {
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
    }
}