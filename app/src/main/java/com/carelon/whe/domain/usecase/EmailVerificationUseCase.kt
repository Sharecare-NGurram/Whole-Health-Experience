package com.carelon.whe.domain.usecase

import com.carelon.whe.UpdateEmailMutation
import com.carelon.whe.ValidateEmailMutation
import com.carelon.whe.network.Resource
import kotlinx.coroutines.flow.Flow

interface EmailVerificationUseCase {

    suspend fun verifyEmail(email: String) : Flow<Resource<ValidateEmailMutation.Data?>>

    suspend fun updateEmail(otp: String, token: String, deviceId: String, userId: String, riskId: String) : Flow<Resource<UpdateEmailMutation.Data?>>

}