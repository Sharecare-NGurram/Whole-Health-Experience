package com.carelon.whe.data

import com.carelon.whe.SaveConsentsMutation
import com.carelon.whe.UserNeedsToConsentQuery
import com.carelon.whe.domain.repository.ConsentRepository
import com.carelon.whe.domain.usecase.ConsentUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.type.ConsentV2Request_Input
import com.carelon.whe.type.mutationInput_submitConsentV2_input_consents_items_consent
import com.carelon.whe.type.mutationInput_submitConsentV2_input_consents_items_type
import com.carelon.whe.type.query_getContentV2_items_type
import com.carelon.whe.util.NetworkUtils
import com.carelon.whe.util.PageRoutes
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ConsentUseCaseImpl @Inject constructor(
    private val repository: ConsentRepository,
    private val gson: Gson
    ) : ConsentUseCase {

    // Get pending consents for the user
    override suspend fun getUserNeedsToConsent(): Flow<Resource<UserNeedsToConsentQuery.Data?>> =
        flow {
            emit(Resource.Loading)
            try {
                val data = repository.getUserNeedsToConsent()
                emit(Resource.Success(data.data))
            } catch (e: Exception) {
                emit(NetworkUtils.resolveError(e))
            }
        }.flowOn(Dispatchers.IO)

    // Save consent to cloud
    override suspend fun saveConsents(consent: UserNeedsToConsentQuery.NeedConsent, type: mutationInput_submitConsentV2_input_consents_items_type): Flow<Resource<SaveConsentsMutation.Data?>> =
        flow {
            emit(Resource.Loading)
            try {
                val consentId = consent.contentId ?: ""
                val consentInput = ConsentV2Request_Input(
                    languageCode = "en",
                    contentId = consentId,
                    type = type,
                    consent = mutationInput_submitConsentV2_input_consents_items_consent.YES
                )
                val requestInput = listOf(consentInput)
                val response = repository.saveConsents(requestInput)
                emit(Resource.Success(response.data))
            } catch (e: Exception) {
                emit(NetworkUtils.resolveError(e))
            }
        }

    /**
     * Check any pending consents
     * Base on consent set the page route for redirection
     */
    override fun getCurrentRoute(
        consentsList: List<UserNeedsToConsentQuery.NeedConsent?>
    ): PageRoutes {
        for (i in consentsList.indices) {
            if (consentsList[i]?.type == query_getContentV2_items_type.EARLY_ACCESS && !repository.isEarlyAccessShown()) {
                return PageRoutes.EarlyAccess(gson.toJson(consentsList[i]))
            } else if (consentsList[i]?.type == query_getContentV2_items_type.PRIVACY && !repository.isPrivacyPolicyShown()) {
                return PageRoutes.PrivacyPolicy(gson.toJson(consentsList[i]))
            }
        }
        return PageRoutes.Dashboard
    }

    override fun setEarlyAccessPageShown() {
        repository.setEarlyAccessPageShown()
    }

    override fun setPrivacyPolicyPageShown() {
        repository.setPrivacyPolicyPageShown()
    }

    override fun clearConsents() {
        repository.clearConsents()
    }
}