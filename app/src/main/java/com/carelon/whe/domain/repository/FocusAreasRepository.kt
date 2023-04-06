package com.carelon.whe.domain.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.TrackStepActivityMutation

interface FocusAreasRepository {

    suspend fun getYoursFocusAreas(): String
}