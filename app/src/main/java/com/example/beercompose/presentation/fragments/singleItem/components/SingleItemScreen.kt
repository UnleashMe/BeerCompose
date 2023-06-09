package com.example.beercompose.presentation.fragments.singleItem.components

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.beercompose.R
import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.domain.model.InCartItem
import com.example.beercompose.domain.model.User
import com.example.beercompose.presentation.common.CustomCard
import com.example.beercompose.presentation.common.CartButton
import com.example.beercompose.presentation.common.MenuItem
import com.example.beercompose.presentation.util.navigation.Screen
import com.example.beercompose.presentation.fragments.singleItem.SingleItemViewModel
import com.example.beercompose.presentation.fragments.singleItem.util.SingleItemEvent
import com.example.beercompose.util.LocalAppGradient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ItemInfoScreen(
    viewModel: SingleItemViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val state = viewModel.state.value
    val scrollState = rememberScrollState()
    val id = viewModel.id
    val context = LocalContext.current

    var isToastShowing by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = isToastShowing) {
        if (isToastShowing) {
            delay(2500)
            isToastShowing = false
        }
    }

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
                        durationMillis = 300
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

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(SingleItemEvent.GetMenuItem(id))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppGradient.current.gradient)
            .padding(16.dp)
            .padding(bottom = 50.dp)
            .verticalScroll(scrollState)
    ) {
        CustomCard(modifier = Modifier.padding(bottom = 12.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.TopStart)
                        .clickable { navController.navigateUp() },
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
                )
                Box(modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.TopEnd)
                    .clickable(indication = null, interactionSource = interactionSource) {
                        if (state.user.role != User.Role.NoUser) {
                            selected = !state.isMainItemLiked
                            viewModel.onEvent(SingleItemEvent.OnFavMainItem)
                        } else {
                            Toast
                                .makeText(
                                    context,
                                    context.getString(R.string.need_to_login_toast),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
                    .scale(scale.value)) {
                    Image(
                        imageVector = if (state.isMainItemLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center),
                        colorFilter = if (state.isMainItemLiked) ColorFilter.tint(Color.Red) else ColorFilter.tint(
                            MaterialTheme.colors.onSurface
                        )
                    )
                    if (state.likes > 0) {
                        Text(
                            text = state.likes.toString(),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(bottom = 4.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
                Image(
                    painter = rememberAsyncImagePainter(model = state.mainItem.image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 180.dp, height = 300.dp)
                        .padding(bottom = 12.dp)
                        .align(Alignment.Center)
                )
                CartButton(
                    inCartItem = state.inCartItem,
                    addItemToCart = {
                        if (state.user.role != User.Role.NoUser) {
                            viewModel.onEvent(SingleItemEvent.AddMainItemToCart)
                        } else {
                            if (!isToastShowing) {
                                isToastShowing = true
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.need_to_login_toast),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    onMinusClick = { viewModel.onEvent(SingleItemEvent.OnMinusMainItem) },
                    onPlusClick = { viewModel.onEvent(SingleItemEvent.OnPlusMainItem) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(width = 100.dp, height = 40.dp)
                )
            }
            Text(
                text = state.mainItem.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 12.dp),
                color = MaterialTheme.colors.onSurface
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string._price, state.mainItem.price),
                    fontSize = 16.sp,
                    style = if (state.mainItem.salePercentage != 0.0) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle(),
                    color = MaterialTheme.colors.onSurface
                )
                if (state.mainItem.salePercentage != 0.0) {
                    Text(
                        text = stringResource(id = R.string._price, state.mainItem.discountedPrice),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                Text(
                    text = if (state.mainItem.category == MenuCategory.BeerCategory) stringResource(
                        id = R.string.alcPercentage, state.mainItem.alcPercentage
                    ) else stringResource(id = R.string.weight, state.mainItem.weight),
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }
            Text(
                text = state.mainItem.description, fontSize = 14.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
        Text(
            text = stringResource(id = R.string.related_products),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
            color = MaterialTheme.colors.onSurface
        )
        CustomCard {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = state.itemsSet.toList()) { item ->
                    MenuItem(
                        item = item,
                        onDeleteClick = {},
                        onChangeClick = {},
                        onItemClick = {
                            navController.navigate(Screen.ItemInfoScreen.passId(it))
                        },
                        isItemLiked = state.likedItems.contains(item.UID),
                        showAdminIcons = false,
                        onFavClick = { viewModel.onEvent(SingleItemEvent.OnFavItemInSet(it)) },
                        inCartItem = state.inCartItemsSet.find { it.UID == item.UID }
                            ?: InCartItem(),
                        onAddToCartClick = { viewModel.onEvent(SingleItemEvent.AddSetItemToCart(it)) },
                        onMinusClick = { viewModel.onEvent(SingleItemEvent.OnMinusSetItem(it)) },
                        onPlusClick = { viewModel.onEvent(SingleItemEvent.OnPlusSetItem(it)) },
                        isUserLogged = state.user.role != User.Role.NoUser,
                        isToastShowing = isToastShowing,
                        onToastStart = { isToastShowing = true }
                    )
                }
            }
        }
    }
}