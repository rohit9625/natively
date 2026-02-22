package dev.androhit.natively.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.androhit.natively.R
import dev.androhit.natively.ui.states.Language
import dev.androhit.natively.ui.theme.NativelyTheme

@Composable
fun LanguageSelectorBar(
    sourceLanguage: Language,
    targetLanguage: Language,
    availableLanguages: List<Language>,
    onSourceLanguageSelected: (Language) -> Unit,
    onTargetLanguageSelected: (Language) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.widthIn(max = 272.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        tonalElevation = 6.dp,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageDropdown(
                selectedLanguage = sourceLanguage,
                availableLanguages = availableLanguages.filter { it != targetLanguage },
                onLanguageSelected = onSourceLanguageSelected,
            )

            Icon(
                painter = painterResource(R.drawable.ic_right_arrow_notch),
                contentDescription = "Swap Languages",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            LanguageDropdown(
                selectedLanguage = targetLanguage,
                availableLanguages = availableLanguages.filter { it != sourceLanguage },
                onLanguageSelected = onTargetLanguageSelected,
            )
        }
    }
}

@Composable
private fun LanguageDropdown(
    selectedLanguage: Language,
    availableLanguages: List<Language>,
    onLanguageSelected: (Language) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Text(
            text = "${selectedLanguage.displayName} (${selectedLanguage.code})",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            availableLanguages.forEach { language ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "${language.displayName} (${language.code})",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    onClick = {
                        onLanguageSelected(language)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LanguageSelectorBarPreview() {
    var sourceLang by remember { mutableStateOf(Language.ENGLISH) }
    var targetLang by remember { mutableStateOf(Language.HINDI) }

    NativelyTheme(dynamicColor = false) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            LanguageSelectorBar(
                sourceLanguage = sourceLang,
                targetLanguage = targetLang,
                availableLanguages = Language.entries,
                onSourceLanguageSelected = { sourceLang = it },
                onTargetLanguageSelected = { targetLang = it }
            )
        }
    }
}
