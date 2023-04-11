package com.nabin0.jobcite.presentation.home.jobs_home.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerAnimationForJobitems(
    imageHeight: Dp = 200.dp,
    padding: Dp = 16.dp
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val cardWidthPx = with(LocalDensity.current) {
            (maxWidth - (padding * 2)).toPx()
        }
        val cardHeightPx = with(LocalDensity.current) {
            (imageHeight - padding).toPx()
        }
        val gradientWidth: Float = (0.2f * cardHeightPx)

        val infiniteTransition = rememberInfiniteTransition()
        val xCardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = cardWidthPx + gradientWidth,
            animationSpec = infiniteRepeatable(
                tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300
                ),
                repeatMode = RepeatMode.Restart
            )
        )

        val yCardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = cardHeightPx + gradientWidth,
            animationSpec = infiniteRepeatable(
                tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300
                ),
                repeatMode = RepeatMode.Restart
            )
        )

        val colors = listOf(
            Color.LightGray.copy(alpha = 0.9f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.9f)
        )

        ShimmerCardItem(
            colors = colors,
            padding = padding,
            cardHeight = imageHeight,
            xShimmer = xCardShimmer.value,
            yShimmer = yCardShimmer.value,
            gradientWidth = gradientWidth
        )
        Spacer(modifier = Modifier.height(15.dp))

    }
}

@Composable
fun ShimmerCardItem(
    colors: List<Color>,
    padding: Dp,
    cardHeight: Dp,
    xShimmer: Float,
    yShimmer: Float,
    gradientWidth: Float
) {
    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(xShimmer - gradientWidth, yShimmer - gradientWidth),
        end = Offset(xShimmer, yShimmer)
    )

    Column(modifier = Modifier.padding(padding)) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = MaterialTheme.shapes.small
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(22.dp)
                        .background(brush = brush)
                )
            }
            Surface(
                shape = MaterialTheme.shapes.small
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .height(22.dp)
                        .background(brush = brush)
                )
            }

        }
        Surface(
            shape = MaterialTheme.shapes.small
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeight)
                        .background(brush = brush)
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            shape = MaterialTheme.shapes.small
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(brush = brush)
            )
        }
    }

}