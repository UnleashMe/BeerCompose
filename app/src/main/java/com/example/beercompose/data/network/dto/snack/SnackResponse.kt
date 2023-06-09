package com.example.beercompose.data.network.dto.snack

data class SnackResponse(
    val createdSnack: SnackData = SnackData(),
    val msg: String = ""
)