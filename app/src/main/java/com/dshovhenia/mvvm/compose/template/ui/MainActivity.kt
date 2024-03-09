package com.dshovhenia.mvvm.compose.template.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dshovhenia.mvvm.compose.template.theme.AppTheme
import com.dshovhenia.mvvm.compose.template.ui.base.Screen
import com.dshovhenia.mvvm.compose.template.ui.detail.CoinDetailScreen
import com.dshovhenia.mvvm.compose.template.ui.list.CoinsListScreen
import dagger.hilt.android.AndroidEntryPoint
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.rememberNavController

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val navController =
                    rememberNavController<Screen>(
                        startDestination = Screen.CoinsList,
                    )

                NavBackHandler(navController)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                ) {
                    Surface(modifier = Modifier.padding(it)) {
                        NavHost(
                            controller = navController,
                        ) { route ->
                            when (route) {
                                is Screen.CoinsList -> {
                                    CoinsListScreen(
                                        navController = navController,
                                    )
                                }

                                is Screen.CoinDetail -> {
                                    CoinDetailScreen(
                                        coinMarkets = route.coinMarkets,
                                        navController = navController,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
