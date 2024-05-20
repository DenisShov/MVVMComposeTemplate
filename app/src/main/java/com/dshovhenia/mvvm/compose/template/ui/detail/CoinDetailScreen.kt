package com.dshovhenia.mvvm.compose.template.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import com.dshovhenia.mvvm.compose.template.Constants
import com.dshovhenia.mvvm.compose.template.Constants.COIN_DETAIL_PARAMETER
import com.dshovhenia.mvvm.compose.template.R
import com.dshovhenia.mvvm.compose.template.core.extension.correspondingTrendColor
import com.dshovhenia.mvvm.compose.template.core.extension.formatPrice
import com.dshovhenia.mvvm.compose.template.core.extension.toErrorMessage
import com.dshovhenia.mvvm.compose.template.core.extension.toLocalDateTime
import com.dshovhenia.mvvm.compose.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.compose.template.theme.ChartFillColor
import com.dshovhenia.mvvm.compose.template.theme.StocksDarkBackgroundTranslucent
import com.dshovhenia.mvvm.compose.template.theme.StocksDarkPrimaryText
import com.dshovhenia.mvvm.compose.template.theme.StocksDarkSecondaryText
import com.dshovhenia.mvvm.compose.template.ui.base.Screen
import com.dshovhenia.mvvm.compose.template.ui.list.CoinIcon
import com.dshovhenia.mvvm.compose.template.ui.widget.chart.CryptoChart
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import dev.olshevski.navigation.reimagined.pop
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
coinMarkets: CoinMarkets,
navController: NavController<Screen>,
viewModel: CoinDetailViewModel =
        hiltViewModel(
            defaultArguments = bundleOf(COIN_DETAIL_PARAMETER to coinMarkets),
        ),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {}, navigationIcon = {
                IconButton(onClick = { navController.pop() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Return to previous screen",
                        modifier = Modifier.padding(start = 12.dp),
                    )
                }
            })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->

        LazyColumn(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            item {
                Header(coinMarkets)
                Spacer(modifier = Modifier.size(16.dp))
            }

            item {
                Chart(uiState, snackbarHostState, viewModel)
            }

            item {
                PeriodSelector(viewModel)
            }

            item {
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Market Data",
                    modifier =
                    Modifier
                        .padding(16.dp)
                        .semantics { heading() },
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Medium,
                    color = StocksDarkPrimaryText,
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            item {
                Column(
                    modifier =
                    Modifier
                        .padding(horizontal = 8.dp)
                        .background(
                            color = StocksDarkBackgroundTranslucent,
                            shape = MaterialTheme.shapes.large,
                        ),
                ) {
                    val list = getCoinMarketUiData(coinMarkets)
                    list.forEachIndexed { index, pair ->
                        SectionInfoItem(
                            name = pair.first,
                            value = pair.second,
                            showDivider = index != list.lastIndex,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Chart(
uiState: CoinDetailViewModel.State,
snackbarHostState: SnackbarHostState,
viewModel: CoinDetailViewModel,
) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .requiredHeight(190.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End,
    ) {
        when (val state = uiState.coinChart) {
            is CoinDetailViewModel.CoinChartState.Loading -> {
                AnimatedVisibility(
                    visible = state.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    LoadingItem(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        text = "Loading chart data...",
                    )
                }
            }

            is CoinDetailViewModel.CoinChartState.Success -> {
                CryptoChart(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(8.dp),
                    data = state.coinChartData,
                    graphColor = state.priceChangePercentage.correspondingTrendColor(),
                    showDashedLine = true,
                )
            }

            is CoinDetailViewModel.CoinChartState.Error -> {
                Text(
                    text = "Error loading chart data.",
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding(16.dp),
                    color = StocksDarkPrimaryText,
                    textAlign = TextAlign.Center,
                )

                LaunchedEffect(snackbarHostState) {
                    val result =
                        snackbarHostState.showSnackbar(
                            message = state.error.toErrorMessage(),
                            actionLabel = "Retry",
                            withDismissAction = true,
                            duration = SnackbarDuration.Indefinite,
                        )

                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.getCryptoChart()
                    }
                }
            }
        }
    }
}

@Composable
fun PeriodSelector(viewModel: CoinDetailViewModel) {
    val options =
        listOf(
            Pair("1H", Constants.ONE_HOUR),
            Pair("24H", Constants.ONE_DAY),
            Pair("7D", Constants.ONE_WEEK),
            Pair("1M", Constants.ONE_MONTH),
            Pair("3M", Constants.ONE_QUARTER),
            Pair("1Y", Constants.ONE_YEAR),
            Pair("All time", Constants.MAX),
        )
    var selectedOption by remember { mutableStateOf(options[1]) }
    val onSelectionChange = { pair: Pair<String, String> ->
        selectedOption = pair
        viewModel.getCryptoChart(pair.second)
    }

    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(20.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        options.forEach { pair ->
            Text(
                text = pair.first,
                color = Color.Black,
                modifier =
                Modifier
                    .clip(shape = RoundedCornerShape(size = 12.dp))
                    .clickable {
                        onSelectionChange(pair)
                    }
                    .background(
                        if (pair == selectedOption) {
                            ChartFillColor
                        } else {
                            Color.White
                        },
                    )
                    .padding(8.dp),
            )
        }
    }
}

@Composable
fun SectionInfoItem(
name: String,
value: String,
showDivider: Boolean,
) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = name,
            color = StocksDarkSecondaryText,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            color = StocksDarkPrimaryText,
            style = MaterialTheme.typography.bodyMedium,
        )
    }

    if (showDivider) {
        Divider(
            modifier =
            Modifier
                .padding(horizontal = 8.dp)
                .alpha(Constants.DIVIDER_ALPHA),
            color = StocksDarkSecondaryText,
        )
    }
}

@Composable
fun PriceText(
modifier: Modifier,
price: String?,
) {
    Text(
        modifier =
        modifier.alpha(
            alpha = if (price == null) 0f else 1f,
        ),
        textAlign = TextAlign.End,
        text = price ?: "000000000",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Medium,
        color = StocksDarkPrimaryText,
        maxLines = 1,
    )
}

@Composable
fun Header(coinMarkets: CoinMarkets) {
    Row(
        modifier =
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CoinIcon(
            imageUrl = coinMarkets.image,
            size = 50.dp,
            shape = MaterialTheme.shapes.large,
        )

        Spacer(modifier = Modifier.size(16.dp))

        Column {
            Text(
                text = coinMarkets.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = StocksDarkPrimaryText,
            )
            Text(
                text = coinMarkets.symbol,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = StocksDarkSecondaryText,
            )
        }

        PriceText(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            price = coinMarkets.currentPrice.formatPrice(),
        )
    }
}

fun getCoinMarketUiData(coinMarkets: CoinMarkets): List<Pair<String, String>> {
    return with(coinMarkets) {
        listOf(
            Pair("Market Cap", marketCap.formatPrice().orNotAvailable()),
            Pair("Trading Volume 24h", totalVolume.formatPrice().orNotAvailable()),
            Pair("Highest Price 24h", high24h.formatPrice().orNotAvailable()),
            Pair("Lowest Price 24h", low24h.formatPrice().orNotAvailable()),
            Pair(
                "Available Supply",
                circulatingSupply.formatPrice(withoutSymbol = true).orNotAvailable(),
            ),
            Pair(
                "Total Supply",
                totalSupply.formatPrice(withoutSymbol = true).orNotAvailable(),
            ),
            Pair(
                "Max Supply",
                totalSupply.formatPrice(withoutSymbol = true).orNotAvailable(),
            ),
            Pair("All-Time High Price", ath.formatPrice().orNotAvailable()),
            Pair(
                "All-Time High Date",
                athDate.toLocalDateTime()?.format(provideDateFormatter()).orNotAvailable(),
            ),
            Pair("All-Time Low Price", atl.formatPrice().orNotAvailable()),
            Pair(
                "All-Time Low Date",
                atlDate.toLocalDateTime()?.format(provideDateFormatter()).orNotAvailable(),
            ),
            Pair(
                "Last updated",
                lastUpdated.toLocalDateTime()?.format(provideDateTimeFormatter()).orNotAvailable(),
            ),
        )
    }
}

private fun provideDateFormatter() = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.US)

private fun provideDateTimeFormatter() = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

fun String?.orNotAvailable() = this ?: "Not available"
