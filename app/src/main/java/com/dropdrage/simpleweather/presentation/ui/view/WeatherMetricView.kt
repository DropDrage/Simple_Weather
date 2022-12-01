package com.dropdrage.simpleweather.presentation.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.dropdrage.simpleweather.R
import kotlin.math.absoluteValue

/**
 * Better than [android.view.TextView.drawableLeft] bcz you can change size of the icon.
 */
class WeatherMetricView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    View(context, attrs, defStyle) {

    private var _text: String? = null // TODO: use a default from R.string...
    private var _textColor: Int = Color.BLACK // TODO: use a default from R.color...
    private var _textSize: Float = 0f // TODO: use a default from R.dimen...

    private lateinit var textPaint: TextPaint
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f


    private var _iconSize: Int = 0
    private val iconSize: Int get() = if (icon != null) _iconSize else 0
    private var _iconIntrinsicWidth: Int = 0
    private var _iconIntrinsicHeight: Int = 0

    private var _textIconMargin: Int = 0
    private val textIconMargin: Int get() = if (icon != null) _textIconMargin else 0

    var icon: Drawable? = null
        set(value) {
            if (value != null) {
                _iconIntrinsicWidth = value.intrinsicWidth
                _iconIntrinsicHeight = value.intrinsicHeight

                var wrappedDrawable = value
                wrappedDrawable.setTint(iconColor)
                field = wrappedDrawable
            } else {
                _iconIntrinsicWidth = 0
                _iconIntrinsicHeight = 0
                field = null
            }
        }

    @ColorInt
    var iconColor: Int = Color.BLACK
        set(value) {
            icon?.setTint(value)
        }

    var text: String?
        get() = _text
        set(value) {
            _text = value
            invalidateTextPaintAndMeasurements()
            requestLayout()
        }

    var textSize: Float
        get() = _textSize
        set(value) {
            _textSize = value
            invalidateTextPaintAndMeasurements()
            requestLayout()
        }

    var textColor: Int
        get() = _textColor
        set(value) {
            _textColor = value
            invalidateTextPaintAndMeasurements()
        }


    init {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.WeatherMetricView, defStyle, 0)

        _text = a.getString(R.styleable.WeatherMetricView_wm_text)
        _textColor = a.getColor(R.styleable.WeatherMetricView_wm_textColor, textColor)
        _textSize = a.getDimension(R.styleable.WeatherMetricView_wm_textSize,
            resources.getDimension(R.dimen.text_size_100))

        if (a.hasValue(R.styleable.WeatherMetricView_wm_icon)) {
            icon = a.getDrawable(R.styleable.WeatherMetricView_wm_icon)
            icon?.callback = this

            _iconSize = a.getDimensionPixelSize(R.styleable.WeatherMetricView_wm_iconSize, _iconIntrinsicWidth)
            iconColor = a.getColor(R.styleable.WeatherMetricView_wm_iconColor, Color.BLACK)

            _textIconMargin = a.getDimensionPixelSize(R.styleable.WeatherMetricView_wm_textIconMargin,
                resources.getDimensionPixelSize(R.dimen.small_100))
        }

        a.recycle()

        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        textPaint.let {
            it.textSize = textSize
            it.color = textColor

            textWidth = it.measureText(text.orEmpty())
            textHeight = it.fontMetrics.ascent.absoluteValue - it.fontMetrics.descent
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val newWidth = iconSize + textIconMargin + textWidth.toInt() + paddingLeft + paddingRight
        val newHeight = iconSize.coerceAtLeast(textHeight.toInt()) + paddingTop + paddingBottom

        val resolvedWidth = resolveSize(newWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(newHeight, heightMeasureSpec)

        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom

        val contentHeight = height - paddingTop - paddingBottom

        icon?.apply {
            val scaleFactor: Float =
                if (_iconIntrinsicWidth > _iconIntrinsicHeight) iconSize / (_iconIntrinsicWidth).toFloat()
                else iconSize / (_iconIntrinsicHeight).toFloat()

            val width: Int = (_iconIntrinsicWidth * scaleFactor).toInt()
            val height: Int = (_iconIntrinsicHeight * scaleFactor).toInt()

            val topOffset: Int = paddingTop + (contentHeight - height) shr 1
            val leftOffset: Int = paddingLeft + (iconSize - width) shr 1
            setBounds(leftOffset, topOffset, leftOffset + width, topOffset + height)
            draw(canvas)
        }


        text?.let {
            canvas.drawText(it,
                (paddingLeft + iconSize + textIconMargin).toFloat(),
                (paddingTop + (contentHeight + textHeight) / 2),
                textPaint)
        }
    }
}