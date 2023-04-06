package com.carelon.whe.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.carelon.whe.SaveConsentsMutation
import com.carelon.whe.UserNeedsToConsentQuery
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.ConsentRepository
import com.carelon.whe.type.ConsentV2Request_Input
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsentRepositoryImpl @Inject constructor(
    private val client: ApolloClient,
    private val preferenceHelper: AppPreferenceHelper
) : ConsentRepository {

    // Get consent that need to show from API
    override suspend fun getUserNeedsToConsent(): ApolloResponse<UserNeedsToConsentQuery.Data> {
        return client.query(UserNeedsToConsentQuery()).execute()
    }

    // Save early access consent to the API
    override suspend fun saveConsents(consent: List<ConsentV2Request_Input?>?): ApolloResponse<SaveConsentsMutation.Data> {
        return client.mutation(SaveConsentsMutation(Optional.present(consent))).execute()
    }

    // Get the early access shown or not value
    override fun isEarlyAccessShown(): Boolean = preferenceHelper.isEarlyAccessPageShown

    // Set the early access shown value
    override fun setEarlyAccessPageShown() {
        preferenceHelper.isEarlyAccessPageShown = true
    }

    override fun setPrivacyPolicyPageShown() {
        preferenceHelper.isPrivacyPolicyPageShown = true
    }

    override fun isPrivacyPolicyShown(): Boolean {
        return preferenceHelper.isPrivacyPolicyPageShown
    }

    override fun clearConsents() {
        preferenceHelper.isEarlyAccessPageShown = false
        preferenceHelper.isPrivacyPolicyPageShown = false
    }
}