package com.merveylcu.swipecard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

private const val scaleAnimationLabel = "scaleAnimationLabel"
private const val rotationZAnimationLabel = "rotationZAnimationLabel"

@Composable
fun <T> SwipeCard(
    item: T,
    actions: SwipeCardActions<T>,
    swipeThreshold: Float = 300f,
    sensitivityFactor: Float = 3f,
    content: @Composable () -> Unit
) {
    var offset by remember { mutableFloatStateOf(0f) }
    var dismissRight by remember { mutableStateOf(false) }
    var dismissLeft by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density
    var isSelected by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isSelected) 0.8f else 1f, label = scaleAnimationLabel)
    val rotationZ by animateFloatAsState(offset / 50, label = rotationZAnimationLabel)

    LaunchedEffect(dismissRight, item) {
        if (dismissRight) {
            actions.onSwipeRight(item)
            dismissRight = false
            offset = 0f
            isSelected = false
        }
    }

    LaunchedEffect(dismissLeft, item) {
        if (dismissLeft) {
            actions.onSwipeLeft(item)
            dismissLeft = false
            offset = 0f
            isSelected = false
        }
    }

    Box(modifier = Modifier
        .scale(scale)
        .offset { IntOffset(offset.roundToInt(), 0) }
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragEnd = {
                    when {
                        offset > swipeThreshold -> {
                            dismissRight = true
                        }

                        offset < -swipeThreshold -> {
                            dismissLeft = true
                        }

                        else -> {
                            offset = 0f
                            isSelected = false
                        }
                    }
                },
                onHorizontalDrag = { change, dragAmount ->
                    isSelected = true
                    offset += (dragAmount / density) * sensitivityFactor
                    if (change.positionChange() != Offset.Zero) change.consume()
                }
            )
        }
        .graphicsLayer(rotationZ = rotationZ)
    ) {
        content()
    }
}
