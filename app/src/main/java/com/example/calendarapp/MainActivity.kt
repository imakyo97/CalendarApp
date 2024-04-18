package com.example.calendarapp

import android.os.Bundle
import android.view.LayoutInflater
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
}