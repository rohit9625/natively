package dev.androhit.natively.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.androhit.natively.R
import dev.androhit.natively.ui.theme.NativelyTheme

enum class CameraFeature {
    LiveTranslate,
    ImageTranslate
}

@Composable
fun SwitchFeatureBottomBar(
    selectedFeature: CameraFeature,
    onFeatureSelected: (CameraFeature) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(64.dp)
            .width(240.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        tonalElevation = 6.dp,
        shadowElevation = 12.dp
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            val indicatorOffset by animateDpAsState(
                targetValue = if (selectedFeature == CameraFeature.LiveTranslate) (-55).dp else 55.dp,
                animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
                label = "IndicatorOffset"
            )

            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .size(width = 100.dp, height = 48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FeatureItem(
                    icon = painterResource(R.drawable.ic_translate),
                    label = stringResource(R.string.live),
                    isSelected = selectedFeature == CameraFeature.LiveTranslate,
                    onClick = { onFeatureSelected(CameraFeature.LiveTranslate) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                FeatureItem(
                    icon = painterResource(R.drawable.ic_image_translate),
                    label = stringResource(R.string.photo),
                    isSelected = selectedFeature == CameraFeature.ImageTranslate,
                    onClick = { onFeatureSelected(CameraFeature.ImageTranslate) }
                )
            }
        }
    }
}

@Composable
private fun FeatureItem(
    icon: Painter,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "ContentColor"
    )

    Column(
        modifier = Modifier
            .size(100.dp, 48.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Preview
@Composable
private fun SwitchFeatureBottomBarPreview() {
    var selectedFeature by remember { mutableStateOf(CameraFeature.LiveTranslate) }
    NativelyTheme(dynamicColor = false) {
        SwitchFeatureBottomBar(
            selectedFeature = selectedFeature,
            onFeatureSelected = { selectedFeature = it }
        )
    }
}
