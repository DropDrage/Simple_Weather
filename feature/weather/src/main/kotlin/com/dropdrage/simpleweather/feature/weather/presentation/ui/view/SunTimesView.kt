package com.dropdrage.simpleweather.feature.weather.presentation.ui.view

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
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.alpha
import com.dropdrage.common.presentation.utils.CommonDimen
import com.dropdrage.simpleweather.feature.weather.R
import com.dropdrage.simpleweather.feature.weather.presentation.util.ViewUtils
import com.dropdrage.simpleweather.feature.weather.presentation.util.extension.calculateTextHeight
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

internal class SunTimesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : View(context, attrs, defStyle) {

    private var _sunIntrinsicWidth: Int = 0
    private var _sunIntrinsicHeight: Int = 0

    @ColorInt
    private var primaryColor: Int = Color.RED

    @ColorInt
    private var arcBackgroundColor: Int = Color.BLUE
    private var arcGroundEdgeMargin: Float = 0f
    private var arcThickness: Float = 0f
        set(value) {
            arcThicknessHalf = value * 0.5f
            field = value
        }
    private var arcThicknessHalf: Float = 0f
    private var arcTopMargin: Int = 0
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
    private var textLeftX: Float = 0f
    private var textRightX: Float = 0f
    private var timeTextY: Float = 0f

    private var sun: Drawable = ColorDrawable()
        set(value) {
            _sunIntrinsicWidth = value.intrinsicWidth
            _sunIntrinsicHeight = value.intrinsicHeight

            value.setTint(primaryColor)
            field = value
        }
    private var sunSize: Int = 0
    private val sunRect: Rect = Rect()

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

    private var leftBorder: Float = 0f
    private var rightBorder: Float = 0f


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
        context.withStyledAttributes(attrs, R.styleable.SunTimesView, defStyle, 0) {
            primaryColor = getColor(R.styleable.SunTimesView_st_primaryColor, primaryColor)

            arcBackgroundColor = getColor(R.styleable.SunTimesView_st_arcBackgroundColor, arcBackgroundColor)
            arcThickness = getDimension(
                R.styleable.SunTimesView_st_arcThickness,
                resources.getDimension(CommonDimen.small_100),
            )
            arcGroundEdgeMargin = getDimension(
                R.styleable.SunTimesView_st_arcEdgeGroundMargin,
                resources.getDimension(CommonDimen.medium_100),
            )

            timeColor = getColor(R.styleable.SunTimesView_st_timeTextColor, timeColor)
            timeTextSize = getDimension(
                R.styleable.SunTimesView_st_timeTextSize,
                resources.getDimension(CommonDimen.text_size_12),
            )
            timeTextTopMargin = getDimensionPixelSize(
                R.styleable.SunTimesView_st_timeTextTopMargin,
                resources.getDimensionPixelSize(CommonDimen.small_100),
            )

            sun = getDrawableOrThrow(R.styleable.SunTimesView_st_sunIcon)
            sun.callback = this@SunTimesView
            sunSize = getDimensionPixelSize(
                R.styleable.SunTimesView_st_sunSize,
                resources.getDimensionPixelSize(CommonDimen.medium_150),
            )
            sunriseTime = getString(R.styleable.SunTimesView_st_sunriseText)
            sunsetTime = getString(R.styleable.SunTimesView_st_sunsetText)

            arcTopMargin = (sunSize shr 1).coerceAtLeast(arcThickness.toInt())
        }
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

            density = resources.displayMetrics.density
            textHeight = calculateTextHeight()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val resolvedWidth = resolveSize(0, widthMeasureSpec)
        val contentWidth = resolvedWidth - paddingLeft - paddingRight
        val contentWidthHalf = contentWidth shr 1

        arcRadius = contentWidthHalf - arcGroundEdgeMargin
        arcHeight = arcRadius * ARC_HEIGHT_COMPRESSION
        arcCenterX = contentWidthHalf + paddingLeft

        val paddingTop = paddingTop
        val textSpace = timeTextTopMargin + textHeight
        val contentHeight = arcTopMargin + arcHeight + textSpace
        val newHeight = contentHeight + paddingTop + paddingBottom

        val contentHeightWithoutTextSpace = contentHeight - textSpace
        arcCenterY = contentHeightWithoutTextSpace + paddingTop

        val resolvedHeight = resolveSize(newHeight.toInt(), heightMeasureSpec)

        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        if (width == oldw && height == oldh) return

        leftBorder = paddingLeft.toFloat()
        rightBorder = (width - paddingRight).toFloat()

        val arcTopMarginWithPaddingTop = arcTopMargin + paddingTop
        arcRectangle.set(
            leftBorder + arcGroundEdgeMargin + arcThicknessHalf,
            arcTopMarginWithPaddingTop.toFloat(),
            rightBorder - arcGroundEdgeMargin - arcThicknessHalf,
            arcTopMarginWithPaddingTop + arcHeight * 2,
        )

        textLeftX = arcRectangle.left
        textRightX = arcRectangle.right
        timeTextY = arcCenterY + timeTextTopMargin + textHeight * 0.5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawArc(canvas)
        drawGround(canvas)
        drawSun(canvas)
        drawTime(canvas)
    }

    private fun drawArc(canvas: Canvas) {
        canvas.drawArc(arcRectangle, ARC_START_ANGLE, SWEEP_DISTANCE, false, pastArcPaint)
        canvas.drawArc(arcRectangle, sunFutureArcStartAngle, sunFutureAngle, false, futureArcPaint)
    }

    private fun drawGround(canvas: Canvas) {
        canvas.drawLine(leftBorder, arcCenterY, rightBorder, arcCenterY, groundPaint)
    }

    private fun drawSun(canvas: Canvas) {
        if (!sunRect.isEmpty) {
            sun.draw(canvas)
        }
    }

    private fun drawTime(canvas: Canvas) {
        sunriseTime?.let {
            canvas.drawText(it, textLeftX, timeTextY, timePaint)
        }
        sunsetTime?.let {
            canvas.drawText(it, textRightX, timeTextY, timePaint)
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
        val defaultZone = ZoneId.systemDefault()
        val nowSeconds = LocalDateTime.now().atZone(defaultZone).toEpochSecond()
        val sunriseSeconds = sunriseTime.atZone(defaultZone).toEpochSecond()
        val sunsetSeconds = sunsetTime.atZone(defaultZone).toEpochSecond()

        val percentOfDayPast = ((nowSeconds - sunriseSeconds).toFloat() / (sunsetSeconds - sunriseSeconds))
            .coerceIn(0f, 1f)
        sunPastAngle = SWEEP_START_ANGLE + SWEEP_DISTANCE * percentOfDayPast
    }

    private fun recalculateSunRect() {
        val sunPastAngleRadians = Math.toRadians(sunPastAngle.toDouble()).toFloat()
        val sunPositionCenter = Point(
            (arcCenterX - cos(sunPastAngleRadians) * (arcRadius - arcThicknessHalf)).toInt(),
            (arcCenterY - sin(sunPastAngleRadians) * arcHeight).toInt(),
        )

        val (width, height) = ViewUtils.resizeToTargetSize(_sunIntrinsicWidth, _sunIntrinsicHeight, sunSize)
        val widthHalf = width shr 1
        val heightHalf = height shr 1

        sunRect.set(
            sunPositionCenter.x - widthHalf,
            sunPositionCenter.y - heightHalf,
            sunPositionCenter.x + widthHalf,
            sunPositionCenter.y + heightHalf,
        )
        sun.bounds = sunRect
    }


    companion object {
        private const val SWEEP_START_ANGLE = 0
        private const val SWEEP_END_ANGLE = 180
        private val SWEEP_DISTANCE = (SWEEP_END_ANGLE - SWEEP_START_ANGLE).absoluteValue.toFloat()

        private const val ARC_START_ANGLE = 180f

        private const val RGB_BITS_COUNT = 8 * 3
        private const val PAST_ARC_ALPHA = 0x99

        private const val ARC_HEIGHT_COMPRESSION = 0.875f
    }

}
