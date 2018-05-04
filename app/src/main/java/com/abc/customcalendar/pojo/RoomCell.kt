package com.abc.customcalendar.pojo

import java.util.*

/**
 * Created by Anton P. on 27.04.2018.
 */
data class RoomCell(val isBooked: Boolean, val bookTime: Array<BookOrder>? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoomCell

        if (isBooked != other.isBooked) return false
        if (!Arrays.equals(bookTime, other.bookTime)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isBooked.hashCode()
        result = 31 * result + (bookTime?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }
}