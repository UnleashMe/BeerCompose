package com.example.beercompose.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    enabled: Boolean = true,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        border = BorderStroke(2.dp, MaterialTheme.colors.secondary),
        shape = RoundedCornerShape(18.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            disabledBackgroundColor = Color.LightGray
        ),
        modifier = modifier
    ) {
        Text(text = text, color = if (enabled) MaterialTheme.colors.onSurface else Color.Black)
    }
}