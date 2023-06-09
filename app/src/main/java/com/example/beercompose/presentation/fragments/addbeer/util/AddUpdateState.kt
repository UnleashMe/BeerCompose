package com.example.beercompose.presentation.fragments.addbeer.util

import com.example.beercompose.R
import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.domain.model.DomainItem

data class AddUpdateState(
    val mode: AddUpdateMode = AddUpdateMode.ADD,
    val validationModel: ValidationModel = ValidationModel(),
    val addUpdateErrorFields: AddUpdateErrorFields = AddUpdateErrorFields(),
    val isButtonEnabled: Boolean = false,
    val mainItem: DomainItem = DomainItem()

) {
    val screenTitle: Int
        get() = when (mode) {
            AddUpdateMode.ADD -> if (mainItem.category == MenuCategory.BeerCategory) {
                R.string.add_beer
            } else {
                R.string.add_snack
            }
            AddUpdateMode.UPDATE -> R.string.changing_item
        }

}