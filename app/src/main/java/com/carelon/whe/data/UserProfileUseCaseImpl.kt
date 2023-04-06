package com.carelon.whe.data

import com.carelon.whe.GetProfileQuery
import com.carelon.whe.domain.model.ErrorItem
import com.carelon.whe.domain.model.UserVerificationData
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.ProfileRepository
import com.carelon.whe.domain.usecase.UserProfileUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserProfileUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val preferenceHelper: AppPreferenceHelper
) : UserProfileUseCase {

    override suspend fun getUserData(): Flow<Resource<UserVerificationData?>> = flow {
        emit(Resource.Loading)
        try {
            val userData = profileRepository.getProfile().data?.xpoProfile?.userProfile
            //val emailVerified = ((userData?.email?.firstOrNull { it.isPrimary == true }?.isMfaValidated ?: false)||isEmailVerified())
            val emailVerified = isEmailVerified()
            val name = userData?.firstName?:""
            emit(Resource.Success(
                UserVerificationData(
                    userName = name,
                    emailVerified = emailVerified,
                    isOnBoardingCompleted = false
                )
            ))
        } catch (e: Exception) {
            emit(NetworkUtils.resolveError(e))
        }
    }

    override fun setEmailVerified() {
        preferenceHelper.isEmailVerified = true
    }

    override fun isEmailVerified():Boolean{
        return preferenceHelper.isEmailVerified
    }

}