package com.dropdrage.simpleweather.presentation.ui.view

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.fonts.FontStyle
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.domain.util.Range
import kotlin.math.absoluteValue
import kotlin.math.max

@IntDef(value = [DEFAULT_TYPEFACE, SANS, SERIF, MONOSPACE])
@Retention(AnnotationRetention.SOURCE)
annotation class TypefaceInt

private const val DEFAULT_TYPEFACE = -1
private const val SANS = 1
private const val SERIF = 2
private const val MONOSPACE = 3

@RequiresApi(Build.VERSION_CODES.Q)
private val FONT_WEIGHT_RANGE = FontStyle.FONT_WEIGHT_MIN..FontStyle.FONT_WEIGHT_MAX

class WeatherMetricView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    View(context, attrs, defStyle) {

    private var _textColor: Int = Color.BLACK // TODO: use a default from R.color...
    private var _textSize: Float = 0f
    private var fontWeight: Int = 600

    private var topText: String? = null // TODO: use a default from R.string...
    private var bottomText: String? = null // TODO: use a default from R.string...

    private lateinit var textPaint: TextPaint
    private var maxTextWidth: Float = 0f
    private var topTextWidth: Float = 0f
    private var bottomTextWidth: Float = 0f
    private var textHeight: Float = 0f

    private var _iconSize: Int = 0
    private val iconSize: Int get() = if (icon != null) _iconSize else 0
    private var _iconIntrinsicWidth: Int = 0
    private var _iconIntrinsicHeight: Int = 0

    private lateinit var dividerPaint: Paint
    private var dividerThickness: Float = 0f

    private var _textIconMargin: Int = 0
    private val textIconMargin: Int get() = if (icon != null) _textIconMargin else 0
    private var _textDividerMargin: Int = 0
    private val textDividerMargin: Int get() = if (bottomText.isNullOrEmpty()) 0 else _textDividerMargin

    private var isOnlyTopText: Boolean = false

    @RequiresApi(Build.VERSION_CODES.Q)
    private var fontWeightAdjustment: Int = 0
    private var originalTypeface: Typeface? = null


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
        getAttributes(attrs, defStyle)

        initTextPaint()
        initDividerPaint()
    }

    private fun getAttributes(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.WeatherMetricView, defStyle, 0)

        val topText = a.getString(R.styleable.WeatherMetricView_wm_topText).orEmpty()
        val bottomText = a.getString(R.styleable.WeatherMetricView_wm_bottomText)
        _textColor = a.getColor(R.styleable.WeatherMetricView_wm_textColor, textColor)
        _textSize = a.getDimension(R.styleable.WeatherMetricView_wm_textSize,
            resources.getDimension(R.dimen.text_size_16))
        fontWeight = a.getInt(R.styleable.WeatherMetricView_wm_fontWeight, R.integer.font_weight_600)

        if (a.hasValue(R.styleable.WeatherMetricView_wm_icon)) {
            icon = a.getDrawable(R.styleable.WeatherMetricView_wm_icon)
            icon?.callback = this

            _iconSize = a.getDimensionPixelSize(R.styleable.WeatherMetricView_wm_iconSize, _iconIntrinsicWidth)
            iconColor = a.getColor(R.styleable.WeatherMetricView_wm_iconColor, Color.BLACK)

            _textIconMargin = a.getDimensionPixelSize(R.styleable.WeatherMetricView_wm_textIconMargin,
                resources.getDimensionPixelSize(R.dimen.small_100))
        }
        dividerThickness = a.getDimension(R.styleable.WeatherMetricView_wm_dividerThickness,
            resources.getDimension(R.dimen.divider_width))

        _textDividerMargin = a.getDimensionPixelSize(R.styleable.WeatherMetricView_wm_textDividerMargin,
            resources.getDimensionPixelSize(R.dimen.small_50))

        a.recycle()

        setTexts(topText, bottomText)
    }

    private fun initTextPaint() {
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
            density = resources.getDisplayMetrics().density
        }

        setTypefaceFromAttrs(Typeface.SANS_SERIF,
            getSemiBoldFontFamilyIfWeightUnsupported(),
            SANS,
            Typeface.NORMAL,
            fontWeight
        )

        invalidateTextPaintAndMeasurements()
    }

    private fun getSemiBoldFontFamilyIfWeightUnsupported() =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) "sans-serif-medium"
        else null

    //region Typeface

    private fun setTypefaceFromAttrs(
        typeface: Typeface?, familyName: String?,
        @TypefaceInt typefaceIndex: Int, style: Int,
        @androidx.annotation.IntRange(from = -1, to = 1000) weight: Int,
    ) {
        if (typeface == null && familyName != null) {
            // Lookup normal Typeface from system font map.
            val normalTypeface = Typeface.create(familyName, Typeface.NORMAL)
            resolveStyleAndSetTypeface(normalTypeface, style, weight)
        } else if (typeface != null) {
            resolveStyleAndSetTypeface(typeface, style, weight)
        } else {  // both typeface and familyName is null.
            when (typefaceIndex) {
                SANS -> resolveStyleAndSetTypeface(Typeface.SANS_SERIF, style, weight)
                SERIF -> resolveStyleAndSetTypeface(Typeface.SERIF, style, weight)
                MONOSPACE -> resolveStyleAndSetTypeface(Typeface.MONOSPACE, style, weight)
                DEFAULT_TYPEFACE -> resolveStyleAndSetTypeface(null, style, weight)
                else -> resolveStyleAndSetTypeface(null, style, weight)
            }
        }
    }

    private fun resolveStyleAndSetTypeface(
        typeface: Typeface?, style: Int,
        @androidx.annotation.IntRange(from = -1, to = 1000) weight: Int,
    ) {
        if (weight >= 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val correctWeight = Math.min(FontStyle.FONT_WEIGHT_MAX, weight)
            val italic = style and Typeface.ITALIC != 0
            setTypeface(Typeface.create(typeface, correctWeight, italic))
        } else {
            setTypeface(typeface, style)
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
                        val typefaceStyle = newTypeface.style
                        val italic = typefaceStyle and Typeface.ITALIC != 0
                        Typeface.create(newTypeface, newWeight, italic)
                    }
            }
        }

        if (textPaint.getTypeface() !== newTypeface) {
            textPaint.setTypeface(newTypeface)
        }
    }

    private fun setTypeface(tf: Typeface?, style: Int) {
        if (style > 0) {
            val nonNullTypeface =
                if (tf == null) Typeface.defaultFromStyle(style)
                else Typeface.create(tf, style)
            setTypeface(nonNullTypeface)
            // now compute what (if any) algorithmic styling is needed
            val typefaceStyle = nonNullTypeface?.style ?: 0
            val need = style and typefaceStyle.inv()
            textPaint.setFakeBoldText(need and Typeface.BOLD != 0)
            textPaint.setTextSkewX(if (need and Typeface.ITALIC != 0) -0.25f else 0f)
        } else {
            textPaint.setFakeBoldText(false)
            textPaint.setTextSkewX(0f)
            setTypeface(tf)
        }
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
            it.color = textColor

            topTextWidth = it.measureText(topText.orEmpty())
            bottomTextWidth = it.measureText(bottomText.orEmpty())
            maxTextWidth = max(topTextWidth, bottomTextWidth)
            textHeight = it.fontMetrics.ascent.absoluteValue - it.fontMetrics.descent
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val newWidth = iconSize + textIconMargin + maxTextWidth.toInt() + paddingLeft + paddingRight
        val textsBlockHeight =
            if (isOnlyTopText) textHeight
            else textHeight + textHeight + textDividerMargin + textDividerMargin + dividerThickness
        val newHeight = max(iconSize, textsBlockHeight.toInt()) + paddingTop + paddingBottom

        val resolvedWidth = resolveSize(newWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(newHeight, heightMeasureSpec)

        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        drawIcon(paddingTop, contentHeight, paddingLeft, canvas)

        val textIconSpace = (contentWidth - iconSize - maxTextWidth).coerceAtLeast(textIconMargin.toFloat())

        val textStartDrawX = paddingLeft + iconSize + textIconSpace
        val dividerMargin = textDividerMargin + dividerThickness / 2
        val centerY = (paddingTop + (contentHeight shr 1)).toFloat()

        drawTopText(canvas, paddingTop, textStartDrawX, contentHeight, dividerMargin)
        drawBottomPartIfNeeded(canvas, centerY, textStartDrawX, dividerMargin)
    }

    private fun drawIcon(paddingTop: Int, contentHeight: Int, paddingLeft: Int, canvas: Canvas) {
        icon?.apply {
            val scaleFactor: Float =
                if (_iconIntrinsicWidth > _iconIntrinsicHeight) iconSize / (_iconIntrinsicWidth).toFloat()
                else iconSize / (_iconIntrinsicHeight).toFloat()

            val width: Int = (_iconIntrinsicWidth * scaleFactor).toInt()
            val height: Int = (_iconIntrinsicHeight * scaleFactor).toInt()

            val topOffset: Int = paddingTop + ((contentHeight - height) shr 1)
            val leftOffset: Int = paddingLeft + ((iconSize - width) shr 1)

            setBounds(leftOffset, topOffset, leftOffset + width, topOffset + height)
            draw(canvas)
        }
    }

    private fun drawTopText(
        canvas: Canvas,
        paddingTop: Int,
        textStartDrawX: Float,
        contentHeight: Int,
        dividerMargin: Float,
    ) {
        topText?.let {
            val drawX: Float
            val drawY: Float
            if (isOnlyTopText) {
                drawX = textStartDrawX
                drawY = paddingTop + (contentHeight + textHeight) / 2
            } else {
                drawX = textStartDrawX + (maxTextWidth - topTextWidth) / 2
                drawY = paddingTop + contentHeight / 2 - dividerMargin
            }

            canvas.drawText(it, drawX, drawY, textPaint)
        }
    }

    private fun drawBottomPartIfNeeded(canvas: Canvas, centerY: Float, textStartDrawX: Float, dividerMargin: Float) {
        if (!isOnlyTopText) {
            drawDivider(canvas, centerY, textStartDrawX)
            drawBottomText(canvas, textStartDrawX, centerY, dividerMargin)
        }
    }

    private fun drawDivider(canvas: Canvas, centerY: Float, textStartDrawX: Float) {
        canvas.drawLine(textStartDrawX, centerY, textStartDrawX + maxTextWidth, centerY, dividerPaint)
    }

    private fun drawBottomText(canvas: Canvas, textStartDrawX: Float, centerY: Float, dividerMargin: Float) {
        bottomText?.let {
            canvas.drawText(it,
                textStartDrawX + (maxTextWidth - bottomTextWidth) / 2,
                centerY + textHeight + dividerMargin,
                textPaint)
        }
    }


    fun setText(texts: Range<String>) {
        setText(texts.start, texts.end)
    }

    fun setText(topText: String, bottomText: String? = null) {
        setTexts(topText, bottomText)

        invalidateTextPaintAndMeasurements()
        requestLayout()
    }

    private fun setTexts(topText: String, bottomText: String?) {
        this.topText = topText
        this.bottomText = bottomText
        isOnlyTopText = bottomText.isNullOrEmpty()
    }
}