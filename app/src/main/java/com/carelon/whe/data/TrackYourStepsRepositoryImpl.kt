package com.carelon.whe.data

import android.content.res.AssetManager
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.TrackStepActivityMutation
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.FocusAreasRepository
import com.carelon.whe.domain.repository.TrackYourStepsRepository
import com.carelon.whe.network.Resource
import com.carelon.whe.util.readAssetsFile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TrackYourStepsRepositoryImpl @Inject constructor(
    private val client: ApolloClient,
    private val preferenceHelper: AppPreferenceHelper
): TrackYourStepsRepository {


    override suspend fun trackStepActivity(): ApolloResponse<TrackStepActivityMutation.Data> {
        return client.mutation(TrackStepActivityMutation()).execute()
    }

    override fun getTrackStepActivity(): Int {
        return preferenceHelper.savedTrackStepActivity
    }

    override fun updateTrackStepActivity(isTrackStepActivityCompleted: Int) {
        preferenceHelper.savedTrackStepActivity = isTrackStepActivityCompleted
    }
}