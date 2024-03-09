package com.dshovhenia.mvvm.compose.template.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dshovhenia.mvvm.compose.template.theme.StocksDarkPrimaryText

@Composable
fun LoadingItem(
modifier: Modifier,
text: String = "Loading data...",
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        modifier = modifier,
    ) {
        CircularProgressIndicator(color = StocksDarkPrimaryText)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = StocksDarkPrimaryText,
        )
    }
}
