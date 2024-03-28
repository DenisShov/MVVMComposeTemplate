package com.dshovhenia.mvvm.compose.template.ui.list

import app.cash.turbine.test
import com.dshovhenia.mvvm.compose.template.core.utils.BaseCoroutineTestWithInstantTaskExecutorRule
import com.dshovhenia.mvvm.compose.template.domain.error.AppError
import com.dshovhenia.mvvm.compose.template.domain.functional.DataResult
import com.dshovhenia.mvvm.compose.template.domain.usecase.GetCoinsMarketsUseCase
import com.dshovhenia.mvvm.compose.template.mocks.testCoinMarketsList
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
class CoinsListViewModelTest : BaseCoroutineTestWithInstantTaskExecutorRule() {
private lateinit var vieModel: CoinsListViewModel

    @MockK
    private lateinit var getCoinsMarketsUseCase: GetCoinsMarketsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        vieModel = CoinsListViewModel(getCoinsMarketsUseCase = getCoinsMarketsUseCase)
    }

    @Test
    fun `test getCoinsMarkets`() =
    runTest {
        coEvery { getCoinsMarketsUseCase.launch() } returns
        flow {
            emit(DataResult.Success(testCoinMarketsList))
        }

        vieModel.getCoinsMarkets()

        vieModel.uiState.test {
            awaitItem().shouldBeEqualTo(CoinsListViewModel.State(CoinsListViewModel.CoinsListState.Loading(true)))
            awaitItem().shouldBeEqualTo(CoinsListViewModel.State(CoinsListViewModel.CoinsListState.Loading(false)))
            awaitItem().shouldBeEqualTo(CoinsListViewModel.State(CoinsListViewModel.CoinsListState.Success(testCoinMarketsList)))

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `test getCoinsMarkets handles error`() =
    runTest {
        val exception = mockk<IllegalStateException>()
        coEvery { getCoinsMarketsUseCase.launch() } returns
        flow {
            emit(DataResult.Failure(AppError.GeneralError(exception)))
        }

        vieModel.getCoinsMarkets()

        vieModel.uiState.test {
            awaitItem().shouldBeEqualTo(CoinsListViewModel.State(CoinsListViewModel.CoinsListState.Loading(true)))
            awaitItem().shouldBeEqualTo(CoinsListViewModel.State(CoinsListViewModel.CoinsListState.Loading(false)))
            awaitItem().shouldBeEqualTo(CoinsListViewModel.State(CoinsListViewModel.CoinsListState.Error(AppError.GeneralError(exception))))

            cancelAndConsumeRemainingEvents()
        }
    }
}
