package com.abc.customcalendar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.abc.customcalendar.data.CalendarTableAdapter
import com.abc.customcalendar.pojo.BookOrder
import com.abc.customcalendar.pojo.CalendarDay
import com.abc.customcalendar.pojo.Room
import com.abc.customcalendar.pojo.RoomCell
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.listener.ITableViewListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val headerDateFormat = SimpleDateFormat("MMM dd EE")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tableView = findViewById<TableView>(R.id.contentContainer)
        tableView.adapter = CalendarTableAdapter(this)
        val instance = Calendar.getInstance()
        val startTime = instance.time
        instance.set(Calendar.HOUR_OF_DAY, 13)
        val startBookTime = instance.time
        instance.add(Calendar.DAY_OF_MONTH, 2)
        instance.set(Calendar.HOUR_OF_DAY, 15)
        tableView.isShowVerticalSeparators = false
        tableView.isShowHorizontalSeparators = false
        val bookOrder = BookOrder(startBookTime, instance.time)
        instance.time = startTime
        val arrayList = ArrayList<Room>()
        val arrayList2 = ArrayList<List<RoomCell>>()
        val arrayList3 = ArrayList<CalendarDay>()
        tableView.isIgnoreSelectionColors = true
        tableView.tableViewListener = object: ITableViewListener {
            override fun onCellLongPressed(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {
                Toast.makeText(this@MainActivity,"onCellLongPressed column $column row $row", Toast.LENGTH_SHORT).show()
            }

            override fun onColumnHeaderLongPressed(columnHeaderView: RecyclerView.ViewHolder, column: Int) {
                Toast.makeText(this@MainActivity,"onColumnHeaderLongPressed column $column ", Toast.LENGTH_SHORT).show()
            }

            override fun onRowHeaderClicked(rowHeaderView: RecyclerView.ViewHolder, row: Int) {
                Toast.makeText(this@MainActivity,"onRowHeaderClicked column row $row", Toast.LENGTH_SHORT).show()
            }

            override fun onColumnHeaderClicked(columnHeaderView: RecyclerView.ViewHolder, column: Int) {
                Toast.makeText(this@MainActivity,"onColumnHeaderClicked column $column ", Toast.LENGTH_SHORT).show()
            }

            override fun onCellClicked(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {
                Toast.makeText(this@MainActivity,"onCellClicked column $column row $row", Toast.LENGTH_SHORT).show()
            }

            override fun onRowHeaderLongPressed(rowHeaderView: RecyclerView.ViewHolder, row: Int) {
                Toast.makeText(this@MainActivity,"onRowHeaderLongPressed column row $row", Toast.LENGTH_SHORT).show()
            }

        }
        for (i in 0..29) {
            val roomNumber = i + 1
            arrayList.add(Room("Room $roomNumber", i.toString()))
            instance.time = startTime
            instance.add(Calendar.DAY_OF_MONTH, i)
            val dayOFWeek = instance.get(Calendar.DAY_OF_WEEK)
            val split = headerDateFormat.format(instance.time).split(" ")
            arrayList3.add(CalendarDay(split[0], split[1],
                    split[2], dayOFWeek == Calendar.SATURDAY || dayOFWeek == Calendar.SUNDAY,
                    instance.time))
            val newList = ArrayList<RoomCell>()
            for (k in 0..29) {
                val roomCell =
                        if (i == 0 && (k == 0 || k == 1 || k == 2)) RoomCell(false, bookOrder)
                        else RoomCell(false)
                newList.add(roomCell)
            }
            arrayList2.add(newList)
        }
        tableView.adapter.setAllItems(arrayList3, arrayList, arrayList2)
    }
}
