package com.example.beercompose.presentation.util.navigation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beercompose.R
import com.example.beercompose.presentation.common.CustomButton

@Composable
fun CartScreenBottomBar(price: Double, onButtonClick: () -> Unit) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 3.dp,
                color = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(MaterialTheme.colors.surface)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = stringResource(id = R.string.full_price),
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = stringResource(id = R.string._price, price),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )
        }
        CustomButton(onClick = {
            onButtonClick()
            Toast.makeText(context, context.getString(R.string.order_toast), Toast.LENGTH_SHORT)
                .show()
        }, enabled = price > 0.0, text = stringResource(id = R.string.order))
    }
}