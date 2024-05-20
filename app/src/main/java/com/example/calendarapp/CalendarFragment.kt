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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.databinding.DayItemBinding
import com.example.calendarapp.databinding.FragmentCalendarBinding
import kotlinx.coroutines.launch

class CalendarFragment : Fragment() {
    private lateinit var fragmentCalendarBinding: FragmentCalendarBinding
    private lateinit var factory: CalendarFragmentViewModel.Factory

    private val viewModel: CalendarFragmentViewModel by viewModels { factory }

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
            factory = CalendarFragmentViewModel.Factory(calendar)
        }

        fragmentCalendarBinding = FragmentCalendarBinding.inflate(inflater, container, false)
        return fragmentCalendarBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvCalendar = fragmentCalendarBinding.rvCalendar
        val layout = GridLayoutManager(this@CalendarFragment.context, CalendarFragmentViewModel.NUMBER_OF_COLUMNS)
        rvCalendar.layoutManager = layout
        val adapter = RecyclerListAdapter()
        rvCalendar.adapter = adapter

        // RecyclerViewのスクロールをOFFにする
        rvCalendar.suppressLayout(true)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { listData ->
                    adapter.setListData(listData)
                }
            }
        }
    }

    private inner class RecyclerListViewHolder(binding: DayItemBinding): RecyclerView.ViewHolder(binding.root) {
        var tvDay: TextView

        init {
            val weeksCount = viewModel.getWeeksCount()
            // 日付Viewの高さを指定
            itemView.layoutParams.height = fragmentCalendarBinding.rvCalendar.height / weeksCount
            tvDay = binding.tvDay
        }
    }

    private inner class RecyclerListAdapter(): RecyclerView.Adapter<RecyclerListViewHolder>() {
        private val listData = mutableListOf<Int>()

        fun setListData(data: IntArray) {
            listData.clear()
            listData.addAll(data.toList())
            notifyDataSetChanged()
        }

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