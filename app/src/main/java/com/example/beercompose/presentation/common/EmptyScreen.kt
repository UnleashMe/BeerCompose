package com.example.beercompose.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beercompose.util.LocalAppGradient

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    text: String = stringResource(id = com.example.beercompose.R.string.nothing_to_show),
    onToMenuClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LocalAppGradient.current.gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colors.onSurface
            )
            CustomButton(onClick = {
                onToMenuClick()
            }, text = stringResource(com.example.beercompose.R.string.to_menu))
        }
    }
}