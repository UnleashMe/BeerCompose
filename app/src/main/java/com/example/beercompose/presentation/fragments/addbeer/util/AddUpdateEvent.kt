package com.example.beercompose.presentation.fragments.addbeer.util

sealed class AddUpdateEvent {
    object ButtonClick : AddUpdateEvent()
    data class TitleTextChanged(val text: String) : AddUpdateEvent()
    data class DescriptionTextChanged(val text: String) : AddUpdateEvent()
    data class TypeTextChanged(val text: String) : AddUpdateEvent()
    data class PriceTextChanged(val text: String) : AddUpdateEvent()
    data class AlcPercentageTextChanged(val text: String) : AddUpdateEvent()
    data class SalePercentageTextChanged(val text: String) : AddUpdateEvent()
    data class WeightTextChanged(val text: String) : AddUpdateEvent()
}
