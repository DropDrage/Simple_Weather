package com.dropdrage.simpleweather.presentation.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.graphics.alpha
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.presentation.util.ViewUtils
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

private const val SWEEP_START_ANGLE = 0
private const val SWEEP_END_ANGLE = 180
private val SWEEP_DISTANCE = (SWEEP_END_ANGLE - SWEEP_START_ANGLE).absoluteValue.toFloat()

private const val ARC_START_ANGLE = 180f

private const val RGB_BITS_COUNT = 8 * 3
private const val PAST_ARC_ALPHA = 0x99

private const val ARC_HEIGHT_COMPRESSION = 0.875f

class SunTimesView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    View(context, attrs, defStyle) {

    private var _sunIntrinsicWidth: Int = 0
    private var _sunIntrinsicHeight: Int = 0

    @ColorInt
    private var primaryColor: Int = Color.RED

    @ColorInt
    private var arcBackgroundColor: Int = Color.BLUE
    private var arcGroundEdgeMargin: Float = 0f
    private var arcThickness: Float = 0f
        set(value) {
            arcThicknessHalf = value / 2
            field = value
        }
    private var arcThicknessHalf: Float = 0f
    private var arcRectangle: RectF = RectF()
    private var arcCenterX: Int = 0
    private var arcCenterY: Float = 0f
    private var arcRadius: Float = 0f
    private var arcHeight: Float = 0f

    @ColorInt
    private var timeColor: Int = Color.GRAY

    private var timeTextSize: Float = 12f
    private var timeTextTopMargin: Int = 8
    private var textHeight: Float = 0f

    private var sun: Drawable = ColorDrawable()
        set(value) {
            _sunIntrinsicWidth = value.intrinsicWidth
            _sunIntrinsicHeight = value.intrinsicHeight

            var wrappedDrawable = value
            wrappedDrawable.setTint(primaryColor)
            field = wrappedDrawable
        }
    private var sunSize: Int = 0
    private var sunRect: Rect = Rect()

    private lateinit var pastArcPaint: Paint
    private lateinit var futureArcPaint: Paint
    private lateinit var groundPaint: Paint
    private lateinit var timePaint: TextPaint

    private var sunriseTime: String? = null
    private var sunsetTime: String? = null
    private var sunPastAngle: Float = 0f
        set(value) {
            sunFutureAngle = SWEEP_DISTANCE - value
            sunFutureArcStartAngle = ARC_START_ANGLE + value
            field = value
        }
    private var sunFutureAngle: Float = 0f
    private var sunFutureArcStartAngle: Float = 0f


    init {
        init(attrs, defStyle)

        if (isInEditMode) {
            setSunTimes(LocalDateTime.now().minusHours(4), LocalDateTime.now().plusHours(4), "12:00", "20:00")
        }
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        getAttributes(attrs, defStyle)

        initPaints()
    }

    private fun getAttributes(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SunTimesView, defStyle, 0)

        primaryColor = a.getColor(R.styleable.SunTimesView_st_primaryColor, primaryColor)

        arcBackgroundColor = a.getColor(R.styleable.SunTimesView_st_arcBackgroundColor, arcBackgroundColor)
        arcThickness = a.getDimension(R.styleable.SunTimesView_st_arcThickness,
            resources.getDimension(R.dimen.small_100))
        arcGroundEdgeMargin = a.getDimension(R.styleable.SunTimesView_st_arcEdgeGroundMargin,
            resources.getDimension(R.dimen.medium_100))

        timeColor = a.getColor(R.styleable.SunTimesView_st_timeTextColor, timeColor)
        timeTextSize = a.getDimension(R.styleable.SunTimesView_st_timeTextSize,
            resources.getDimension(R.dimen.text_size_12))
        timeTextTopMargin = a.getDimensionPixelSize(R.styleable.SunTimesView_st_timeTextTopMargin,
            resources.getDimensionPixelSize(R.dimen.small_100))

        sun = a.getDrawableOrThrow(R.styleable.SunTimesView_st_sunIcon)
        sun?.callback = this
        sunSize = a.getDimensionPixelSize(R.styleable.SunTimesView_st_sunSize,
            resources.getDimensionPixelSize(R.dimen.medium_150))
        sunriseTime = a.getString(R.styleable.SunTimesView_st_sunriseText)
        sunsetTime = a.getString(R.styleable.SunTimesView_st_sunsetText)

        a.recycle()
    }

    private fun initPaints() {
        pastArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            val primaryColorAlpha = primaryColor.alpha
            val alphaDifference = primaryColorAlpha - PAST_ARC_ALPHA
            val transparentPrimaryColor = primaryColor - (alphaDifference shl RGB_BITS_COUNT)

            color = transparentPrimaryColor
            strokeWidth = arcThickness
            style = Paint.Style.STROKE
        }
        futureArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = arcBackgroundColor
            strokeWidth = arcThickness
            style = Paint.Style.STROKE
        }

        groundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = arcBackgroundColor
            strokeWidth = resources.getDimension(R.dimen.divider_thickness)
            style = Paint.Style.STROKE
        }

        timePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = timeTextSize
            color = timeColor
            textAlign = Paint.Align.CENTER
            density = resources.getDisplayMetrics().density
        }
        recalculateTextMeasurements()
    }

    private fun recalculateTextMeasurements() {
        textHeight = timePaint.fontMetrics.ascent.absoluteValue - timePaint.fontMetrics.descent
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val resolvedWidth = resolveSize(0, widthMeasureSpec)
        val contentWidth = resolvedWidth - paddingLeft - paddingRight

        arcCenterX = resolvedWidth shr 1
        arcRadius = (contentWidth shr 1).toFloat() - arcGroundEdgeMargin
        arcHeight = arcRadius * ARC_HEIGHT_COMPRESSION

        val arcTopMargin = (sunSize shr 1).coerceAtLeast(arcThickness.toInt())
        val textSpace = timeTextTopMargin + textHeight
        val contentHeight = arcTopMargin + arcHeight + textSpace
        val newHeight = contentHeight + paddingTop + paddingBottom

        val contentHeightWithoutTextSpace = contentHeight - textSpace
        arcCenterY = paddingTop + contentHeightWithoutTextSpace
        arcRectangle.set(
            arcGroundEdgeMargin + arcThicknessHalf + paddingLeft,
            (arcTopMargin + paddingTop).toFloat(),
            resolvedWidth - arcGroundEdgeMargin - arcThicknessHalf - paddingRight,
            arcTopMargin + arcHeight * 2 + paddingTop
        )

        val resolvedHeight = resolveSize(newHeight.toInt(), heightMeasureSpec)

        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val width = width

        val rightEnd = (width - paddingRight).toFloat()

        drawArc(canvas)
        drawGround(canvas, paddingLeft.toFloat(), rightEnd)
        drawSun(canvas)
        drawTime(canvas, rightEnd - arcGroundEdgeMargin - arcThicknessHalf)
    }

    private fun drawArc(canvas: Canvas) {
        canvas.drawArc(arcRectangle, ARC_START_ANGLE, SWEEP_DISTANCE, false, pastArcPaint)
        canvas.drawArc(arcRectangle, sunFutureArcStartAngle, sunFutureAngle, false, futureArcPaint)
    }

    fun drawGround(canvas: Canvas, startX: Float, endX: Float) {
        canvas.drawLine(startX, arcCenterY, endX, arcCenterY, groundPaint)
    }

    private fun drawSun(canvas: Canvas) {
        if (!sunRect.isEmpty) {
            sun.draw(canvas)
        }
    }

    private fun drawTime(canvas: Canvas, rightArcX: Float) {
        val timeTextY = arcCenterY + timeTextTopMargin + textHeight / 2
        sunriseTime?.let {
            canvas.drawText(it, paddingLeft + arcGroundEdgeMargin + arcThicknessHalf, timeTextY, timePaint)
        }
        sunsetTime?.let {
            canvas.drawText(it, rightArcX, timeTextY, timePaint)
        }
    }


    fun setSunTimes(
        sunriseTime: LocalDateTime,
        sunsetTime: LocalDateTime,
        sunriseFormatted: String,
        sunsetFormatted: String,
    ) {
        calculateSunAngle(sunriseTime, sunsetTime)

        this.sunriseTime = sunriseFormatted
        this.sunsetTime = sunsetFormatted

        recalculateSunRect()

        invalidate()
    }

    private fun calculateSunAngle(sunriseTime: LocalDateTime, sunsetTime: LocalDateTime) {
        val nowSeconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
        val sunriseSeconds = sunriseTime.atZone(ZoneId.systemDefault()).toEpochSecond()
        val sunsetSeconds = sunsetTime.atZone(ZoneId.systemDefault()).toEpochSecond()
        val percentOfDayPast = ((nowSeconds - sunriseSeconds).toFloat() / (sunsetSeconds - sunriseSeconds))
            .coerceIn(0f, 1f)
        sunPastAngle = SWEEP_START_ANGLE + SWEEP_DISTANCE * percentOfDayPast
    }

    private fun recalculateSunRect() {
        val (width, height) = ViewUtils.resizeToTargetSize(_sunIntrinsicWidth, _sunIntrinsicHeight, sunSize)
        val widthHalf = width shr 1
        val heightHalf = height shr 1

        val sunPastAngleRadians = Math.toRadians(sunPastAngle.toDouble()).toFloat()
        val sunPositionCenter = Point(
            (arcCenterX - cos(sunPastAngleRadians) * (arcRadius - arcThicknessHalf)).toInt(),
            (arcCenterY - sin(sunPastAngleRadians) * arcHeight).toInt()
        )

        sunRect.set(
            sunPositionCenter.x - widthHalf,
            sunPositionCenter.y - heightHalf,
            sunPositionCenter.x + widthHalf,
            sunPositionCenter.y + heightHalf,
        )
        sun.setBounds(sunRect)
    }
}