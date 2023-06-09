package com.example.beercompose.domain.use_cases.likes

import com.example.beercompose.domain.repositories.LikesRepository

class GetItemLikesByIdUseCase(
    private val repository: LikesRepository
) {
    operator fun invoke(id: String) = repository.getItemLikesById(id)
}