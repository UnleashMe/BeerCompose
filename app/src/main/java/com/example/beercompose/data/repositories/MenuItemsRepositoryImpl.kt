package com.example.beercompose.data.repositories

import android.accounts.NetworkErrorException
import com.example.beercompose.data.database.BeersDao
import com.example.beercompose.data.database.entities.MenuItemEntity
import com.example.beercompose.util.mapper.MenuItemMapper
import com.example.beercompose.data.network.dto.beer.BeerResponse
import com.example.beercompose.data.network.dto.beer.BeerRequest
import com.example.beercompose.data.network.dto.snack.SnackResponse
import com.example.beercompose.data.network.dto.snack.SnackRequest
import com.example.beercompose.data.network.ApiClient
import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.domain.model.DomainItem
import com.example.beercompose.domain.repositories.MenuItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MenuItemsRepositoryImpl @Inject constructor(
    private val beerMapper: MenuItemMapper,
    private val apiClient: ApiClient,
    private val beersDao: BeersDao
) : MenuItemsRepository {

    override fun getDatabaseMenuItems(): Flow<List<DomainItem>> {
        return beersDao
            .getDatabaseMenuItems()
            .map { list ->
                list.map {
                    beerMapper.buildDomainFromEntity(it)
                }
            }
            .onEach {
                if (it.isEmpty()) {
                    getItems()
                }
            }
    }

    private suspend fun getItems() {
        val requestBeers = apiClient.getBeers()
        val requestSnacks = apiClient.getSnacks()

        if (requestBeers.failed || !requestBeers.isSuccessful) {
            throw requestBeers.exception ?: NetworkErrorException("Beers request went wrong")
        }
        if (requestSnacks.failed || !requestSnacks.isSuccessful) {
            throw requestSnacks.exception ?: NetworkErrorException("Snacks request went wrong")
        }
        val allItems = requestBeers.body.data.map {
            beerMapper.buildBeerEntityFromNetwork(it)
        } + requestSnacks.body.data.map {
            beerMapper.buildSnackEntityFromNetwork(it)
        }
        beersDao.addMenuItems(allItems)
    }

    override suspend fun getMenuItemById(itemId: String): DomainItem {
        val request = beersDao.getMenuItemById(itemId)
        return beerMapper.buildDomainFromEntity(request)
    }

    override suspend fun addApiBeer(beerRequest: BeerRequest): BeerResponse? {

        val request = apiClient.addBeer(beerRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    override suspend fun addBeerToDatabase(beerResponse: BeerResponse) {
        beersDao.addOneMenuItem(beerMapper.buildBeerEntityFromResponse(beerResponse))
    }

    override suspend fun addApiSnack(snackRequest: SnackRequest): SnackResponse? {
        val request = apiClient.addSnack(snackRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    override suspend fun addSnackToDatabase(snackResponse: SnackResponse) {
        beersDao.addOneMenuItem(beerMapper.buildSnackEntityFromResponse(snackResponse))
    }

    override suspend fun deleteApiBeer(beerId: String): BeerResponse? {

        val request = apiClient.deleteBeer(beerId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        deleteMenuItemFromDatabase(beerId)
        return request.body
    }

    private suspend fun deleteMenuItemFromDatabase(itemId: String) {
        beersDao.deleteMenuItemById(itemId)
    }

    override suspend fun deleteApiSnack(snackId: String): SnackResponse? {
        val request = apiClient.deleteSnack(snackId)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        deleteMenuItemFromDatabase(snackId)
        return request.body
    }

    override suspend fun updateApiBeer(beerId: String, beerRequest: BeerRequest): BeerResponse? {

        val request = apiClient.updateBeer(beerId, beerRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    override suspend fun updateDatabaseBeer(beerId: String, beerRequest: BeerRequest) {
        beersDao.updateMenuItem(
            item = MenuItemEntity(
                UID = beerId,
                description = beerRequest.description,
                name = beerRequest.name,
                price = beerRequest.price,
                type = beerRequest.type,
                alcPercentage = beerRequest.alcPercentage,
                category = MenuCategory.BeerCategory,
                salePercentage = beerRequest.salePercentage ?: 0.0,
                imageUrl = beerRequest.imagePath
            )
        )
    }

    override suspend fun updateDatabaseMenuItem(itemEntity: MenuItemEntity) {
        beersDao.updateMenuItem(itemEntity)
    }

    override suspend fun updateApiSnack(
        snackId: String,
        snackRequest: SnackRequest
    ): SnackResponse? {
        val request = apiClient.updateSnack(snackId, snackRequest)

        if (request.failed) {
            return null
        }
        if (!request.isSuccessful) {
            return null
        }
        return request.body
    }

    override suspend fun updateDatabaseSnack(snackId: String, snackRequest: SnackRequest) {
        beersDao.updateMenuItem(
            item = MenuItemEntity(
                UID = snackId,
                description = snackRequest.description,
                name = snackRequest.name,
                price = snackRequest.price,
                type = snackRequest.type,
                category = MenuCategory.SnackCategory,
                alcPercentage = null,
                weight = snackRequest.weight,
                imageUrl = snackRequest.imagePath
            )
        )
    }
}