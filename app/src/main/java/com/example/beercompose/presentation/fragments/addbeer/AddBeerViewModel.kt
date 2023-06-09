package com.example.beercompose.presentation.fragments.addbeer

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beercompose.data.network.dto.beer.BeerRequest
import com.example.beercompose.data.network.dto.snack.SnackRequest
import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.domain.use_cases.menu_items.MenuItemsUseCases
import com.example.beercompose.presentation.fragments.addbeer.util.AddUpdateEvent
import com.example.beercompose.domain.use_cases.menu_validation.MenuValidationUseCases
import com.example.beercompose.presentation.fragments.addbeer.util.AddUpdateMode
import com.example.beercompose.presentation.fragments.addbeer.util.AddUpdateState
import com.example.beercompose.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class AddBeerViewModel @Inject constructor(
    private val menuItemsUseCases: MenuItemsUseCases,
    private val validationUseCases: MenuValidationUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(AddUpdateState())
    val state: State<AddUpdateState> = _state

    private val _response = MutableSharedFlow<String>()
    val response: SharedFlow<String> = _response

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id")
            if (id == "null") {
                val category =
                    MenuCategory.toMenuCategory(savedStateHandle.get<String>("category") ?: "")
                _state.value = state.value.copy(
                    mode = AddUpdateMode.ADD,
                    mainItem = state.value.mainItem.copy(category = category)
                )
            } else {
                val item = menuItemsUseCases.getMenuItemByIdUseCase(id ?: "")
                _state.value = state.value.copy(
                    mode = AddUpdateMode.UPDATE,
                    mainItem = item,
                    validationModel = state.value.validationModel.copy(
                        title = item.name,
                        description = item.description,
                        type = item.type,
                        price = String.format("%.2f", item.price),
                        alc = String.format("%.1f", item.alcPercentage),
                        salePercentage = item.salePercentage.toInt().toString(),
                        weight = String.format("%.2f", item.weight)
                    )
                )
            }
        }
    }

    fun onEvent(event: AddUpdateEvent) {
        when (event) {
            is AddUpdateEvent.ButtonClick -> {
                onButtonClick()
            }
            is AddUpdateEvent.TitleTextChanged -> {
                onTitleTextChanged(event.text)
            }
            is AddUpdateEvent.DescriptionTextChanged -> {
                onDescriptionTextChanged(event.text)
            }
            is AddUpdateEvent.TypeTextChanged -> {
                onTypeTextChanged(event.text)
            }
            is AddUpdateEvent.PriceTextChanged -> {
                onPriceTextChanged(event.text)
            }
            is AddUpdateEvent.AlcPercentageTextChanged -> {
                onAlcPercentageTextChanged(event.text)
            }
            is AddUpdateEvent.SalePercentageTextChanged -> {
                onSalePercentageTextChanged(event.text)
            }
            is AddUpdateEvent.WeightTextChanged -> {
                onWeightTextChanged(event.text)
            }
        }
    }

    private fun onButtonClick() = viewModelScope.launch {
        val mode = state.value.mode
        val category = state.value.mainItem.category
        if (mode == AddUpdateMode.ADD) {
            if (category == MenuCategory.BeerCategory) addBeer() else addSnack()
        } else {
            if (category == MenuCategory.BeerCategory) updateBeer() else updateSnack()
        }
    }

    private fun onTitleTextChanged(text: String) = viewModelScope.launch {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(title = text),
            addUpdateErrorFields = try {
                validationUseCases.titleValidationUseCase(text)
                state.value.addUpdateErrorFields.copy(titleError = "")
            } catch (e: EmptyFieldException) {
                state.value.addUpdateErrorFields.copy(titleError = e.message ?: "")
            }
        )
        enableButton()
    }

    private fun onDescriptionTextChanged(text: String) = viewModelScope.launch {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(description = text),
            addUpdateErrorFields = try {
                validationUseCases.descriptionValidationUseCase(text)
                state.value.addUpdateErrorFields.copy(descriptionError = "")
            } catch (e: EmptyFieldException) {
                state.value.addUpdateErrorFields.copy(descriptionError = e.message ?: "")
            }
        )
        enableButton()
    }

    private fun onTypeTextChanged(text: String) = viewModelScope.launch {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(type = text),
            addUpdateErrorFields = try {
                validationUseCases.typeValidationUseCase(text)
                state.value.addUpdateErrorFields.copy(typeError = "")
            } catch (e: EmptyFieldException) {
                state.value.addUpdateErrorFields.copy(typeError = e.message ?: "")
            }
        )
        enableButton()
    }

    private fun onPriceTextChanged(text: String) = viewModelScope.launch {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(price = text),
            addUpdateErrorFields = try {
                validationUseCases.priceValidationUseCase(text)
                state.value.addUpdateErrorFields.copy(priceError = "")
            } catch (e: InvalidPriceException) {
                state.value.addUpdateErrorFields.copy(priceError = e.message ?: "")
            }
        )
        enableButton()
    }

    private fun onAlcPercentageTextChanged(text: String) = viewModelScope.launch {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(alc = text),
            addUpdateErrorFields = try {
                validationUseCases.alcPercentageValidationUseCase(text)
                state.value.addUpdateErrorFields.copy(alcPercentageError = "")
            } catch (e: InvalidAlcPercentageException) {
                state.value.addUpdateErrorFields.copy(alcPercentageError = e.message ?: "")
            }
        )
        enableButton()
    }

    private fun onSalePercentageTextChanged(text: String) = viewModelScope.launch {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(salePercentage = text),
            addUpdateErrorFields = try {
                validationUseCases.salePercentageValidationUseCase(text)
                state.value.addUpdateErrorFields.copy(salePercentageError = "")
            } catch (e: InvalidSalePercentageException) {
                state.value.addUpdateErrorFields.copy(salePercentageError = e.message ?: "")
            }
        )
        enableButton()
    }

    private fun onWeightTextChanged(text: String) = viewModelScope.launch {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(weight = text),
            addUpdateErrorFields = try {
                validationUseCases.weightValidationUseCase(text)
                state.value.addUpdateErrorFields.copy(weightError = "")
            } catch (e: InvalidWeightException) {
                state.value.addUpdateErrorFields.copy(weightError = e.message ?: "")
            }
        )
        enableButton()
    }

    private fun enableButton() = viewModelScope.launch {
        _state.value = state.value.copy(
            isButtonEnabled = if (state.value.mainItem.category == MenuCategory.BeerCategory) {
                state.value.validationModel.title.isNotEmpty()
                        && state.value.validationModel.description.isNotEmpty()
                        && state.value.validationModel.type.isNotEmpty()
                        && state.value.validationModel.price.isNotEmpty()
                        && state.value.validationModel.alc.isNotEmpty()
                        && state.value.validationModel.salePercentage.isNotEmpty()
                        && state.value.addUpdateErrorFields.titleError.isEmpty()
                        && state.value.addUpdateErrorFields.descriptionError.isEmpty()
                        && state.value.addUpdateErrorFields.typeError.isEmpty()
                        && state.value.addUpdateErrorFields.priceError.isEmpty()
                        && state.value.addUpdateErrorFields.alcPercentageError.isEmpty()
                        && state.value.addUpdateErrorFields.salePercentageError.isEmpty()
            } else {
                state.value.validationModel.title.isNotEmpty()
                        && state.value.validationModel.description.isNotEmpty()
                        && state.value.validationModel.type.isNotEmpty()
                        && state.value.validationModel.price.isNotEmpty()
                        && state.value.validationModel.weight.isNotEmpty()
                        && state.value.addUpdateErrorFields.titleError.isEmpty()
                        && state.value.addUpdateErrorFields.descriptionError.isEmpty()
                        && state.value.addUpdateErrorFields.typeError.isEmpty()
                        && state.value.addUpdateErrorFields.priceError.isEmpty()
                        && state.value.addUpdateErrorFields.weightError.isEmpty()
            }
        )
    }

    private fun addBeer() = viewModelScope.launch(Dispatchers.IO) {
        val beerRequest = BeerRequest(
            name = state.value.validationModel.title,
            description = state.value.validationModel.description,
            type = state.value.validationModel.type,
            price = state.value.validationModel.price.toDouble(),
            alcPercentage = state.value.validationModel.alc.toDouble(),
            category = "",
            salePercentage = state.value.validationModel.salePercentage.toDouble(),
            imagePath = null
        )
        withContext(viewModelScope.coroutineContext) {
            val response = menuItemsUseCases.addBeerUseCase(beerRequest)
            _response.emit(response)
        }
    }

    private fun addSnack() = viewModelScope.launch(Dispatchers.IO) {
        val snackRequest = SnackRequest(
            name = state.value.validationModel.title,
            description = state.value.validationModel.description,
            type = state.value.validationModel.type,
            price = state.value.validationModel.price.toDouble(),
            weight = state.value.validationModel.weight.toDouble(),
            imagePath = null
        )
        launch {
            val response = async {
                menuItemsUseCases.addSnackUseCase(snackRequest)
            }
            _response.emit(response.await())
        }
    }

    private fun updateBeer() =
        viewModelScope.launch(Dispatchers.IO) {
            val beerId = state.value.mainItem.UID
            val image = menuItemsUseCases.getMenuItemByIdUseCase(beerId).image
            val beerRequest = BeerRequest(
                name = state.value.validationModel.title,
                description = state.value.validationModel.description,
                type = state.value.validationModel.type,
                price = state.value.validationModel.price.toDouble(),
                alcPercentage = state.value.validationModel.alc.toDouble(),
                category = "",
                salePercentage = state.value.validationModel.salePercentage.toDouble(),
                imagePath = image
            )
            val response = menuItemsUseCases.updateBeerUseCase(beerId, beerRequest)
            _response.emit(response)
        }

    private fun updateSnack() =
        viewModelScope.launch(Dispatchers.IO) {
            val snackId = state.value.mainItem.UID
            val snackRequest = SnackRequest(
                name = state.value.validationModel.title,
                description = state.value.validationModel.description,
                type = state.value.validationModel.type,
                price = state.value.validationModel.price.toDouble(),
                weight = state.value.validationModel.weight.toDouble(),
                imagePath = state.value.mainItem.image
            )
            val response = menuItemsUseCases.updateSnackUseCase(snackId, snackRequest)
            _response.emit(response)
        }
}