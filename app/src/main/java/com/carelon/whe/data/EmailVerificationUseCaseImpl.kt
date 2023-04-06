package com.carelon.whe.data

import com.carelon.whe.UpdateEmailMutation
import com.carelon.whe.ValidateEmailMutation
import com.carelon.whe.domain.repository.EmailVerificationRepository
import com.carelon.whe.domain.usecase.EmailVerificationUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EmailVerificationUseCaseImpl @Inject constructor(val repository: EmailVerificationRepository): EmailVerificationUseCase {
    override suspend fun verifyEmail(email: String): Flow<Resource<ValidateEmailMutation.Data?>> =
        flow {
            emit(Resource.Loading)
            try {
                val response = repository.verifyEmail(email)
                emit(Resource.Success(response.data))
            } catch (e: Exception) {
                emit(NetworkUtils.resolveError(e))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun updateEmail(
        otp: String,
        token: String,
        deviceId: String,
        userId: String,
        riskId: String
    ): Flow<Resource<UpdateEmailMutation.Data?>> = flow {
        emit(Resource.Loading)
        try {
            val response = repository.updateEmail(otp, token, deviceId, userId, riskId)
            emit(Resource.Success(response.data))
        }catch (e: Exception){
            emit(NetworkUtils.resolveError(e))
        }
    }.flowOn(Dispatchers.IO)
}