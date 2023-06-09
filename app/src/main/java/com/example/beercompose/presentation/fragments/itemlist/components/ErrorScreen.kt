package com.example.beercompose.presentation.fragments.itemlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beercompose.presentation.common.CustomButton
import com.example.beercompose.R

@Composable
fun ErrorScreen(
    onTryAgainClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.couldnt_load_menu),
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            CustomButton(
                onClick = { onTryAgainClick() },
                text = stringResource(id = R.string.try_again)
            )
        }
    }
}