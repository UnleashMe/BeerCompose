package com.example.beercompose.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun TextFieldWithError(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isHidden: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = {
                Text(text = label, color = MaterialTheme.colors.onSurface)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isHidden) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.outlinedTextFieldColors(cursorColor = MaterialTheme.colors.onSurface)
        )
        Text(text = error, color = Color.Red)
    }
}