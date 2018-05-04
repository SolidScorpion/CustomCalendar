package com.abc.customcalendar.viewholder

import android.view.View
import android.widget.FrameLayout
import com.abc.customcalendar.BookingInfo
import com.abc.customcalendar.R
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder

/**
 * Created by Anton P. on 27.04.2018.
 */
class CellViewHolder(itemView: View, bookingClickListener: BookingInfo.OnBookingClickListener) : AbstractViewHolder(itemView) {
    val cellRoot : FrameLayout = itemView.findViewById(R.id.roomCellRoot)
    val bookingInfo: BookingInfo = itemView.findViewById(R.id.bookingInfo)
    init {
        bookingInfo.bookingClickListener = bookingClickListener
    }
}