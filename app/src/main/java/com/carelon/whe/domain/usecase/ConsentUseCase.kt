package com.carelon.whe.domain.usecase

import com.carelon.whe.SaveConsentsMutation
import com.carelon.whe.UserNeedsToConsentQuery
import com.carelon.whe.network.Resource
import com.carelon.whe.type.mutationInput_submitConsentV2_input_consents_items_type
import com.carelon.whe.util.PageRoutes
import kotlinx.coroutines.flow.Flow

/**
 * Interface to handle all consent related functionalities
 */
interface ConsentUseCase {
    suspend fun getUserNeedsToConsent() : Flow<Resource<UserNeedsToConsentQuery.Data?>>
    suspend fun saveConsents(consent: UserNeedsToConsentQuery.NeedConsent, type: mutationInput_submitConsentV2_input_consents_items_type
    ) : Flow<Resource<SaveConsentsMutation.Data?>>
    fun getCurrentRoute(consentsList: List<UserNeedsToConsentQuery.NeedConsent?>) : PageRoutes
    fun setEarlyAccessPageShown()
    fun setPrivacyPolicyPageShown()
    fun clearConsents()
}