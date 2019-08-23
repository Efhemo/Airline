package com.efhems.airlines.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.efhems.airlines.R
import com.efhems.airlines.domain.Schedule
import kotlinx.android.synthetic.main.schedules_item.view.*

/**
 * Recyclerview Adapter setUp
 * */
class ScheduleRCAdapter(val onScheduleClick: OnScheculeClickListener) :
    ListAdapter<Schedule, ScheduleRCAdapter.ItemViewholder>(DiffCallback()) {

    interface OnScheculeClickListener {
        fun onClick(schedule: Schedule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.schedules_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Schedule) = with(itemView) {

            this.time.text = item.departureTime.substring(11, 16)
            this.flight_no.text = item.flightNumber.toString()

            setOnClickListener {
                onScheduleClick.onClick(item)
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Schedule>() {
    override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem.departureTime == newItem.departureTime
    }

    override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem == newItem
    }
}