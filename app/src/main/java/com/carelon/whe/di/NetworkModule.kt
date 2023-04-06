package com.carelon.whe.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.carelon.whe.BuildConfig
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.network.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // TODO: Need to add graphql url
    private val baseUrl: String by lazy { BuildConfig.BASE_URL }

    private const val READ_TIMEOUT_SECONDS = 60
    private const val WRITE_TIMEOUT_SECONDS = 60
    private const val CONNECT_TIMEOUT_SECONDS = 10

    @Singleton
    @Provides
    fun provideOkHttpClient(authorizationInterceptor: AuthorizationInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(authorizationInterceptor)
            connectTimeout(CONNECT_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        }
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideAuthorizationInterceptor(preferenceHelper: AppPreferenceHelper): AuthorizationInterceptor {
        return AuthorizationInterceptor(preferenceHelper)
    }


    @Singleton
    @Provides
    fun provideApolloClient(
        okHttpClient: OkHttpClient
    ): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(baseUrl)
            .okHttpClient(okHttpClient)
            .build()
    }

}