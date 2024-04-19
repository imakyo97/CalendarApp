package com.example.calendarapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.databinding.ActivityMainBinding
import com.example.calendarapp.databinding.DayItemBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    private val calendarManager = CalendarManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val activityMainView = activityMainBinding.root
        setContentView(activityMainView)

        activityMainBinding.tvMonth.text = calendarManager.getTitle()
        val calendarHeaderListener = CalendarHeaderListener()
        activityMainBinding.btPreviousMonth.setOnClickListener(calendarHeaderListener)
        activityMainBinding.btNextMonth.setOnClickListener(calendarHeaderListener)

        val rvCalendar = activityMainBinding.rvCalendar
        val numberOfColumns = 7
        val layout = GridLayoutManager(applicationContext, numberOfColumns)
        rvCalendar.layoutManager = layout
        val adapter = RecyclerListAdapter(calendarManager.getDays())
        rvCalendar.adapter = adapter
    }

    private inner class RecyclerListViewHolder(binding: DayItemBinding): RecyclerView.ViewHolder(binding.root) {
        var tvDay: TextView

        init {
            val weeksCount = calendarManager.getWeeksCount()
            // 日付Viewの高さを指定
            itemView.layoutParams.height = activityMainBinding.rvCalendar.height / weeksCount
            tvDay = binding.tvDay
        }
    }

    private inner class RecyclerListAdapter(private val listData: IntArray): RecyclerView.Adapter<RecyclerListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerListViewHolder {
            val binding = DayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return RecyclerListViewHolder(binding)
        }

        override fun onBindViewHolder(holder: RecyclerListViewHolder, position: Int) {
            val day = listData[position].toString()
            holder.tvDay.text = day
        }

        override fun getItemCount(): Int {
            return listData.size
        }
    }

    private inner class CalendarHeaderListener: View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(v: View?) {
            when(v?.id) {
                // < ボタン
                activityMainBinding.btPreviousMonth.id -> {
                    calendarManager.makePreviousMonth()
                }
                // > ボタン
                activityMainBinding.btNextMonth.id -> {
                    calendarManager.makeNextMonth()
                }
            }
            activityMainBinding.tvMonth.text = calendarManager.getTitle()
            // 新しいデータをアダプターに設定
            activityMainBinding.rvCalendar.adapter = RecyclerListAdapter(calendarManager.getDays())
            // RecyclerViewを更新
            activityMainBinding.rvCalendar.adapter?.notifyDataSetChanged()
        }

    }
}