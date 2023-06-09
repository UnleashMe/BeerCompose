package com.example.beercompose.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beercompose.domain.model.InCartItem

@Composable
fun CartButton(
    inCartItem: InCartItem,
    cornerRadius: Dp = 24.dp,
    addItemToCart: (String) -> Unit,
    onMinusClick: (InCartItem) -> Unit,
    onPlusClick: (InCartItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {
            if (inCartItem.quantity == 0) {
                addItemToCart(inCartItem.UID)
            }
        },
        modifier = modifier,
        border = BorderStroke(2.dp, MaterialTheme.colors.secondary),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        if (inCartItem.quantity == 0) {
            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { onMinusClick(inCartItem) }, Modifier.size(20.dp)) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = null)
                }
                Text(text = inCartItem.quantity.toString(), fontSize = 14.sp)
                IconButton(onClick = { onPlusClick(inCartItem) }, Modifier.size(20.dp)) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }
}