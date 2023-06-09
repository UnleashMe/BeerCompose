package com.example.beercompose.presentation.fragments.itemlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TypeItem(
    title: String,
    isChosen: Boolean,
    onClick: (String) -> Unit,
    onChosenClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Box(
        modifier = modifier
            .border(
                3.dp,
                color = if (isChosen) MaterialTheme.colors.error else MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(100.dp)
            )
            .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(100.dp))
            .clickable(indication = null, interactionSource = interactionSource) {
                if (isChosen) {
                    onChosenClick()

                } else {
                    onClick(title)
                }
            }
            .padding(10.dp)
    ) {
        Text(text = title, color = MaterialTheme.colors.onSurface)
    }
}