package com.dshovhenia.mvvm.compose.template.core.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseCoroutineTestWithTestDispatcherProvider(
    dispatcher: TestDispatcher = StandardTestDispatcher(),
) {
@get:Rule
    val mainDispatcherRule = MainDispatcherRule(dispatcher = dispatcher)

    /**
     * DispatcherProvider to be provided as dependency to mocked classes.
     */
    protected val testDispatcherProvider = TestDispatcherProvider(testDispatcher = dispatcher)
}
