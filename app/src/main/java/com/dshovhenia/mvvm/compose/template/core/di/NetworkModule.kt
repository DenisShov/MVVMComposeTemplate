package com.dshovhenia.mvvm.compose.template.core.di

import com.dshovhenia.mvvm.compose.template.BuildConfig
import com.dshovhenia.mvvm.compose.template.core.utils.DefaultDispatcherProvider
import com.dshovhenia.mvvm.compose.template.core.utils.DispatcherProvider
import com.dshovhenia.mvvm.compose.template.data.network.ApiErrorInterceptor
import com.dshovhenia.mvvm.compose.template.data.network.CoinGeckoApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideIODispatcher(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addLoggingInterceptor(BuildConfig.DEBUG)
            .addInterceptor(ApiErrorInterceptor())
            .build()
    }

    private fun OkHttpClient.Builder.addLoggingInterceptor(isLogEnabled: Boolean) =
        apply {
            if (!isLogEnabled) {
                return@apply
            }
            val loggingInterceptor =
                HttpLoggingInterceptor { message -> Timber.i(message) }
                    .apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }

            addInterceptor(loggingInterceptor)
        }

    @Provides
    @Singleton
    fun provideCoinGeckoApi(
    okHttpClient: OkHttpClient,
    gson: Gson,
    ): CoinGeckoApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CoinGeckoApi::class.java)
    }
}
