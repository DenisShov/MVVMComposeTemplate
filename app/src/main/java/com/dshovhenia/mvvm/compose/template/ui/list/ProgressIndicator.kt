package com.dshovhenia.mvvm.compose.template.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun ProgressIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier =
        modifier
            .fillMaxSize()
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}
