package com.abc.customcalendar

import android.content.Context
import android.graphics.*
import android.support.v4.view.GestureDetectorCompat
import android.util.AttributeSet
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
    private val startTriangle: Path = Path()
    private val endTriangle: Path = Path()
    private val offsetEnd = 0.9f
    private val offsetStart = 0.1f
    private val shaderGroupOnline = BitmapShader(BitmapFactory.decodeResource(resources,
            R.drawable.texture_group_online),
            Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
    private val shaderGroupOffline: BitmapShader = BitmapShader(BitmapFactory.decodeResource(resources,
            R.drawable.texture_group_offline),
            Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
    var bookingClickListener: OnBookingClickListener? = null
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.GREEN
        it.style = Paint.Style.FILL
        it.shader = shaderGroupOffline
    }

    private val calendar = Calendar.getInstance()

    var calendarDay: CalendarDay? = null
        set(value) {
            field = value
            calendar.time = value?.date
        }
    private val gestureDetectorCompat: GestureDetectorCompat = GestureDetectorCompat(context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    return if (shouldFill) {
                        bookingClickListener?.onBookingClicked()
                        true
                    } else {
                        val x = e.x.toInt()
                        val y = e.y.toInt()
                        if (shouldDrawStart && startRegion.contains(x, y)) {
                            bookingClickListener?.onBookingClicked()
                        } else if (shouldDrawEnd && endRegion.contains(x, y)) {
                            bookingClickListener?.onBookingClicked()
                        } else {
                            if (!(shouldDrawStart && shouldDrawEnd)) { // in case cell has start
                                // and end booking do not react on clicking the empty space between them
                                bookingClickListener?.onEmptyBookingClicked()
                            }
                        }
                        true
                    }
                }
            })
    private var shouldDrawEnd = false
    private var shouldDrawStart = false
    private var shouldFill = false


    var roomCell: RoomCell? = null
    private val endBounds: RectF = RectF()
    private val endRegion: Region = Region()
    private val startBounds: RectF = RectF()
    private val startRegion: Region = Region()

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
        initStartTriangle()
        initEndTriangle()
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        shouldDrawEnd = false
        shouldDrawStart = false
        shouldFill = false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (roomCell?.bookTime != null) {
            calendar.time = calendarDay?.date
            val cellDay = calendar.get(Calendar.DAY_OF_MONTH)
            val cellMonth = calendar.get(Calendar.MONTH)
            val startDate = roomCell?.bookTime?.get(0)?.startDate
            val endDate = roomCell?.bookTime?.get(0)?.endDate
            calendar.time = startDate
            val startDay = calendar.get(Calendar.DAY_OF_MONTH)
            val startMonth = calendar.get(Calendar.MONTH)
            calendar.time = endDate
            val endDay = calendar.get(Calendar.DAY_OF_MONTH)
            val endMonth = calendar.get(Calendar.MONTH)
            if (cellMonth == startMonth && cellDay == startDay) {
                shouldDrawStart = true
            } else if (cellMonth == endMonth && cellDay <= endDay) {
                if (endDay == cellDay) {
                    shouldDrawEnd = true
                } else {
                    shouldFill = true
                }
            }
            if (roomCell?.bookTime?.size!! > 1) {
                shouldDrawStart = true
            }
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetectorCompat.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawChip(canvas)
    }

    private fun drawChip(canvas: Canvas) {
        if (shouldDrawStart) {
            canvas.drawPath(startTriangle, paint)
        }
        if (shouldDrawEnd) {
            canvas.drawPath(endTriangle, paint)
        }
        if (shouldFill) {
            canvas.drawRect(0f, height * offsetStart,
                    width.toFloat(), height * offsetEnd, paint)
        }
    }

    private fun initEndTriangle() {
        endTriangle.moveTo(width * offsetEnd, height * offsetStart)
        endTriangle.lineTo(0f, height * offsetStart)
        endTriangle.lineTo(0f, height * offsetEnd)
        endTriangle.lineTo(width * offsetEnd, height * offsetStart)
        endTriangle.computeBounds(endBounds, true)
        endRegion.setPath(endTriangle, Region(endBounds.left.toInt(), endBounds.top.toInt(),
                endBounds.right.toInt(), endBounds.bottom.toInt()))
    }

    private fun initStartTriangle() {
        startTriangle.moveTo(width.toFloat(), height * offsetStart)
        startTriangle.lineTo(width.toFloat(), height * offsetEnd)
        startTriangle.lineTo(width * offsetStart, height * offsetEnd)
        startTriangle.lineTo(width.toFloat(), height * offsetStart)
        startTriangle.computeBounds(startBounds, true)
        startRegion.setPath(startTriangle, Region(startBounds.left.toInt(), startBounds.top.toInt(),
                startBounds.right.toInt(), startBounds.bottom.toInt()))
    }

    interface OnBookingClickListener {
        fun onBookingClicked()
        fun onEmptyBookingClicked()
    }
}