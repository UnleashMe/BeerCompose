package com.example.beercompose.util.mapper

import com.example.beercompose.data.database.entities.MenuItemEntity
import com.example.beercompose.data.network.dto.beer.BeerData
import com.example.beercompose.data.network.dto.beer.BeerResponse
import com.example.beercompose.data.network.dto.snack.SnackData
import com.example.beercompose.data.network.dto.snack.SnackResponse
import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.domain.model.DomainItem
import com.example.beercompose.util.Constants.BASE_URL
import javax.inject.Inject

class MenuItemMapper @Inject constructor() {
    fun buildDomainFromEntity(menuItemEntity: MenuItemEntity): DomainItem {
        if (menuItemEntity.category is MenuCategory.BeerCategory) {
            return DomainItem(
                alcPercentage = menuItemEntity.alcPercentage ?: 0.0,
                description = menuItemEntity.description,
                name = menuItemEntity.name,
                price = menuItemEntity.price,
                type = menuItemEntity.type,
                UID = menuItemEntity.UID,
                image = menuItemEntity.imageUrl,
                category = MenuCategory.BeerCategory,
                salePercentage = menuItemEntity.salePercentage
            )
        } else return DomainItem(
            alcPercentage = 0.0,
            description = menuItemEntity.description,
            name = menuItemEntity.name,
            price = menuItemEntity.price,
            type = menuItemEntity.type,
            UID = menuItemEntity.UID,
            image = menuItemEntity.imageUrl,
            category = MenuCategory.SnackCategory,
            weight = menuItemEntity.weight
        )
    }

    fun buildBeerEntityFromNetwork(data: BeerData): MenuItemEntity {
        return MenuItemEntity(
            UID = data.UID,
            description = data.description,
            name = data.name,
            price = data.price,
            type = data.type,
            alcPercentage = data.alcPercentage,
            category = MenuCategory.BeerCategory,
            salePercentage = data.salePercentage ?: 0.0,
            // imageUrl = data.imagePath?.replace("http://localhost:1611/", BASE_URL )
            imageUrl = BASE_URL.dropLast(4) + data.imagePath
        )
    }

    fun buildSnackEntityFromNetwork(data: SnackData): MenuItemEntity {
        return MenuItemEntity(
            UID = data.UID,
            description = data.description,
            name = data.name,
            price = data.price,
            type = data.type,
            alcPercentage = null,
            category = MenuCategory.SnackCategory,
            weight = data.weight ?: 100.0,
            //   imageUrl = data.imagePath?.replace("http://localhost:1611/", BASE_URL)
            imageUrl = BASE_URL.dropLast(4) + data.imagePath
        )
    }

    fun buildBeerEntityFromResponse(beerResponse: BeerResponse): MenuItemEntity {
        return MenuItemEntity(
            UID = beerResponse.createdBeverage.UID,
            description = beerResponse.createdBeverage.description,
            name = beerResponse.createdBeverage.name,
            price = beerResponse.createdBeverage.price,
            type = beerResponse.createdBeverage.type,
            alcPercentage = beerResponse.createdBeverage.alcPercentage,
            category = MenuCategory.BeerCategory,
            salePercentage = beerResponse.createdBeverage.salePercentage ?: 0.0,
            imageUrl = beerResponse.createdBeverage.imagePath?.replace(
                "http://localhost:1611/",
                BASE_URL
            )
        )
    }

    fun buildSnackEntityFromResponse(snackResponse: SnackResponse): MenuItemEntity {
        return MenuItemEntity(
            UID = snackResponse.createdSnack.UID,
            description = snackResponse.createdSnack.description,
            name = snackResponse.createdSnack.name,
            price = snackResponse.createdSnack.price,
            type = snackResponse.createdSnack.type,
            category = MenuCategory.SnackCategory,
            alcPercentage = null,
            weight = snackResponse.createdSnack.weight ?: 100.0,
            imageUrl = snackResponse.createdSnack.imagePath?.replace(
                "http://localhost:1611/",
                BASE_URL
            )
        )
    }
}