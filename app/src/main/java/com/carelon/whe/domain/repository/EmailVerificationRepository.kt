package com.carelon.whe.domain.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.UpdateEmailMutation
import com.carelon.whe.ValidateEmailMutation
import com.carelon.whe.network.Resource
import kotlinx.coroutines.flow.Flow

interface EmailVerificationRepository {

    suspend fun verifyEmail(email: String) : ApolloResponse<ValidateEmailMutation.Data>

    suspend fun updateEmail(otp: String, token: String, deviceId: String, userId: String, riskId: String) : ApolloResponse<UpdateEmailMutation.Data>

}