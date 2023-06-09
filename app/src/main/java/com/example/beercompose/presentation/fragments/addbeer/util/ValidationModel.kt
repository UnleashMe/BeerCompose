package com.example.beercompose.presentation.fragments.addbeer.util

data class ValidationModel(
    val title: String = "",
    val description: String = "",
    val type: String = "",
    val price: String = "",
    val alc: String = "",
    val salePercentage: String = "",
    val weight: String = ""
)
