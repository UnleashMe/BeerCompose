package com.example.beercompose.presentation.fragments.menu_categories_list.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beercompose.data.common.MenuCategory

@Composable
fun CategoryItem(
    menuCategory: MenuCategory,
    onClick: () -> Unit
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(indication = null, interactionSource = interactionSource) {
                onClick()
            },
        contentAlignment = Alignment.BottomStart
    ) {
        Image(
            painter = painterResource(id = menuCategory.previewImage),
            contentDescription = stringResource(
                id = menuCategory.name
            ),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )
        Canvas(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        Color.Black
                    )
                )
            )
        }
        Text(
            text = stringResource(id = menuCategory.name),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 36.sp,
            modifier = Modifier.padding(8.dp)
        )
    }
}