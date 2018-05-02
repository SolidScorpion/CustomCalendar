package com.abc.customcalendar

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import com.abc.customcalendar.pojo.CalendarDay
import com.abc.customcalendar.pojo.RoomCell
import java.util.*

/**
 * Created by Anton P. on 30.04.2018.
 */
class BookingInfo : View {
    private val heightPart = 1 / 3f
    private val widthPart = 1 / 24f
    private val display: DisplayMetrics = context.resources.displayMetrics
    private val cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, display)
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.GREEN
        it.style = Paint.Style.FILL
    }

    private val cellPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.WHITE
        it.style = Paint.Style.STROKE
        it.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, display)
    }

    private val calendar = Calendar.getInstance()

    private val textPaint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.WHITE
        it.textAlign = Paint.Align.CENTER
    }
    private val borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.WHITE
        it.style = Paint.Style.STROKE
        it.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, display)
    }
    private val rect: RectF = RectF()
    private val measureRect = Rect()
    private val sampleText = "Booked Number"
    var calendarDay: CalendarDay? = null
        set(value) {
            calendar.time = value?.date
            field = value
        }
    var roomCell: RoomCell? = null

    constructor(c: Context) : super(c) {
        init(c)
    }

    constructor(c: Context, attributeSet: AttributeSet) : super(c, attributeSet) {
        init(c, attributeSet)
    }

    private fun init(c: Context, attributeSet: AttributeSet? = null) {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect.left = w * widthPart * 16
        rect.top = h * heightPart
        rect.right = w.toFloat()
        rect.bottom = h * heightPart * 2
        textPaint.getTextBounds(sampleText, 0, sampleText.length, measureRect)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), cellPaint)
        drawChip(canvas)
    }

    private fun drawChip(canvas: Canvas) {
        if (roomCell?.bookTime != null) {
            val currentDate = calendar.time
            val cellDay = calendar.get(Calendar.DAY_OF_MONTH)
            val cellMonth = calendar.get(Calendar.MONTH)

            val startDate = roomCell?.bookTime?.startDate
            val endDate = roomCell?.bookTime?.endDate
            calendar.time = startDate
            val startDay = calendar.get(Calendar.DAY_OF_MONTH)
            val startMonth = calendar.get(Calendar.MONTH)
            calendar.time = endDate
            val endDay = calendar.get(Calendar.DAY_OF_MONTH)
            val endMonth = calendar.get(Calendar.MONTH)
            if (cellMonth == startMonth && cellDay == startDay) { //draw start
                val startHour = calendar.get(Calendar.HOUR_OF_DAY)
                rect.left = width * widthPart * startHour
                rect.right = width * 2f
            } else if (cellMonth == endMonth && cellDay <= endDay) {
                calendar.time = endDate
                val endHour = calendar.get(Calendar.HOUR_OF_DAY)
                rect.left = -width.toFloat()
                if (endDay == cellDay) {
                    rect.right = width * widthPart * endHour
                } else {
                    rect.right = width * 2f
                }
            }
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, borderPaint)
        }
    }
}