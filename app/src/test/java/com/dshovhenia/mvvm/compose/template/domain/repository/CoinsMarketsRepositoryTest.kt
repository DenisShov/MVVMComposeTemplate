package com.dshovhenia.mvvm.compose.template.domain.repository

import com.dshovhenia.mvvm.compose.template.Constants
import com.dshovhenia.mvvm.compose.template.core.utils.BaseCoroutineTestWithTestDispatcherProvider
import com.dshovhenia.mvvm.compose.template.data.store.GetCoinChartStore
import com.dshovhenia.mvvm.compose.template.data.store.GetCoinsMarketsStore
import com.dshovhenia.mvvm.compose.template.mocks.testCoinChartData
import com.dshovhenia.mvvm.compose.template.mocks.testCoinMarketsList
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class CoinsMarketsRepositoryTest : BaseCoroutineTestWithTestDispatcherProvider() {
    private lateinit var repository: CoinsMarketsRepository

    @MockK
    private lateinit var getCoinsMarketsStore: GetCoinsMarketsStore

    @MockK
    private lateinit var getCoinChartStore: GetCoinChartStore

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        repository =
            CoinsMarketsRepository(
                getCoinsMarketsStore = getCoinsMarketsStore, getCoinChartStore = getCoinChartStore,
            )
    }

    @Test
    fun `test getCoinsMarkets`() =
        runTest {
            coEvery { getCoinsMarketsStore.getCoinsMarkets() } returns testCoinMarketsList

            val coinMarkets = repository.getCoinsMarkets()

            coinMarkets.shouldBeEqualTo(testCoinMarketsList)

            coVerify(exactly = 1) {
                getCoinsMarketsStore.getCoinsMarkets()
            }
        }

    @Test
    fun `test getCoinChart`() =
        runTest {
            coEvery { getCoinChartStore.getCoinChart(any(), any()) } returns testCoinChartData

            val id = "btc"
            val days = Constants.ONE_DAY

            val coinMarkets = repository.getCoinChart(id, days)

            coinMarkets.shouldBeEqualTo(testCoinChartData)

            coVerify(exactly = 1) {
                getCoinChartStore.getCoinChart(id, days)
            }
        }
}
