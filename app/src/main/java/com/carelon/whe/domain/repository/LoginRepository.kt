package com.carelon.whe.domain.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.UserNeedsToConsentQuery
import com.carelon.whe.WholeHealthQuery

interface LoginRepository {
    suspend fun getAuthInfo() : ApolloResponse<WholeHealthQuery.Data>
    fun saveAuthToken(token: String)

    fun getAuthToken():String?
}