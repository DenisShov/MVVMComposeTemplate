package com.dshovhenia.mvvm.compose.template.domain.usecase

import app.cash.turbine.test
import com.dshovhenia.mvvm.compose.template.Constants
import com.dshovhenia.mvvm.compose.template.core.utils.BaseCoroutineTestWithTestDispatcherProvider
import com.dshovhenia.mvvm.compose.template.domain.error.AppError
import com.dshovhenia.mvvm.compose.template.domain.functional.DataResult
import com.dshovhenia.mvvm.compose.template.domain.repository.CoinsMarketsRepository
import com.dshovhenia.mvvm.compose.template.mocks.testCoinChartData
import com.dshovhenia.mvvm.compose.template.mocks.testResponseListFiltered
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class GetCoinChartUseCaseTest : BaseCoroutineTestWithTestDispatcherProvider() {
    private lateinit var useCase: GetCoinChartUseCase

    @MockK
    private lateinit var repository: CoinsMarketsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase =
            GetCoinChartUseCase(
                dispatcherProvider = testDispatcherProvider,
                coinsMarketsRepository = repository,
            )
    }

    @Test
    fun `test use case`() =
        runTest {
            coEvery { repository.getCoinChart(any(), any()) } returns testCoinChartData

            val id = "btc"
            val days = Constants.ONE_DAY

            useCase.launch(
                id = id,
                days = days,
            ).test {
                val firstItem = awaitItem()
                firstItem.shouldBeEqualTo(DataResult.Success(testResponseListFiltered))
                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) {
                repository.getCoinChart(
                    id = id,
                    days = days,
                )
            }
        }

    @Test
    fun `test use case handles errors`() =
        runTest {
            val exception = mockk<IllegalStateException>()
            coEvery { repository.getCoinChart(any(), any()) } throws exception

            val id = "btc"
            val days = Constants.ONE_DAY

            useCase.launch(
                id = id,
                days = days,
            ).test {
                val error = awaitItem()
                error.shouldBeEqualTo(DataResult.Failure(AppError.GeneralError(exception)))
                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) {
                repository.getCoinChart(
                    id = id,
                    days = days,
                )
            }
        }
}
