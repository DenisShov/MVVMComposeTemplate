package com.dshovhenia.mvvm.compose.template.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dshovhenia.mvvm.compose.template.core.extension.formatPrice
import com.dshovhenia.mvvm.compose.template.core.extension.formatToPercentage
import com.dshovhenia.mvvm.compose.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.compose.template.theme.AppTheme
import com.dshovhenia.mvvm.compose.template.theme.NegativeTrend
import com.dshovhenia.mvvm.compose.template.theme.PositiveTrend
import com.dshovhenia.mvvm.compose.template.theme.StocksDarkPrimaryText
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinWithMarketDataItem(
    modifier: Modifier = Modifier,
    item: CoinMarkets,
    onCoinItemClick: () -> Unit,
) {
    Card(
        modifier = modifier.wrapContentHeight(),
        onClick = {
            onCoinItemClick()
        },
        colors =
        CardDefaults.cardColors(
            contentColor = StocksDarkPrimaryText,
        ),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier =
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Timber.d("CoinItem card recomposition ${item.symbol}")

            CoinIcon(imageUrl = item.image)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.symbol,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Box(
                modifier = Modifier,
                contentAlignment = Alignment.CenterEnd,
            ) {
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        modifier =
                        Modifier
                            .width(IntrinsicSize.Max),
                        text = item.currentPrice.formatPrice(),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                    )

                    Spacer(modifier = Modifier.padding(1.dp))

                    Card(
                        modifier =
                        Modifier
                            .requiredWidth(72.dp),
                        shape = MaterialTheme.shapes.small,
                        colors =
                        CardDefaults.cardColors(
                            containerColor =
                            if (item.priceChangePercentage24h > 0) {
                                PositiveTrend
                            } else {
                                NegativeTrend
                            },
                            contentColor = Color.White,
                        ),
                    ) {
                        Text(
                            text = item.priceChangePercentage24h.formatToPercentage(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier =
                            Modifier
                                .padding(horizontal = 8.dp, vertical = 1.dp)
                                .align(Alignment.End),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CoinIcon(
    modifier: Modifier = Modifier,
    imageUrl: String,
    size: Dp = 30.dp,
    shape: Shape = MaterialTheme.shapes.medium,
) {
    AsyncImage(
        model = imageUrl,
        modifier =
        modifier
            .size(size)
            .clip(shape = shape),
        contentDescription = null,
    )
}

@Preview(showBackground = true)
@Composable
private fun CoinWithMarketDataItemPreview() {
    AppTheme {
        CoinWithMarketDataItem(
            item =
            CoinMarkets(
                id = "",
                name = "Bitcoin",
                symbol = "BTC",
                image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
                marketCapRank = 1,
                currentPrice = 1000000.0,
                priceChangePercentage24h = 3.45,
            ),
            onCoinItemClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CoinIconPreview() {
    CoinIcon(imageUrl = "")
}
