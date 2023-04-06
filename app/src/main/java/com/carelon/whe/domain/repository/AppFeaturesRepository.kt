package com.carelon.whe.domain.repository

import com.carelon.whe.domain.model.AppFeatureModel
import com.carelon.whe.util.PageRoutes

interface AppFeaturesRepository {

    fun getAppFeatures() : String

    fun saveAppFeatureSeen() : PageRoutes

    fun getCurrentRoute() : PageRoutes

    fun isAppFeaturesShown() : Boolean

}