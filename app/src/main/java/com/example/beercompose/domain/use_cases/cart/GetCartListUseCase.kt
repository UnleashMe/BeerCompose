package com.example.beercompose.domain.use_cases.cart

import com.example.beercompose.domain.model.InCartItem
import com.example.beercompose.data.database.entities.UserAndItemsInCart
import com.example.beercompose.domain.repositories.CartRepository
import com.example.beercompose.util.mapper.MenuItemMapper

class GetCartListUseCase(
    private val beerMapper: MenuItemMapper,
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(userAndItems: UserAndItemsInCart?): List<InCartItem> {

        if (userAndItems == null) return emptyList()
        val list = mutableListOf<InCartItem>()

        userAndItems.menuItems.map { entity ->

            beerMapper.buildDomainFromEntity(entity)
        }.sortedBy { it.category.name }.forEach { item ->
            list.add(
                InCartItem(
                    item.UID,
                    item.name,
                    price = if (item.salePercentage == 0.0) item.price else item.discountedPrice,
                    cartRepository.getItemInCart(
                        userAndItems.user.phoneNumber,
                        item.UID
                    )!!.quantity,
                    item.image
                )
            )
        }
        return list
    }
}