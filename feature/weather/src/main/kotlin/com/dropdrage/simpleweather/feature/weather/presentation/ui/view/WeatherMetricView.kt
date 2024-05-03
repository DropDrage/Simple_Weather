package com.dropdrage.simpleweather.feature.weather.presentation.ui.view

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.fonts.FontStyle
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.content.res.use
import com.dropdrage.common.domain.Range
import com.dropdrage.common.presentation.utils.CommonDimen
import com.dropdrage.simpleweather.feature.weather.R
import com.dropdrage.simpleweather.feature.weather.presentation.util.ViewUtils
import com.dropdrage.simpleweather.feature.weather.presentation.util.extension.calculateTextHeight
import kotlin.math.max

internal class WeatherMetricView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) :
    View(context, attrs, defStyle) {

    private var _textColor: Int = Color.BLACK
    private var _textSize: Float = 0f
    private var fontWeight: Int = 600
    private var textStartDrawX: Float = 0f

    private var topText: String? = null
    private var topTextX: Float = 0f
    private var topTextY: Float = 0f
    private var bottomText: String? = null
    private var bottomTextX: Float = 0f
    private var bottomTextY: Float = 0f

    private lateinit var textPaint: TextPaint
    private var maxTextWidth: Float = 0f
    private var topTextWidth: Float = 0f
    private var bottomTextWidth: Float = 0f
    private var textHeight: Float = 0f

    private var _iconSize: Int = 0
    private val iconSize: Int get() = if (icon != null) _iconSize else 0
    private var _iconIntrinsicWidth: Int = 0
    private var _iconIntrinsicHeight: Int = 0
    private var iconRect: Rect = Rect()

    private lateinit var dividerPaint: Paint
    private var dividerThickness: Float = 0f
        set(value) {
            dividerThicknessHalf = value * 0.5f
            field = value
        }
    private var dividerThicknessHalf: Float = 0f
    private var dividerEndX: Float = 0f

    private var _textIconMargin: Int = 0
    private val textIconMargin: Int get() = if (icon != null) _textIconMargin else 0
    private var _textDividerMargin: Int = 0

    private var isOnlyTopText: Boolean = false

    @RequiresApi(Build.VERSION_CODES.Q)
    private var fontWeightAdjustment: Int = 0
    private var originalTypeface: Typeface? = null

    private var centerY: Float = 0f

    var icon: Drawable? = null
        set(value) {
            if (value != null) {
                _iconIntrinsicWidth = value.intrinsicWidth
                _iconIntrinsicHeight = value.intrinsicHeight

                value.setTint(iconColor)
                field = value
                recalculateIconRect()
            } else {
                _iconIntrinsicWidth = 0
                _iconIntrinsicHeight = 0
                field = null
            }

            invalidate()
        }

    @ColorInt
    var iconColor: Int = Color.BLACK
        set(value) {
            icon?.setTint(value)
        }

    var textSize: Float
        get() = _textSize
        set(value) {
            _textSize = value
            invalidateTextPaintAndMeasurements()
            requestLayout()
        }


    init {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        getAttributes(attrs, defStyle)

        initTextPaint()
        initDividerPaint()
    }

    private fun getAttributes(attrs: AttributeSet?, defStyle: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.WeatherMetricView, defStyle, 0).use { a ->
            val topText = a.getString(R.styleable.WeatherMetricView_wm_topText).orEmpty()
            val bottomText = a.getString(R.styleable.WeatherMetricView_wm_bottomText)
            _textColor = a.getColor(R.styleable.WeatherMetricView_wm_textColor, _textColor)
            _textSize = a.getDimension(
                R.styleable.WeatherMetricView_wm_textSize,
                resources.getDimension(CommonDimen.text_size_16)
            )
            fontWeight = a.getInt(R.styleable.WeatherMetricView_wm_fontWeight, R.integer.font_weight_600)

            if (a.hasValue(R.styleable.WeatherMetricView_wm_icon)) {
                icon = a.getDrawable(R.styleable.WeatherMetricView_wm_icon)
                icon?.callback = this

                _iconSize = a.getDimensionPixelSize(R.styleable.WeatherMetricView_wm_iconSize, _iconIntrinsicWidth)
                iconColor = a.getColor(R.styleable.WeatherMetricView_wm_iconColor, Color.BLACK)

                _textIconMargin = a.getDimensionPixelSize(
                    R.styleable.WeatherMetricView_wm_textIconMargin,
                    resources.getDimensionPixelSize(CommonDimen.small_100)
                )
            }
            dividerThickness = a.getDimension(
                R.styleable.WeatherMetricView_wm_dividerThickness,
                resources.getDimension(R.dimen.divider_thickness)
            )

            _textDividerMargin = a.getDimensionPixelSize(
                R.styleable.WeatherMetricView_wm_textDividerMargin,
                resources.getDimensionPixelSize(CommonDimen.small_50)
            )

            setTexts(topText, bottomText)
        }
    }

    private fun initTextPaint() {
        textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.LEFT
            density = resources.displayMetrics.density
        }

        setSansSerifTypeface(getSemiBoldFontFamilyIfWeightUnsupported(), fontWeight)

        invalidateTextPaintAndMeasurements()
    }

    private fun getSemiBoldFontFamilyIfWeightUnsupported() =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) "sans-serif-medium"
        else null

    //region Typeface

    private fun setSansSerifTypeface(
        familyName: String?, @androidx.annotation.IntRange(from = -1, to = 1000) weight: Int,
    ) {
        val sansSerifTypeface = Typeface.SANS_SERIF

        if (sansSerifTypeface == null && familyName != null) {
            val normalTypeface = Typeface.create(familyName, Typeface.NORMAL)
            resolveStyleAndSetTypeface(normalTypeface, weight)
        } else {
            resolveStyleAndSetTypeface(sansSerifTypeface, weight)
        }
    }

    private fun resolveStyleAndSetTypeface(
        typeface: Typeface?, @androidx.annotation.IntRange(from = -1, to = 1000) weight: Int,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && weight >= 0) {
            val correctWeight = weight.coerceAtMost(FontStyle.FONT_WEIGHT_MAX)
            setTypeface(Typeface.create(typeface, correctWeight, false))
        } else {
            setNormalTypeface(typeface)
        }
    }

    private fun setTypeface(typeface: Typeface?) {
        var newTypeface = typeface
        originalTypeface = newTypeface

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (fontWeightAdjustment != 0 && fontWeightAdjustment != Configuration.FONT_WEIGHT_ADJUSTMENT_UNDEFINED) {
                newTypeface =
                    if (newTypeface == null) Typeface.DEFAULT
                    else {
                        val newWeight = (newTypeface.weight + fontWeightAdjustment).coerceIn(FONT_WEIGHT_RANGE)
                        Typeface.create(newTypeface, newWeight, false)
                    }
            }
        }

        if (textPaint.typeface !== newTypeface) {
            textPaint.typeface = newTypeface
        }
    }

    private fun setNormalTypeface(tf: Typeface?) {
        textPaint.setFakeBoldText(false)
        textPaint.setTextSkewX(0f)
        setTypeface(tf)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (fontWeightAdjustment != newConfig.fontWeightAdjustment) {
                fontWeightAdjustment = newConfig.fontWeightAdjustment
                setTypeface(originalTypeface)
            }
        }
    }

    //endregion

    private fun invalidateTextPaintAndMeasurements() {
        textPaint.let {
            it.textSize = textSize
            it.color = _textColor

            it.density = resources.displayMetrics.density

            topTextWidth = it.measureText(topText.orEmpty())
            bottomTextWidth = it.measureText(bottomText.orEmpty())
            maxTextWidth = max(topTextWidth, bottomTextWidth)
            textHeight = it.calculateTextHeight()
        }
    }

    private fun initDividerPaint() {
        dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = _textColor
            strokeWidth = dividerThickness
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val textsBlockHeight = textHeight + textHeight + _textDividerMargin + _textDividerMargin + dividerThickness
        val contentHeight = max(iconSize, textsBlockHeight.toInt())
        val newHeight = contentHeight + paddingTop + paddingBottom

        val resolvedWidth = resolveSize(0, widthMeasureSpec)
        val resolvedHeight = resolveSize(newHeight, heightMeasureSpec)

        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        recalculateTextMeasurements(width)

        val paddingTop = paddingTop
        val resolvedContentHeight = height - paddingTop - paddingBottom
        centerY = ((resolvedContentHeight shr 1) + paddingTop).toFloat()

        val dividerMargin = _textDividerMargin + dividerThicknessHalf
        bottomTextY = centerY + textHeight + dividerMargin

        recalculateIconRect(resolvedContentHeight, paddingLeft, paddingTop)
    }

    private fun recalculateTextMeasurements(width: Int = getWidth()) {
        val paddingLeft = paddingLeft
        val contentWidth = width - paddingLeft - paddingRight
        val textIconSpace = (contentWidth - iconSize - maxTextWidth).coerceAtLeast(textIconMargin.toFloat())
        textStartDrawX = paddingLeft + iconSize + textIconSpace

        if (isOnlyTopText) {
            topTextX = textStartDrawX
            topTextY = centerY + textHeight * 0.5f
        } else {
            topTextX = textStartDrawX + (maxTextWidth - topTextWidth) * 0.5f
            val dividerMargin = _textDividerMargin + dividerThicknessHalf
            topTextY = centerY - dividerMargin
        }

        dividerEndX = textStartDrawX + maxTextWidth

        bottomTextX = textStartDrawX + (maxTextWidth - bottomTextWidth) * 0.5f
    }

    private fun recalculateIconRect() {
        val paddingTop = paddingTop
        recalculateIconRect(
            height - paddingTop - paddingBottom,
            paddingLeft,
            paddingTop
        )
    }

    private fun recalculateIconRect(contentHeight: Int, paddingLeft: Int, paddingTop: Int) {
        val (width, height) = ViewUtils.resizeToTargetSize(_iconIntrinsicWidth, _iconIntrinsicHeight, iconSize)

        val topOffset = paddingTop + ((contentHeight - height) shr 1)
        val leftOffset = paddingLeft + ((iconSize - width) shr 1)

        iconRect.set(leftOffset, topOffset, leftOffset + width, topOffset + height)
        icon?.bounds = iconRect
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawIcon(canvas)

        drawTopText(canvas)
        drawBottomPartIfNeeded(canvas)
    }

    private fun drawIcon(canvas: Canvas) {
        icon?.draw(canvas)
    }

    private fun drawTopText(canvas: Canvas) {
        topText?.let {
            canvas.drawText(it, topTextX, topTextY, textPaint)
        }
    }

    private fun drawBottomPartIfNeeded(canvas: Canvas) {
        if (!isOnlyTopText) {
            drawDivider(canvas)
            drawBottomText(canvas)
        }
    }

    private fun drawDivider(canvas: Canvas) {
        canvas.drawLine(textStartDrawX, centerY, dividerEndX, centerY, dividerPaint)
    }

    private fun drawBottomText(canvas: Canvas) {
        bottomText?.let {
            canvas.drawText(it, bottomTextX, bottomTextY, textPaint)
        }
    }


    fun setTextEndOnTop(texts: Range<String>) {
        setText(texts.end, texts.start)
    }

    fun setText(topText: String, bottomText: String? = null) {
        if (this.topText == topText && this.bottomText == bottomText) {
            return
        }

        setTexts(topText, bottomText)

        invalidateTextPaintAndMeasurements()
        recalculateTextMeasurements()
        invalidate()
    }

    private fun setTexts(topText: String, bottomText: String?) {
        this.topText = topText
        this.bottomText = bottomText
        isOnlyTopText = bottomText.isNullOrEmpty()
    }


    companion object {
        @RequiresApi(Build.VERSION_CODES.Q)
        private val FONT_WEIGHT_RANGE = FontStyle.FONT_WEIGHT_MIN..FontStyle.FONT_WEIGHT_MAX
    }

}
