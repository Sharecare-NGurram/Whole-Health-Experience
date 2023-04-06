package com.carelon.whe.domain.usecase

import com.carelon.whe.GetProfileQuery
import com.carelon.whe.domain.model.UserVerificationData
import com.carelon.whe.network.Resource
import kotlinx.coroutines.flow.Flow

interface UserProfileUseCase {

    suspend fun getUserData() : Flow<Resource<UserVerificationData?>>

    fun setEmailVerified()

    fun isEmailVerified():Boolean

}