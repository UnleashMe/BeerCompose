package com.example.beercompose.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardContent(
    text: String,
    textSize: TextUnit = 24.sp,
    textModifier: Modifier = Modifier,
    onButtonClick: () -> Unit = {},
    buttonEnabled: Boolean = true,
    buttonText: String,
    content: @Composable () -> Unit = {}
) {
    Text(
        text = text,
        fontSize = textSize,
        modifier = textModifier.padding(bottom = 12.dp),
        color = MaterialTheme.colors.onSurface
    )
    content()
    CustomButton(text = buttonText, enabled = buttonEnabled, onClick = { onButtonClick() })

}