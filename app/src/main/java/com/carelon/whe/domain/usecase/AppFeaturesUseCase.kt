package com.carelon.whe.domain.usecase

import com.carelon.whe.domain.model.AppFeatureModel
import com.carelon.whe.network.Resource
import com.carelon.whe.util.PageRoutes
import kotlinx.coroutines.flow.Flow

interface AppFeaturesUseCase {

    fun getAppFeatures() : Flow<Resource<List<AppFeatureModel?>?>>

    fun setAppFeatureSeen() : Flow<Resource<PageRoutes>>

    fun getCurrentRoute() : Flow<Resource<PageRoutes>>

    fun isAppFeaturesShown() : Boolean

}