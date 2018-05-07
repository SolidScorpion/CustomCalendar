package com.abc.customcalendar.pojo

import java.util.*

/**
 * Created by Anton P. on 27.04.2018.
 */
data class CalendarDay(val year: String, val month: String, val day: String,
                       val weekDay: String, val isHoliday: Boolean,
                       val date: Date)