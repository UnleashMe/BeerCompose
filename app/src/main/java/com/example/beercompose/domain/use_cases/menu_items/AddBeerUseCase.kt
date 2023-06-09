package com.example.beercompose.domain.use_cases.menu_items

import android.content.Context
import com.example.beercompose.R
import com.example.beercompose.data.network.dto.beer.BeerRequest
import com.example.beercompose.domain.repositories.MenuItemsRepository

class AddBeerUseCase(
    private val repository: MenuItemsRepository,
    private val context: Context
) {
    suspend operator fun invoke(beerRequest: BeerRequest): String {
        val response = repository.addApiBeer(beerRequest)
        response?.let {
            repository.addBeerToDatabase(it)
            return it.msg
        } ?: return context.getString(R.string.check_connection)

    }
}