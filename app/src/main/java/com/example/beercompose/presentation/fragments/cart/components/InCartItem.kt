package com.example.beercompose.presentation.fragments.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.beercompose.domain.model.InCartItem
import com.example.beercompose.presentation.common.CartButton

@Composable
fun InCartItem(
    inCartItem: InCartItem,
    onMinusClick: (InCartItem) -> Unit,
    onPlusClick: (InCartItem) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(3.dp, MaterialTheme.colors.secondary),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = inCartItem.image),
                contentDescription = null,
                modifier = Modifier
                    .weight(3f)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) { onItemClick(inCartItem.UID) }
            )
            Column(
                modifier = Modifier
                    .weight(7f)
                    .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = inCartItem.title,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colors.onSurface
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            id = com.example.beercompose.R.string._price,
                            inCartItem.price * inCartItem.quantity
                        ),
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                    CartButton(inCartItem = inCartItem, addItemToCart = {}, onMinusClick = {
                        onMinusClick(inCartItem)
                    }, onPlusClick = {
                        onPlusClick(inCartItem)
                    }, modifier = Modifier.width(100.dp))
                }
            }
        }
    }
}