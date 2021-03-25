package dev.kaitei.feature.photo.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.imageloading.ImageLoadState
import dev.chrisbanes.accompanist.imageloading.MaterialLoadingImage
import dev.kaitei.feature.photo.R

private const val MIN_ALPHA = 0.2F
private const val MID_ALPHA = 0.4F
private const val MAX_ALPHA = 0.6F

private const val ANIMATION_DURATION = 1000

@Composable
internal fun PhotoItem(photo: String) {
    CoilImage(
        data = photo,
        modifier = Modifier.aspectRatio(1F)
    ) { imageState ->
        when (imageState) {
            is ImageLoadState.Success -> {
                MaterialLoadingImage(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    result = imageState,
                    contentDescription = stringResource(R.string.dog_image_description),
                    fadeInEnabled = true,
                    fadeInDurationMs = 600,
                    modifier = Modifier.padding(1.dp)
                )
            }
            is ImageLoadState.Error -> {
                Placeholder(animationEnabled = false, res = R.drawable.ic_error_24dp)
            }
            ImageLoadState.Loading -> {
                Placeholder(animationEnabled = true, res = R.drawable.ic_paw_24dp)
            }
            ImageLoadState.Empty -> {
                Placeholder(animationEnabled = false, res = R.drawable.ic_paw_24dp)
            }
        }
    }
}

@Composable
internal fun Placeholder(
    @DrawableRes res: Int,
    animationEnabled: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition()
    val imageAlpha = if (animationEnabled) {
        val alpha by infiniteTransition.animateFloat(
            initialValue = MIN_ALPHA,
            targetValue = MAX_ALPHA,
            animationSpec = infiniteRepeatable(
                animation = tween(ANIMATION_DURATION, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        alpha
    } else {
        MID_ALPHA
    }

    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(1.dp)
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.05F))
            .border(2.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.1F))
    ) {
        Icon(
            painter = painterResource(res),
            modifier = Modifier
                .size(48.dp)
                .alpha(imageAlpha)
                .align(Alignment.Center),
            contentDescription = stringResource(R.string.content_description_loading)
        )
    }
}