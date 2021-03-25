package dev.kaitei.common.ui

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import dev.kaitei.common.theme.DoggoTheme
import dev.kaitei.doggo.ui.R

@Composable
fun ErrorView(
    title: String,
    message: String,
    button: String,
    @RawRes animationRes: Int,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                title,
                style = MaterialTheme.typography.h6,
                maxLines = 2,
                textAlign = TextAlign.Center
            )

            val animationSpec = remember { LottieAnimationSpec.RawRes(animationRes) }
            val animationState =
                rememberLottieAnimationState(autoPlay = true, repeatCount = 2)

            LottieAnimation(
                spec = animationSpec,
                animationState = animationState,
                modifier = Modifier
                    .size(160.dp)
            )

            Text(
                message,
                style = MaterialTheme.typography.body1,
                maxLines = 3,
                textAlign = TextAlign.Center
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onClick = onButtonClick
        ) {
            Text(button)
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun ErrorViewPreview() {
    DoggoTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colors.background) {
            ErrorView(
                title = "No internet connection",
                message = "Unable to establish connection with network. Please check your network setting and retry.",
                button = "Retry",
                animationRes = R.raw.astro_doggo,
                onButtonClick = { }
            )
        }
    }
}
