package dev.kaitei.feature.list.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kaitei.common.theme.DoggoTheme

@Composable
internal fun BreedItem(breed: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = breed, style = MaterialTheme.typography.body1)
    }
}

@Composable
internal fun SubBreedItem(breed: String, subBreed: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(LocalContentColor.current.copy(alpha = 0.05F))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val dotColor = LocalContentColor.current.copy(alpha = 0.1F)
        Canvas(
            modifier = Modifier.size(4.dp),
        ) {
            drawCircle(dotColor)
        }

        val text = with(AnnotatedString.Builder()) {
            pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
            append(subBreed)
            pop()
            append(" $breed")
            toAnnotatedString()
        }

        Text(text, style = MaterialTheme.typography.body1)
    }
}

@Preview
@Composable
internal fun BreedItemPreview() {
    DoggoTheme {
        Column {
            BreedItem(breed = "Woof", onClick = { })
            SubBreedItem(breed = "Woof", subBreed = "Dogge", onClick = { })
        }
    }
}