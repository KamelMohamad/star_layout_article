package com.example.star_layout_article

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun StarLayout(
    radius: Dp,
    modifier: Modifier = Modifier,
    drawStyle: DrawStyle = Stroke(12f),
    color: Color = Color.Yellow,
    content: @Composable () -> Unit
) {
    var starRadiusPx = with(LocalDensity.current) { radius.roundToPx() }
    var totalRadius = 0
    var maxChildDiameter = 0
    var count = 0
    Layout(
        content = content,
        modifier = modifier
//            .background(Color.Gray)
            .drawWithCache {
                onDrawBehind {
                    //val path = StarShape.createStar(count, radius = starRadiusPx.toFloat())
                    val maxR = starRadiusPx.toFloat()
                    val minR = maxR * 0.5f
                    val path = createStarPath(maxR, minR, count)
                    path.translate(Offset(maxChildDiameter * 0.5f, maxChildDiameter * 0.5f))
                    drawPath(
                        path,
                        color = color,
                        style = drawStyle
                    )
                }
            }
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        count = placeables.size
        placeables.forEach {
            val h = it.height.toDouble()
            val w = it.width.toDouble()
            val diameter = sqrt(h * h + w * w)
            if (diameter > maxChildDiameter) maxChildDiameter = diameter.toInt()
        }
        totalRadius = starRadiusPx + maxChildDiameter / 2
        layout(totalRadius * 2, totalRadius * 2) {
            val step = PI * 2 / count
            var angle = 0.0
            placeables.forEach { placeable ->
                placeable.place(
                    totalRadius - placeable.width / 2 + (starRadiusPx * cos(angle)).toInt(),
                    totalRadius - placeable.height / 2 + (starRadiusPx * sin(angle)).toInt(),
                )
                angle += step
            }
        }
    }
}

private fun createStarPath(
    maxR: Float,
    minR: Float,
    tipCount: Int,
): Path {
    val path = Path()
    val step = PI / tipCount
    var r = maxR
    var angle = 0.0;
    path.moveTo(2 * maxR, maxR)
    repeat(tipCount * 2) {
        angle += step
        r = if (r == maxR) minR else maxR
        path.lineTo(maxR + r * cos(angle).toFloat(), maxR + r * sin(angle).toFloat())
    }
    path.close()
    return path
}

