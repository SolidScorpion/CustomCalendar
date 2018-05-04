package com.abc.customcalendar

import android.content.Context
import android.graphics.*
import android.support.v4.view.GestureDetectorCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.abc.customcalendar.pojo.CalendarDay
import com.abc.customcalendar.pojo.RoomCell
import java.util.*

/**
 * Created by Anton P. on 30.04.2018.
 */
class BookingInfo : View {
    private val widthPart = 1 / 24f
    private val startTriangle: Path = Path()
    private val endTriangle: Path = Path()
    private val display: DisplayMetrics = context.resources.displayMetrics
    var bookingClickListener: OnBookingClickListener? = null
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
    private val gestureDetectorCompat: GestureDetectorCompat = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            if (region.contains(e.x.toInt(), e.y.toInt())) {
                bookingClickListener?.onBookingClicked()
            } else {
                bookingClickListener?.onEmptyBookingClicked()
            }
            return true
        }
    })


    var roomCell: RoomCell? = null
    private val startBounds: RectF = RectF()
    private val region: Region = Region()

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
        rect.top = 0f
        rect.right = w.toFloat()
        rect.bottom = h.toFloat()
        textPaint.getTextBounds(sampleText, 0, sampleText.length, measureRect)
        initStartTriangle()
        initEndTriangle()
    }

    private fun initEndTriangle() {
        endTriangle.moveTo(width * 0.9f, 0f)
        endTriangle.lineTo(0f, 0f)
        endTriangle.lineTo(0f, height.toFloat())
        endTriangle.lineTo(width * 0.9f, 0f)

    }

    private fun initStartTriangle() {
        startTriangle.moveTo(width.toFloat(), 0f)
        startTriangle.lineTo(width.toFloat(), height.toFloat())
        startTriangle.lineTo(width * 0.1f, height.toFloat())
        startTriangle.lineTo(width.toFloat(), 0f)
        startTriangle.computeBounds(startBounds, true)
        region.setPath(startTriangle, Region(startBounds.left.toInt(), startBounds.top.toInt(), startBounds.right.toInt(), startBounds.bottom.toInt()))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetectorCompat.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), cellPaint)
        drawChip(canvas)
    }

    private fun drawChip(canvas: Canvas) {
        if (roomCell?.bookTime != null) {
            calendar.time = calendarDay?.date
            val cellDay = calendar.get(Calendar.DAY_OF_MONTH)
            val cellMonth = calendar.get(Calendar.MONTH)
            canvas.save()
            val startDate = roomCell?.bookTime?.get(0)?.startDate
            val endDate = roomCell?.bookTime?.get(0)?.endDate
            calendar.time = startDate
            val startDay = calendar.get(Calendar.DAY_OF_MONTH)
            val startMonth = calendar.get(Calendar.MONTH)
            calendar.time = endDate
            val endDay = calendar.get(Calendar.DAY_OF_MONTH)
            val endMonth = calendar.get(Calendar.MONTH)
            if (cellMonth == startMonth && cellDay == startDay) {
                canvas.drawPath(startTriangle, paint)//draw start
            } else if (cellMonth == endMonth && cellDay <= endDay) {
                if (endDay == cellDay) {
                    canvas.drawPath(endTriangle, paint)
                } else {
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
                }
            }
            if (roomCell?.bookTime?.size!! > 1) {
                canvas.drawPath(startTriangle, paint)
            }
        }
    }

    interface OnBookingClickListener {
        fun onBookingClicked()
        fun onEmptyBookingClicked()
    }
}