package com.abc.customcalendar.data

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abc.customcalendar.R
import com.abc.customcalendar.pojo.CalendarDay
import com.abc.customcalendar.pojo.Room
import com.abc.customcalendar.pojo.RoomCell
import com.abc.customcalendar.viewholder.CalendarDayViewHolder
import com.abc.customcalendar.viewholder.CellViewHolder
import com.abc.customcalendar.viewholder.RoomHeaderViewHolder
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder

/**
 * Created by Anton P. on 27.04.2018.
 */
class CalendarTableAdapter(c: Context) : AbstractTableAdapter<CalendarDay, Room, RoomCell>(c) {
    private var inflater = LayoutInflater.from(c)
    private fun inflate(layoutRes: Int, parent: ViewGroup? = null): View {
        return inflater.inflate(layoutRes, parent, false)
    }

    override fun onCreateColumnHeaderViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return CalendarDayViewHolder(inflate(R.layout.item_column_header, parent))
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return RoomHeaderViewHolder(inflate(R.layout.item_row_header, parent))
    }

    override fun onCreateCellViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return CellViewHolder(inflate(R.layout.item_room_cell, parent))
    }

    override fun onCreateCornerView(): View = inflate(R.layout.item_corner)

    override fun onBindCellViewHolder(holder: AbstractViewHolder, cellItemModel: Any, columnPosition: Int, rowPosition: Int) {
        val cellViewHolder = holder as? CellViewHolder
        cellViewHolder?.let {
            val item = getColumnHeaderItem(columnPosition)
            val context = it.cellRoot.context
            holder.cellRoot.setBackgroundColor(ContextCompat.getColor(context, if (item.isHoliday) R.color.holiday else R.color.usualDay))
            holder.bookingInfo.roomCell = cellItemModel as RoomCell
            holder.bookingInfo.calendarDay = item
        }
    }

    override fun onBindColumnHeaderViewHolder(holder: AbstractViewHolder?, columnHeaderItemModel: Any?, columnPosition: Int) {
        if (holder as? CalendarDayViewHolder != null && columnHeaderItemModel as? CalendarDay != null) {
            holder.dayTv.text = columnHeaderItemModel.day
            holder.monthNameTv.text = columnHeaderItemModel.month
            holder.weekDay.text = columnHeaderItemModel.weekDay
            val context = holder.weekDay.context
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, if (columnHeaderItemModel.isHoliday) R.color.holiday else R.color.usualDay))
        }
    }

    override fun onBindRowHeaderViewHolder(holder: AbstractViewHolder?, rowHeaderItemModel: Any?, rowPosition: Int) {
        if (holder as? RoomHeaderViewHolder != null && rowHeaderItemModel as? Room != null) {
            holder.roomHeaderTv.text = rowHeaderItemModel.roomName
        }
    }

    override fun getCellItemViewType(position: Int) = 0

    override fun getColumnHeaderItemViewType(position: Int) = 0

    override fun getRowHeaderItemViewType(position: Int) = 0
}