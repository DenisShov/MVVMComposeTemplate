package com.dshovhenia.mvvm.compose.template.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshovhenia.mvvm.compose.template.Constants
import com.dshovhenia.mvvm.compose.template.data.entity.CoinChartData
import com.dshovhenia.mvvm.compose.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.compose.template.domain.error.AppError
import com.dshovhenia.mvvm.compose.template.domain.functional.fold
import com.dshovhenia.mvvm.compose.template.domain.usecase.GetCoinChartUseCase
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
class CoinDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCoinChartUseCase: GetCoinChartUseCase,
) : ViewModel() {
    private val coinDetailMainUiData: CoinMarkets
        get() =
            savedStateHandle[Constants.COIN_DETAIL_PARAMETER]
                ?: throw IllegalStateException(
                    "CoinDetailViewModel must be provided with " +
                            "${Constants.COIN_DETAIL_PARAMETER} parameter.",
                )

    private val _uiState = MutableStateFlow(State(coinChart = CoinChartState.Loading()))
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    data class State(
        val coinChart: CoinChartState,
    )

    sealed interface CoinChartState {
        data class Loading(val isLoading: Boolean = true) : CoinChartState

        data class Success(val coinChartData: CoinChartData) : CoinChartState

        data class Error(val error: AppError) : CoinChartState
    }

    init {
        getCryptoChart(Constants.ONE_DAY)
    }

    private var chartViewDays: String? = null

    fun getCryptoChart(days: String) {
        chartViewDays = days
        getCryptoChart()
    }

    fun getCryptoChart() {
        chartViewDays?.let {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(coinChart = CoinChartState.Loading(true))
                }

                getCoinChartUseCase.launch(coinDetailMainUiData.id, it)
                    .onEach { result ->
                        _uiState.update {
                            it.copy(coinChart = CoinChartState.Loading(false))
                        }

                        result.fold(
                            onSuccess = { cryptoChartData ->
                                _uiState.update {
                                    it.copy(coinChart = CoinChartState.Success(cryptoChartData))
                                }
                            },
                            onFailure = { error ->
                                _uiState.update {
                                    it.copy(coinChart = CoinChartState.Error(error))
                                }
                            },
                        )
                    }
                    .launchIn(viewModelScope)
            }
        }
    }
}
