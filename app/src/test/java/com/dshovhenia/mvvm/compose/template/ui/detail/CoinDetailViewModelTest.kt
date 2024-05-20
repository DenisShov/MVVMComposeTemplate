package com.dshovhenia.mvvm.compose.template.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.dshovhenia.mvvm.compose.template.Constants
import com.dshovhenia.mvvm.compose.template.Constants.COIN_DETAIL_PARAMETER
import com.dshovhenia.mvvm.compose.template.core.utils.BaseCoroutineTestWithInstantTaskExecutorRule
import com.dshovhenia.mvvm.compose.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.compose.template.domain.error.AppError
import com.dshovhenia.mvvm.compose.template.domain.functional.DataResult
import com.dshovhenia.mvvm.compose.template.domain.usecase.GetCoinChartUseCase
import com.dshovhenia.mvvm.compose.template.mocks.testCoinMarkets
import com.dshovhenia.mvvm.compose.template.mocks.testPriceChangePercentage
import com.dshovhenia.mvvm.compose.template.mocks.testResponseListFiltered
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoinDetailViewModelTest : BaseCoroutineTestWithInstantTaskExecutorRule() {
    private lateinit var vieModel: CoinDetailViewModel

    @MockK
    private lateinit var getCoinChartUseCase: GetCoinChartUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        vieModel =
            CoinDetailViewModel(
                savedStateHandle = savedStateHandle,
                getCoinChartUseCase = getCoinChartUseCase,
            )
    }

    @Test
    fun `test getCryptoChart`() =
        runTest {
            coEvery { savedStateHandle.get<CoinMarkets>(COIN_DETAIL_PARAMETER) } returns testCoinMarkets

            coEvery { getCoinChartUseCase.launch(any(), any()) } returns
                    flow {
                        emit(DataResult.Success(testResponseListFiltered))
                    }

            val days = Constants.ONE_DAY

            vieModel.getCryptoChart(days)

            vieModel.uiState.test {
                awaitItem().shouldBeEqualTo(CoinDetailViewModel.State(CoinDetailViewModel.CoinChartState.Loading(true)))
                awaitItem().shouldBeEqualTo(CoinDetailViewModel.State(CoinDetailViewModel.CoinChartState.Loading(false)))
                awaitItem().shouldBeEqualTo(CoinDetailViewModel.State(CoinDetailViewModel.CoinChartState.Success(testResponseListFiltered, testPriceChangePercentage)))

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `test getCryptoChart handles error`() =
        runTest {
            coEvery { savedStateHandle.get<CoinMarkets>(COIN_DETAIL_PARAMETER) } returns testCoinMarkets

            val exception = mockk<IllegalStateException>()
            coEvery { getCoinChartUseCase.launch(any(), any()) } returns
                    flow {
                        emit(DataResult.Failure(AppError.GeneralError(exception)))
                    }

            val days = Constants.ONE_DAY

            vieModel.getCryptoChart(days)

            vieModel.uiState.test {
                awaitItem().shouldBeEqualTo(CoinDetailViewModel.State(CoinDetailViewModel.CoinChartState.Loading(true)))
                awaitItem().shouldBeEqualTo(CoinDetailViewModel.State(CoinDetailViewModel.CoinChartState.Loading(false)))
                awaitItem().shouldBeEqualTo(CoinDetailViewModel.State(CoinDetailViewModel.CoinChartState.Error(AppError.GeneralError(exception))))

                cancelAndConsumeRemainingEvents()
            }
        }
}
