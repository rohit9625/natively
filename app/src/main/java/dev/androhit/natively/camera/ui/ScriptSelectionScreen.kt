package dev.androhit.natively.camera.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.androhit.natively.R
import dev.androhit.natively.ui.theme.NativelyTheme

enum class TextScript(
    val displayName: String,
    val examples: List<String>
) {
    Latin("Latin", listOf("English", "Spanish", "French", "German")),
    Devanagari("Devanagari", listOf("Hindi", "Marathi", "Nepali", "Sanskrit")),
    Chinese("Chinese", listOf("Chinese Simplified", "Traditional")),
    Japanese("Japanese", listOf("Japanese")),
    Korean("Korean", listOf("Korean"))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScriptSelectionScreen(
    onProceed: (TextScript) -> Unit,
) {
    var selectedScript by remember { mutableStateOf<TextScript?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.choose_text_script),
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.select_script_rationale),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(TextScript.entries) { script ->
                    ScriptItem(
                        script = script,
                        onClick = { selectedScript = script },
                        isSelected = script == selectedScript
                    )
                }
            }

            Button(
                onClick = { selectedScript?.let { onProceed(it) } },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.proceed),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun ScriptItem(
    script: TextScript,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val symbol = when (script) {
        TextScript.Latin -> painterResource(R.drawable.ic_latin_symbol)
        TextScript.Chinese -> painterResource(R.drawable.ic_chinese_symbol)
        TextScript.Devanagari -> painterResource(R.drawable.ic_devanagari_symbol)
        TextScript.Japanese -> painterResource(R.drawable.ic_japanese_symbol)
        TextScript.Korean -> painterResource(R.drawable.ic_korean_symbol)
    }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if(isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = symbol,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = script.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "Languages: ${script.examples.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                lineHeight = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScriptSelectionScreenPreview() {
    NativelyTheme(dynamicColor = false) {
        ScriptSelectionScreen(onProceed = {})
    }
}

@Preview
@Composable
fun ScriptItemPreview() {
    NativelyTheme(dynamicColor = false) {
        Surface {
            ScriptItem(
                script = TextScript.Latin,
                onClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
