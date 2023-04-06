package com.carelon.whe.data

import android.content.res.AssetManager
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.AppFeaturesRepository
import com.carelon.whe.util.PageRoutes
import com.carelon.whe.util.readAssetsFile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppFeaturesRepositoryImpl @Inject constructor(
    private val assetManager : AssetManager,
    private val preferenceHelper: AppPreferenceHelper
) : AppFeaturesRepository {


    /**
     * get app feature list. Currently this is static
     */
    override fun getAppFeatures(): String {
        return assetManager.readAssetsFile("app_features.json")
    }

    /**
     * save status in local device that user has seen app feature page
    * */
    override fun saveAppFeatureSeen(): PageRoutes {
        preferenceHelper.isFeaturePageShown = true
        return getCurrentRoute()
    }

    /**
     * This will provide the route to which we need current page to go
     * if user seen app feature page then next time directly go to dashboard
     * */
    override fun getCurrentRoute():PageRoutes{
        return if(!preferenceHelper.isFeaturePageShown){
            PageRoutes.AppFeatures
        }else{
            PageRoutes.Dashboard
        }
    }

    override fun isAppFeaturesShown(): Boolean = preferenceHelper.isFeaturePageShown
}