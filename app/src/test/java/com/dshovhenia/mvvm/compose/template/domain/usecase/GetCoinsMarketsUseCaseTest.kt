package com.dshovhenia.mvvm.compose.template.domain.usecase

import app.cash.turbine.test
import com.dshovhenia.mvvm.compose.template.core.utils.BaseCoroutineTestWithTestDispatcherProvider
import com.dshovhenia.mvvm.compose.template.domain.error.AppError
import com.dshovhenia.mvvm.compose.template.domain.functional.DataResult
import com.dshovhenia.mvvm.compose.template.domain.repository.CoinsMarketsRepository
import com.dshovhenia.mvvm.compose.template.mocks.testCoinMarketsList
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCoinsMarketsUseCaseTest : BaseCoroutineTestWithTestDispatcherProvider() {
private lateinit var useCase: GetCoinsMarketsUseCase

    @MockK
    private lateinit var repository: CoinsMarketsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase =
        GetCoinsMarketsUseCase(
            dispatcherProvider = testDispatcherProvider,
            coinsMarketsRepository = repository,
        )
    }

    @Test
    fun `test use case`() =
    runTest {
        coEvery { repository.getCoinsMarkets() } returns testCoinMarketsList

        useCase.launch().test {
            val firstItem = awaitItem()
            firstItem.shouldBeEqualTo(DataResult.Success(testCoinMarketsList))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            repository.getCoinsMarkets()
        }
    }

    @Test
    fun `test use case handles errors`() =
    runTest {
        val exception = mockk<IllegalStateException>()
        coEvery { repository.getCoinsMarkets() } throws exception

        useCase.launch().test {
            val firstItem = awaitItem()
            firstItem.shouldBeEqualTo(DataResult.Failure(AppError.GeneralError(exception)))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            repository.getCoinsMarkets()
        }
    }
}
