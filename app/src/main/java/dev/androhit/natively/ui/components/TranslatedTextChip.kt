package dev.androhit.natively.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.androhit.natively.ui.theme.NativelyTheme

@Composable
fun TranslateTextChip(
    text: String,
    onTranslate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(6.dp))

            TextButton(
                onClick = onTranslate,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Translate")
            }
        }
    }
}

@Preview
@Composable
private fun TranslateTextChipPreview() {
    NativelyTheme {
        TranslateTextChip(
            text = "This is a sample of detected text that might be translated.",
            onTranslate = {}
        )
    }
}