package com.example.beercompose.presentation.fragments.itemlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beercompose.R

@Composable
fun SearchRow(
    searchField: String,
    onValueChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(8.dp)

    ) {
        OutlinedTextField(
            value = searchField,
            onValueChange = {
                onValueChanged(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.onBackground)
                .border(
                    width = 3.dp,
                    color = MaterialTheme.colors.onSurface,
                    shape = RoundedCornerShape(12.dp)
                ),
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            trailingIcon = {
                IconButton(onClick = {
                    onValueChanged("")
                }) {
                    Icon(
                        imageVector = if (searchField.isEmpty()) Icons.Default.Search else Icons.Default.Close,
                        contentDescription = null
                    )
                }
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search),
                    color = MaterialTheme.colors.onSurface
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(cursorColor = MaterialTheme.colors.onSurface)
        )
    }
}