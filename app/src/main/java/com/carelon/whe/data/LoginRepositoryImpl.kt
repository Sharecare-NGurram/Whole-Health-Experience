package com.carelon.whe.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.UserNeedsToConsentQuery
import com.carelon.whe.WholeHealthQuery
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.LoginRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val client: ApolloClient,
    private val preferenceHelper: AppPreferenceHelper
) : LoginRepository {
    override suspend fun getAuthInfo(): ApolloResponse<WholeHealthQuery.Data> {
        return client.query(WholeHealthQuery()).execute()
    }

    override fun saveAuthToken(token: String) {
        preferenceHelper.accessToken = token
    }

    override fun getAuthToken(): String? {
        return preferenceHelper.accessToken
    }
}