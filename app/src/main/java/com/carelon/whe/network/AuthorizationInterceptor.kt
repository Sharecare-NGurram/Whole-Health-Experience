package com.carelon.whe.network

import com.carelon.whe.domain.prefs.AppPreferenceHelper
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val preferenceHelper: AppPreferenceHelper): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", preferenceHelper.accessToken ?: "")
            .build()

        return chain.proceed(request)
    }
}