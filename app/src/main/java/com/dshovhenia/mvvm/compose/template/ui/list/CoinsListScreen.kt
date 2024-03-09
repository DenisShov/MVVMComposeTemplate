package com.dshovhenia.mvvm.compose.template.ui.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dshovhenia.mvvm.compose.template.R
import com.dshovhenia.mvvm.compose.template.core.extension.toErrorMessage
import com.dshovhenia.mvvm.compose.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.compose.template.ui.base.Screen
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import dev.olshevski.navigation.reimagined.navigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinsListScreen(
navController: NavController<Screen>,
viewModel: CoinsListViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getCoinsMarkets()
    }

    val coinsListState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    val goToCoinDetail: (CoinMarkets) -> Unit = {
        navController.navigate(Screen.CoinDetail(coinMarkets = it))
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier, snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = {
        TopAppBar(title = {
            PoweredByCoinGeckoText()
        })
    }) { innerPadding ->

        when (val state = uiState.coinsList) {
            is CoinsListViewModel.CoinsListState.Loading -> {
                AnimatedVisibility(
                    visible = state.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    ProgressIndicator()
                }
            }

            is CoinsListViewModel.CoinsListState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = coinsListState,
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(count = state.coinMarkets.size) { index ->
                        CoinWithMarketDataItem(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            item = state.coinMarkets[index],
                            onCoinItemClick = {
                                goToCoinDetail(state.coinMarkets[index])
                            },
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.size(32.dp))
                    }
                }
            }

            is CoinsListViewModel.CoinsListState.Error -> {
                LaunchedEffect(snackbarHostState) {
                    val result =
                        snackbarHostState.showSnackbar(
                            message = state.error.toErrorMessage(),
                            actionLabel = "Retry",
                            withDismissAction = true,
                            duration = SnackbarDuration.Indefinite,
                        )

                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.getCoinsMarkets()
                    }
                }
            }
        }
    }
}

@Composable
private fun PoweredByCoinGeckoText(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            text = "Powered by ",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodySmall,
        )
        Image(
            modifier =
            Modifier
                .requiredHeight(20.dp)
                .padding(top = 2.dp),
            painter = painterResource(id = R.drawable.ic_coingecko),
            contentDescription = null,
        )
    }
}
