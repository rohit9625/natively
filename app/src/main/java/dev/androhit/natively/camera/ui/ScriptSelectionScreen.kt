package dev.androhit.natively.camera.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.androhit.natively.R
import dev.androhit.natively.domain.TextScript
import dev.androhit.natively.ui.theme.NativelyTheme

data class ScriptUiState(
    val script: TextScript,
    val nameRes: Int,
    val languagesRes: Int,
    val symbolRes: Int,
)

fun TextScript.toUiState(
    name: Int,
    languages: Int,
    symbol: Int,
) = ScriptUiState(
    script = this,
    nameRes = name,
    languagesRes = languages,
    symbolRes = symbol
)


val scripts = TextScript.entries.map {
    when(it) {
        TextScript.Latin -> it.toUiState(
            name = R.string.latin,
            symbol = R.drawable.ic_latin_symbol,
            languages = R.array.latin_languages
        )

        TextScript.Devanagari -> it.toUiState(
            name = R.string.devanagari,
            symbol = R.drawable.ic_devanagari_symbol,
            languages = R.array.devanagari_languages
        )

        TextScript.Chinese -> it.toUiState(
            name = R.string.chinese,
            symbol = R.drawable.ic_chinese_symbol,
            languages = R.array.chinese_languages
        )

        TextScript.Japanese -> it.toUiState(
            name = R.string.japanese,
            symbol = R.drawable.ic_japanese_symbol,
            languages = R.array.japanese_languages
        )

        TextScript.Korean -> it.toUiState(
            name = R.string.korean,
            symbol = R.drawable.ic_korean_symbol,
            languages = R.array.korean_languages
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScriptSelectionScreen(
    isFirstLaunch: Boolean,
    script: TextScript,
    onProceed: (TextScript) -> Unit,
) {
    var selectedScript by remember { mutableStateOf(script) }
    var error by remember { mutableStateOf<Int?>(null) }

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
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(scripts) { script ->
                    ScriptItem(
                        script = script,
                        onClick = {
                            selectedScript = script.script
                            error = null
                        },
                        isSelected = script.script == selectedScript
                    )
                }
            }

            Text(
                text = error?.let { stringResource(error!!) } ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )

            Button(
                onClick = {
                    selectedScript?.let {
                        error = null
                        onProceed(it)
                    } ?: run {
                        error = R.string.select_script_error
                    }
                },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 24.dp),
                enabled = error == null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(
                        if (isFirstLaunch) R.string.proceed
                        else R.string.update_script
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun ScriptItem(
    script: ScriptUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if(isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(script.symbolRes),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = stringResource(script.nameRes),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = "${stringResource(R.string.languages)}: ${stringArrayResource(script.languagesRes).joinToString(", ")}",
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
        ScriptSelectionScreen(
            isFirstLaunch = false,
            script = TextScript.Latin,
            onProceed = {}
        )
    }
}

@Preview
@Composable
fun ScriptItemPreview() {
    NativelyTheme(dynamicColor = false) {
        Surface {
            ScriptItem(
                script = ScriptUiState(
                    script = TextScript.Latin,
                    nameRes = R.string.latin,
                    languagesRes = R.array.latin_languages,
                    symbolRes = R.drawable.ic_latin_symbol
                ),
                onClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
