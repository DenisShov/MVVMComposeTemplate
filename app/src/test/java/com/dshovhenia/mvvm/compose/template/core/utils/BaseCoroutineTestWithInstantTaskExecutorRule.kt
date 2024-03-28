package com.dshovhenia.mvvm.compose.template.core.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseCoroutineTestWithInstantTaskExecutorRule(dispatcher: TestDispatcher = StandardTestDispatcher()) : BaseCoroutineTestWithTestDispatcherProvider(dispatcher) {
@get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
}
