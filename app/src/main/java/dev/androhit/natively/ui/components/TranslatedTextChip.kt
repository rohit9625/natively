package dev.androhit.natively.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.androhit.natively.ui.theme.NativelyTheme

@Composable
fun TranslateTextChip(
    text: String,
    onTranslate: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    translatedText: String? = null,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        // Use IntrinsicSize.Min to ensure the width is determined by the text content
        Column(
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(12.dp)
                    .width(IntrinsicSize.Max)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic
                )

                translatedText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (translatedText == null) {
                    Text(
                        text = if (isLoading) "Translating..." else "Translate",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable(
                                enabled = !isLoading,
                                onClick = onTranslate,
                            )
                    )
                }
            }

            AnimatedVisibility(
                visible = isLoading,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp
                            )
                        ),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.Transparent
                )
            }
        }
    }
}

@Preview
@Composable
private fun TranslateTextChipPreview() {
    NativelyTheme(dynamicColor = false) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            TranslateTextChip(
                text = "Hello, how are you?",
                onTranslate = {}
            )

            TranslateTextChip(
                text = "Hello, how are you?",
                onTranslate = {},
                isLoading = true,
            )

            TranslateTextChip(
                text = "Hello, how are you?",
                onTranslate = {},
                translatedText = "नमस्ते, आप कैसे हैं?"
            )
        }
    }
}
