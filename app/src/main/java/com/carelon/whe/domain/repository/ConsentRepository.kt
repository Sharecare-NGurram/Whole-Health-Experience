package com.carelon.whe.domain.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.SaveConsentsMutation
import com.carelon.whe.UserNeedsToConsentQuery
import com.carelon.whe.WholeHealthQuery
import com.carelon.whe.network.Resource
import com.carelon.whe.type.ConsentV2Request_Input
import kotlinx.coroutines.flow.Flow

/**
 * Interface to perform all consent related functionalities
 */
interface ConsentRepository {
    suspend fun getUserNeedsToConsent(): ApolloResponse<UserNeedsToConsentQuery.Data>
    suspend fun saveConsents(consent: List<ConsentV2Request_Input?>?): ApolloResponse<SaveConsentsMutation.Data>
    fun isEarlyAccessShown(): Boolean
    fun setEarlyAccessPageShown()
    fun setPrivacyPolicyPageShown()
    fun isPrivacyPolicyShown(): Boolean
    fun clearConsents()
}