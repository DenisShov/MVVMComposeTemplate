package com.dshovhenia.mvvm.compose.template.domain.usecase

import com.dshovhenia.mvvm.compose.template.core.utils.DispatcherProvider
import com.dshovhenia.mvvm.compose.template.data.entity.CoinChartData
import com.dshovhenia.mvvm.compose.template.domain.functional.DataResult
import com.dshovhenia.mvvm.compose.template.domain.functional.asDataResult
import com.dshovhenia.mvvm.compose.template.domain.repository.CoinsMarketsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
    ): Flow<DataResult<CoinChartData>> {
        return flow {
            emit(coinsMarketsRepository.getCoinChart(id, days))
        }
            .asDataResult()
            .flowOn(dispatcherProvider.io)
    }
}
