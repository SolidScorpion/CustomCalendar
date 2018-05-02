package com.abc.customcalendar.viewholder

import android.view.View
import android.widget.TextView
import com.abc.customcalendar.R
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder

/**
 * Created by Anton P. on 27.04.2018.
 */
class CalendarDayViewHolder(itemView: View) : AbstractViewHolder(itemView) {
    val monthNameTv: TextView = itemView.findViewById(R.id.calendarMonth)
    val dayTv: TextView = itemView.findViewById(R.id.calendarDay)
    val weekDay:TextView = itemView.findViewById(R.id.calendarWeekDay)
}