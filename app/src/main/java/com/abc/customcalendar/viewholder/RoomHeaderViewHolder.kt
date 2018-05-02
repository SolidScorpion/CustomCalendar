package com.abc.customcalendar.viewholder

import android.view.View
import android.widget.TextView
import com.abc.customcalendar.R
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder

/**
 * Created by Anton P. on 27.04.2018.
 */
class RoomHeaderViewHolder(itemView: View) : AbstractViewHolder(itemView) {
    val roomHeaderTv: TextView = itemView.findViewById(R.id.roomHeaderTv)
}