package com.carelon.whe.domain.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.TrackStepActivityMutation

interface TrackYourStepsRepository {

    suspend fun trackStepActivity(): ApolloResponse<TrackStepActivityMutation.Data>

    fun getTrackStepActivity(): Int

    fun updateTrackStepActivity(isTrackStepActivityCompleted: Int)

}