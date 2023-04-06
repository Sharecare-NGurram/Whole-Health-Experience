package com.carelon.whe.data

import com.carelon.whe.UserNeedsToConsentQuery
import com.carelon.whe.WholeHealthQuery
import com.carelon.whe.domain.repository.LoginRepository
import com.carelon.whe.domain.usecase.LoginUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(private val repository: LoginRepository) : LoginUseCase {
    override suspend fun getAuthInfo(): Flow<Resource<WholeHealthQuery.Anthem_auth_config?>> = flow {
        emit(Resource.Loading)
        try {
            val data = repository.getAuthInfo()
            emit(Resource.Success(data.data?.anthem_auth_config))
        } catch (e: Exception) {
            emit(NetworkUtils.resolveError(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun saveAuthToken(token: String) {
        repository.saveAuthToken(token)
    }

    override fun getAuthToken(): String? {
        return repository.getAuthToken()
    }
}