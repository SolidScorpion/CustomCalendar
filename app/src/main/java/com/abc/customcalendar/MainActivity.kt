package com.abc.customcalendar

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.abc.customcalendar.data.CalendarTableAdapter
import com.abc.customcalendar.pojo.BookOrder
import com.abc.customcalendar.pojo.CalendarDay
import com.abc.customcalendar.pojo.Room
import com.abc.customcalendar.pojo.RoomCell
import com.abc.customcalendar.tableview.TableView
import com.abc.customcalendar.tableview.listener.ITableViewListener
import com.abc.customcalendar.viewholder.CellViewHolder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), BookingInfo.OnBookingClickListener, DatePickerDialog.OnDateSetListener {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var tableView: TableView
    private val calendarInstance = Calendar.getInstance()
    private val startTime = Date(System.currentTimeMillis())
    private val offsetTime = calendarInstance.let {
        it.time = startTime
        it.add(Calendar.MONTH, -1)
        it.time
    }
    private val endDate = calendarInstance.let {
        it.time = startTime
        it.add(Calendar.MONTH, 18)
        it.time
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        calendarInstance.set(year, month, dayOfMonth)
        val targetDay = calendarInstance.timeInMillis
        calendarInstance.time = offsetTime
        val initialTime = calendarInstance.timeInMillis
        val day = TimeUnit.DAYS.convert(targetDay - initialTime, TimeUnit.MILLISECONDS)
        tableView.scrollToColumnPosition(day.toInt())
        Log.d(TAG, "Date set is $year month $month.$dayOfMonth")
    }

    override fun onBookingClicked() {
        showToast("Booking clicked")
    }

    override fun onEmptyBookingClicked() {
        showToast("Empty place clicked")
    }

    private val headerDateFormat = SimpleDateFormat("MMM dd EE")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tableView = findViewById(R.id.contentContainer)
        tableView.adapter = CalendarTableAdapter(this, this, View.OnClickListener {
            calendarInstance.time = startTime
            val datePickerDialog = DatePickerDialog(this, this,
                    calendarInstance.get(Calendar.YEAR),
                    calendarInstance.get(Calendar.MONTH),
                    calendarInstance.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = calendarInstance.timeInMillis
            calendarInstance.time = endDate
            datePickerDialog.datePicker.maxDate = calendarInstance.timeInMillis
            calendarInstance.time = startTime
            datePickerDialog.show()
        })
        tableView.tableViewListener = object : ITableViewListener {
            override fun onCellLongPressed(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {

            }

            override fun onColumnHeaderLongPressed(columnHeaderView: RecyclerView.ViewHolder, column: Int) {
            }

            override fun onRowHeaderClicked(rowHeaderView: RecyclerView.ViewHolder, row: Int) {
            }

            override fun onColumnHeaderClicked(columnHeaderView: RecyclerView.ViewHolder, column: Int) {
            }

            override fun onCellClicked(cellView: RecyclerView.ViewHolder, column: Int, row: Int, e: MotionEvent) {
                (cellView as CellViewHolder).bookingInfo.onTouchEvent(e)
            }

            override fun onRowHeaderLongPressed(rowHeaderView: RecyclerView.ViewHolder, row: Int) {
            }
        }
        val (bookOrder, secondBookOrder) = initStubBooking()
        val arrayList = ArrayList<Room>()
        val arrayList2 = ArrayList<List<RoomCell>>()
        val arrayList3 = ArrayList<CalendarDay>()
        tableView.isIgnoreSelectionColors = true
        calendarInstance.time = offsetTime
        val startTimeInMillis = calendarInstance.timeInMillis
        calendarInstance.time = endDate
        val endTimeInMillis = calendarInstance.timeInMillis
        val difference = endTimeInMillis - startTimeInMillis
        val differenceInDays = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
        calendarInstance.time = offsetTime
        val offsetStartInMillis = calendarInstance.timeInMillis
        calendarInstance.time = startTime
        val currentTime = calendarInstance.timeInMillis
        val currentDayIndex = TimeUnit.DAYS.convert(currentTime - offsetStartInMillis, TimeUnit.MILLISECONDS)
        Log.d(TAG, "Time difference in days: $differenceInDays")
        for (i in 0..differenceInDays.toInt()) {
            val roomNumber = i + 1
            arrayList.add(Room("Room $roomNumber", i.toString()))
            calendarInstance.time = offsetTime
            calendarInstance.add(Calendar.DAY_OF_MONTH, i)
            val dayOFWeek = calendarInstance.get(Calendar.DAY_OF_WEEK)
            val split = headerDateFormat.format(calendarInstance.time).split(" ")
            arrayList3.add(CalendarDay(split[0], split[1],
                    split[2], dayOFWeek == Calendar.SATURDAY || dayOFWeek == Calendar.SUNDAY,
                    calendarInstance.time))
            val newList = ArrayList<RoomCell>()
            for (k in 0..differenceInDays.toInt()) {
                val roomCell =
                        if (i == 0 && (k == 0 || k == 1)) RoomCell(false, arrayOf(bookOrder))
                        else if (i == 0 && k == 2) RoomCell(false, arrayOf(bookOrder, secondBookOrder))
                        else if (i == 0 && k == 3) RoomCell(false, arrayOf(secondBookOrder))
                        else RoomCell(false)
                newList.add(roomCell)
            }
            arrayList2.add(newList)
        }
        tableView.adapter.setAllItems(arrayList3, arrayList, arrayList2)
        tableView.scrollToColumnPosition(currentDayIndex.toInt())
    }

    private fun initStubBooking(): Pair<BookOrder, BookOrder> {
        calendarInstance.time = offsetTime
        val startBookTime = calendarInstance.time
        calendarInstance.add(Calendar.DAY_OF_MONTH, 2)
        val firsBookIngEnd = calendarInstance.time
        val bookOrder = BookOrder(startBookTime, firsBookIngEnd)
        calendarInstance.add(Calendar.DAY_OF_MONTH, 1)
        val secondBookOrder = BookOrder(firsBookIngEnd, calendarInstance.time)
        calendarInstance.time = offsetTime
        return Pair(bookOrder, secondBookOrder)
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
