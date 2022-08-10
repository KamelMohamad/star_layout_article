package com.example.star_layout_article

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
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
    content: @Composable StarLayoutScope.() -> Unit
) {
    var starRadiusPx = with(LocalDensity.current) { radius.roundToPx() }
    var totalRadius = 0
    var maxChildDiameter = 0
    var count = 0
    Layout(
        content = { StarLayoutScope.content() },
        modifier = modifier
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
        val itemDataList = measurables.map { it.parentData as? StarItemData }
        count = itemDataList.filter { it == null || it.onArm }.size
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
            placeables.forEachIndexed { index, placeable ->
                val itemData = itemDataList.getOrNull(index)
                val slideFactor = itemData?.slide ?: 1f
                val customAngle = itemData?.customAngle ?: 0.0
                val onEdge = itemData?.onArm ?: true
                println("index is $index, slide data : $itemData")
                val drawAngle = if (onEdge) angle else Math.toRadians(-customAngle)
                placeable.place(
                    totalRadius - placeable.width / 2 + (starRadiusPx * slideFactor * cos(drawAngle)).toInt(),
                    totalRadius - placeable.height / 2 + (starRadiusPx * slideFactor * sin(drawAngle)).toInt(),
                )
                if (onEdge) angle += step
            }
/*            placeables.forEach { placeable ->
                placeable.place(
                    totalRadius - placeable.width / 2 + (starRadiusPx * cos(angle)).toInt(),
                    totalRadius - placeable.height / 2 + (starRadiusPx * sin(angle)).toInt(),
                )
                angle += step
            }*/
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

interface StarLayoutScope {
    fun Modifier.adjustPlace(slide: Float, onArm: Boolean = true, customAngle: Double = 0.0) =
        this.then(
            StarItemData(slide, onArm, customAngle)
        )

    companion object : StarLayoutScope
}

private data class StarItemData(
    val slide: Float,
    val onArm: Boolean = true,
    val customAngle: Double = 0.0
) :
    ParentDataModifier {

    override fun Density.modifyParentData(parentData: Any?) = this@StarItemData
}
