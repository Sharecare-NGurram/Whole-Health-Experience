package com.carelon.whe.domain.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.GetProfileQuery

interface ProfileRepository {

    suspend fun getProfile(): ApolloResponse<GetProfileQuery.Data>

}