package com.carelon.whe.data

import android.content.res.AssetManager
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.carelon.whe.TrackStepActivityMutation
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.FocusAreasRepository
import com.carelon.whe.network.Resource
import com.carelon.whe.util.readAssetsFile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FocusAreasRepositoryImpl @Inject constructor(
    private val assetManager : AssetManager
): FocusAreasRepository {

    override suspend fun getYoursFocusAreas(): String {
        return assetManager.readAssetsFile("pick_focus_areas.json")
    }
}