package com.carelon.whe.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.GetProfileQuery
import com.carelon.whe.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : ProfileRepository {

    override suspend fun getProfile(): ApolloResponse<GetProfileQuery.Data> {
        return apolloClient.query(
            GetProfileQuery()
        ).execute()
    }

}