package com.example.beercompose.domain.use_cases.menu_items

import android.content.Context
import com.example.beercompose.R
import com.example.beercompose.data.network.dto.beer.BeerRequest
import com.example.beercompose.domain.repositories.MenuItemsRepository

class UpdateBeerUseCase(
    private val repository: MenuItemsRepository,
    private val context: Context
) {
    suspend operator fun invoke(beerId: String, beerRequest: BeerRequest): String {
        val response = repository.updateApiBeer(beerId, beerRequest)
        response?.let {
            repository.updateDatabaseBeer(beerId, beerRequest)
            return response.msg
        } ?: return context.getString(R.string.check_connection)
    }
}