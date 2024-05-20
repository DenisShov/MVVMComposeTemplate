package com.dshovhenia.mvvm.compose.template.domain.usecase

import com.dshovhenia.mvvm.compose.template.core.utils.DispatcherProvider
import com.dshovhenia.mvvm.compose.template.domain.functional.DataResult
import com.dshovhenia.mvvm.compose.template.domain.functional.asDataResult
import com.dshovhenia.mvvm.compose.template.domain.repository.CoinsMarketsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.math.BigDecimal
import javax.inject.Inject

class GetCoinChartUseCase
@Inject
constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val coinsMarketsRepository: CoinsMarketsRepository,
) {
    suspend fun launch(
    id: String,
    days: String,
    ): Flow<DataResult<List<BigDecimal>>> {
        return flow {
            emit(
                coinsMarketsRepository.getCoinChart(id, days)
                    // get only the price, without the timeline
                    .prices.flatten().filterIndexed { index, _ -> index % 2 == 1 },
            )
        }
            .asDataResult()
            .flowOn(dispatcherProvider.io)
    }
}
