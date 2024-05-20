package com.dshovhenia.mvvm.compose.template.core.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.junit.Rule

abstract class BaseCoroutineTestWithInstantTaskExecutorRule(dispatcher: TestDispatcher = StandardTestDispatcher()) : BaseCoroutineTestWithTestDispatcherProvider(dispatcher) {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
}
