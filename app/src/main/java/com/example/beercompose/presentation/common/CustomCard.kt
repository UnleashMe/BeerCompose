package com.example.beercompose.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    backGroundColor: Color = MaterialTheme.colors.surface,
    borderWidth: Dp = 3.dp,
    borderColor: Color = MaterialTheme.colors.secondary,
    cornerRadius: Dp = 12.dp,
    padding: Dp = 12.dp,
    content: @Composable () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = backGroundColor,
        border = BorderStroke(borderWidth, borderColor),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Column(
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}