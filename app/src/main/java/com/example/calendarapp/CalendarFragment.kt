package com.example.calendarapp

import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.databinding.DayItemBinding
import com.example.calendarapp.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {
    private lateinit var fragmentCalendarBinding: FragmentCalendarBinding
    private lateinit var calendarManager: CalendarManager

    companion object {
        private const val CALENDAR = "calendar"

        // newInstanceメソッドで引数を設定
        fun newInstance(calendar: Calendar): CalendarFragment {
            val fragment = CalendarFragment()
            val args = Bundle()
            args.putSerializable(CALENDAR, calendar)
            fragment.arguments = args
            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            val calendar = it.getSerializable(CALENDAR, Calendar::class.java) as Calendar
            calendarManager = CalendarManager(calendar)
        }

        fragmentCalendarBinding = FragmentCalendarBinding.inflate(inflater, container, false)

        val rvCalendar = fragmentCalendarBinding.rvCalendar
        val numberOfColumns = 7
        val layout = GridLayoutManager(this@CalendarFragment.context, numberOfColumns)
        rvCalendar.layoutManager = layout
        val adapter = RecyclerListAdapter(calendarManager.getDays())
        rvCalendar.adapter = adapter

        // RecyclerViewのスクロールをOFFにする
        rvCalendar.suppressLayout(true)

        return fragmentCalendarBinding.root
    }

    private inner class RecyclerListViewHolder(binding: DayItemBinding): RecyclerView.ViewHolder(binding.root) {
        var tvDay: TextView

        init {
            val weeksCount = calendarManager.getWeeksCount()
            // 日付Viewの高さを指定
            itemView.layoutParams.height = fragmentCalendarBinding.rvCalendar.height / weeksCount
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