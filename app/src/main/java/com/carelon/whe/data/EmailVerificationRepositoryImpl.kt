package com.carelon.whe.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.UpdateEmailMutation
import com.carelon.whe.ValidateEmailMutation
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.EmailVerificationRepository
import com.carelon.whe.type.MutationInput_post_xpocare_v1_user_validateEmail_input_contactInfo_Input
import com.carelon.whe.type.email_const
import javax.inject.Inject

class EmailVerificationRepositoryImpl @Inject constructor(
    private val client: ApolloClient,
    private val preferenceHelper: AppPreferenceHelper
): EmailVerificationRepository {
    override suspend fun verifyEmail(email: String) : ApolloResponse<ValidateEmailMutation.Data> {
        return client.mutation(ValidateEmailMutation(
            MutationInput_post_xpocare_v1_user_validateEmail_input_contactInfo_Input(email_const.email, email)
        )).execute()
    }

    override suspend fun updateEmail(
        otp: String,
        token: String,
        deviceId: String,
        userId: String,
        riskId: String
    ): ApolloResponse<UpdateEmailMutation.Data> {
        return client.mutation(UpdateEmailMutation(otp, token, deviceId, userId, riskId)).execute()
    }
}