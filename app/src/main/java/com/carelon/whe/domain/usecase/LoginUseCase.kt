package com.carelon.whe.domain.usecase

import com.carelon.whe.UserNeedsToConsentQuery
import com.carelon.whe.WholeHealthQuery
import com.carelon.whe.network.Resource
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {
    suspend fun getAuthInfo() : Flow<Resource<WholeHealthQuery.Anthem_auth_config?>>
    fun saveAuthToken(token: String)
    fun getAuthToken():String?
}