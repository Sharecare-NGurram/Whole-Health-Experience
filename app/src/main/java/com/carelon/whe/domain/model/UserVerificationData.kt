package com.carelon.whe.domain.model

data class UserVerificationData(
    val userName:String,
    val emailVerified:Boolean,
    val isOnBoardingCompleted:Boolean,
)
