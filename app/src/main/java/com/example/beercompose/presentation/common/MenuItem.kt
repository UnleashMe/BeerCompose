package com.example.beercompose.presentation.common

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.beercompose.R
import com.example.beercompose.domain.model.DomainItem
import com.example.beercompose.domain.model.InCartItem
import kotlinx.coroutines.launch
import androidx.compose.material.MaterialTheme

@Composable
fun MenuItem(
    item: DomainItem,
    isItemLiked: Boolean,
    onDeleteClick: (String) -> Unit,
    onChangeClick: (String) -> Unit,
    onItemClick: (String) -> Unit,
    showAdminIcons: Boolean = false,
    onFavClick: (String) -> Unit,
    inCartItem: InCartItem,
    onAddToCartClick: (String) -> Unit,
    onMinusClick: (InCartItem) -> Unit,
    onPlusClick: (InCartItem) -> Unit,
    isUserLogged: Boolean,
    isToastShowing: Boolean,
    onToastStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var selected by remember {
        mutableStateOf(false)
    }

    val scale = remember {
        androidx.compose.animation.core.Animatable(1f)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    LaunchedEffect(key1 = selected) {
        if (selected) {
            launch {
                scale.animateTo(
                    targetValue = 0.3f,
                    animationSpec = tween(
                        durationMillis = 500
                    )
                )
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessVeryLow
                    )
                )
            }
        } else {
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 500
                    )
                )
            }
        }
    }

    Box(
        modifier = modifier
            .width(187.dp)
            .height(300.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 3.dp,
                color = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                Brush.verticalGradient(
                    listOf(MaterialTheme.colors.surface, MaterialTheme.colors.onBackground),
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(indication = null, interactionSource = interactionSource) {
                    onItemClick(item.UID)
                }
                .padding(top = 16.dp, bottom = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(7f)
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.TopStart
            ) {

                if (item.salePercentage != 0.0) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(8.dp, 8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.discount),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = stringResource(
                                id = R.string.sale_percentage,
                                item.salePercentage
                            ),
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }

                Image(
                    painter = rememberAsyncImagePainter(model = item.image),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.Center)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = if (isItemLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(indication = null, interactionSource = interactionSource) {
                            if (isItemLiked) {
                                selected = false
                                onFavClick(item.UID)
                            } else {
                                if (isUserLogged) {
                                    selected = true
                                    onFavClick(item.UID)
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            R.string.need_to_login_toast,
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            }
                        }
                        .scale(scale.value),
                    colorFilter = if (isItemLiked) ColorFilter.tint(Color.Red) else ColorFilter.tint(
                        MaterialTheme.colors.onSurface
                    )
                )
                CartButton(
                    inCartItem = inCartItem,
                    addItemToCart = {
                        if (isUserLogged) onAddToCartClick(inCartItem.UID) else {
                            if (!isToastShowing) {
                                onToastStart()
                                Toast.makeText(
                                    context,
                                    R.string.need_to_login_toast,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    onMinusClick = { onMinusClick(inCartItem) },
                    onPlusClick = { onPlusClick(inCartItem) },
                    modifier = Modifier
                        .size(90.dp, 32.dp)
                )
            }
            Text(
                text = item.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = 10.dp)
                    .align(Alignment.Start),
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onSurface
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string._price, item.discountedPrice),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(2f)
                        .padding(horizontal = 10.dp),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    color = MaterialTheme.colors.onSurface
                )
                if (showAdminIcons) {
                    IconButton(onClick = {
                        onChangeClick(item.UID)
                    }, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSurface
                        )
                    }
                    IconButton(onClick = {
                        onDeleteClick(item.UID)
                    }, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
        }
    }
}