package com.dshovhenia.mvvm.compose.template.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshovhenia.mvvm.compose.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.compose.template.domain.error.AppError
import com.dshovhenia.mvvm.compose.template.domain.functional.fold
import com.dshovhenia.mvvm.compose.template.domain.usecase.GetCoinsMarketsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinsListViewModel
@Inject
constructor(
    private val getCoinsMarketsUseCase: GetCoinsMarketsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(State(coinsList = CoinsListState.Loading()))
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    data class State(
        val coinsList: CoinsListState,
    )

    sealed interface CoinsListState {
        data class Loading(val isLoading: Boolean = true) : CoinsListState

        data class Success(val coinMarkets: List<CoinMarkets>) : CoinsListState

        data class Error(val error: AppError) : CoinsListState
    }

    fun getCoinsMarkets() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(coinsList = CoinsListState.Loading(true))
            }
            getCoinsMarketsUseCase.launch().onEach { result ->
                _uiState.update {
                    it.copy(coinsList = CoinsListState.Loading(false))
                }

                result.fold(onSuccess = { coinMarkets ->
                    _uiState.update {
                        it.copy(coinsList = CoinsListState.Success(coinMarkets = coinMarkets))
                    }
                }, onFailure = { error ->
                    _uiState.update {
                        it.copy(coinsList = CoinsListState.Error(error))
                    }
                })
            }.launchIn(viewModelScope)
        }
    }
}
