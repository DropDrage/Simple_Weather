package com.dropdrage.simpleweather.weather.presentation.presentation.ui.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.dropdrage.common.presentation.utils.getBitmapFromDrawable
import com.dropdrage.common.presentation.utils.toPx
import com.dropdrage.common.presentation.utils.toPxInt
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import com.dropdrage.simpleweather.core.style.Medium100
import com.dropdrage.simpleweather.core.style.Medium150
import com.dropdrage.simpleweather.core.style.Small100
import com.dropdrage.simpleweather.weather.presentation.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import kotlin.math.cos
import kotlin.math.sin

private const val ARC_START_ANGLE = 180f
private const val ARC_HEIGHT_COMPRESSION = 1f //0.875f

private const val SWEEP_DISTANCE = 180

@Composable
fun SunTimes(
    primaryColor: Color,
    arcBackgroundColor: Color, arcThickness: Dp, arcGroundEdgeMargin: Dp,
    timeTextStyle: TextStyle = MaterialTheme.typography.labelMedium, timeTextTopMargin: Dp,
    @DrawableRes sunDrawableRes: Int, sunSize: Dp,
    sunriseTime: LocalDateTime, sunsetTime: LocalDateTime,
    sunriseFormatted: String, sunsetFormatted: String,
    modifier: Modifier = Modifier,
) {
    val localDensity = LocalDensity.current

    SunTimes(
        primaryColor = primaryColor,
        arcBackgroundColor = arcBackgroundColor,
        arcThickness = arcThickness.toPx(localDensity),
        arcGroundEdgeMargin = arcGroundEdgeMargin.toPx(localDensity),
        timeTextStyle = timeTextStyle,
        timeTextTopMargin = timeTextTopMargin.toPxInt(localDensity),
        sun = LocalContext.current.getBitmapFromDrawable(sunDrawableRes).asImageBitmap(),
        sunSize = sunSize.toPxInt(localDensity),
        sunriseTime = sunriseTime,
        sunsetTime = sunsetTime,
        sunriseFormatted = sunriseFormatted,
        sunsetFormatted = sunsetFormatted,
        groundThickness = 1.dp.toPx(localDensity),
        modifier = modifier,
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun SunTimes(
    primaryColor: Color,
    arcBackgroundColor: Color, arcThickness: Float, arcGroundEdgeMargin: Float,
    timeTextStyle: TextStyle = MaterialTheme.typography.labelMedium, timeTextTopMargin: Int,
    sun: ImageBitmap, sunSize: Int,
    sunriseTime: LocalDateTime, sunsetTime: LocalDateTime,
    sunriseFormatted: String, sunsetFormatted: String,
    groundThickness: Float,
    modifier: Modifier = Modifier,
) {
    val localDensity = LocalDensity.current

    val textMeasurer = rememberTextMeasurer()

    val sunriseMeasure = textMeasurer.measure(text = AnnotatedString(sunriseFormatted), style = timeTextStyle)
    val sunsetMeasure = textMeasurer.measure(text = AnnotatedString(sunsetFormatted), style = timeTextStyle)
    var textHeight = sunriseMeasure.size.height.coerceAtLeast(sunsetMeasure.size.height)

    val transparentPrimaryColor = primaryColor.copy(alpha = 0.6f)

    val arcStroke: DrawStyle = Stroke(width = arcThickness)

    val arcTopMargin = (sun.width shr 1).coerceAtLeast(arcThickness.toInt())

    fun calculatePercentOfDayPast(): Float {
        val defaultZone = ZoneId.systemDefault()
        val nowSeconds = LocalDateTime.now().atZone(defaultZone).toEpochSecond()
        val sunriseSeconds = sunriseTime.atZone(defaultZone).toEpochSecond()
        val sunsetSeconds = sunsetTime.atZone(defaultZone).toEpochSecond()
        return ((nowSeconds - sunriseSeconds).toFloat() / (sunsetSeconds - sunriseSeconds))
            .coerceIn(0f, 1f)
    }

    val sunPastAngle = SWEEP_DISTANCE * calculatePercentOfDayPast()
    val sunFutureAngle = SWEEP_DISTANCE - sunPastAngle
    val sunFutureArcStartAngle = ARC_START_ANGLE + sunPastAngle

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val width = with(localDensity) { maxWidth.toPx() }
        val widthHalf = width / 2

        val arcRadius = widthHalf - arcGroundEdgeMargin
        val arcHeight = arcRadius * ARC_HEIGHT_COMPRESSION
        val textSpace = timeTextTopMargin + textHeight

        val height = arcTopMargin + arcHeight + textSpace

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(localDensity) { height.toDp() })
        ) {
            val arcCenterX = widthHalf
            val arcCenterY = height - textSpace

            // arc
            val arcSize = Size(arcRadius * 2, arcHeight * 2)
            val topLeft = Offset(arcGroundEdgeMargin, arcTopMargin.toFloat())
            drawArc(
                color = transparentPrimaryColor,
                startAngle = ARC_START_ANGLE,
                sweepAngle = sunPastAngle,
                useCenter = false,
                size = arcSize,
                topLeft = topLeft,
                style = arcStroke
            )
            drawArc(
                color = arcBackgroundColor,
                startAngle = sunFutureArcStartAngle,
                sweepAngle = sunFutureAngle,
                useCenter = false,
                size = arcSize,
                topLeft = topLeft,
                style = arcStroke
            )

            // ground
            var leftBorder = Offset(0f, arcCenterY)
            var rightBorder = Offset(width, arcCenterY)
            drawLine(color = arcBackgroundColor, start = leftBorder, end = rightBorder, strokeWidth = groundThickness)

            // sun
            val sunPastAngleRadians = Math.toRadians(sunPastAngle.toDouble()).toFloat()
            val sunSizeHalf = sunSize shr 1
            val sunPositionCenter = IntOffset(
                (arcCenterX - cos(sunPastAngleRadians) * arcRadius).toInt() - sunSizeHalf,
                (arcCenterY - sin(sunPastAngleRadians) * arcHeight).toInt() - sunSizeHalf
            )
            drawImage(sun, dstOffset = sunPositionCenter, dstSize = IntSize(sunSize, sunSize))

            // time
            val timeTextY = arcCenterY + timeTextTopMargin
            var textLeftOffset = Offset(arcGroundEdgeMargin - (sunriseMeasure.size.width shr 1), timeTextY)
            var textRightOffset = Offset(width - arcGroundEdgeMargin - (sunsetMeasure.size.width shr 1), timeTextY)
            drawText(
                textLayoutResult = sunriseMeasure,
                topLeft = textLeftOffset,
                color = timeTextStyle.color,
            )
            drawText(
                textLayoutResult = sunsetMeasure,
                topLeft = textRightOffset,
                color = timeTextStyle.color
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 400)
@Composable
private fun SunTimesPreview() {
    ComposeMaterial3Theme {
        Card {
            SunTimes(
                primaryColor = MaterialTheme.colorScheme.onPrimaryContainer,
                arcBackgroundColor = MaterialTheme.colorScheme.background,
                arcThickness = Small100,
                arcGroundEdgeMargin = Medium150,
                timeTextTopMargin = Small100,
                sunDrawableRes = R.drawable.ic_sun,
                sunSize = Medium150,
                sunriseTime = LocalDateTime.of(LocalDate.now(), LocalTime.NOON),
                sunsetTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT),
                sunriseFormatted = "12:00",
                sunsetFormatted = "00:00",
                modifier = Modifier.padding(Medium100)
            )
        }
    }
}
