package com.carelon.whe.data

import com.carelon.whe.domain.model.AppFeatureModel
import com.carelon.whe.domain.repository.AppFeaturesRepository
import com.carelon.whe.domain.usecase.AppFeaturesUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.NetworkUtils
import com.carelon.whe.util.PageRoutes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import javax.inject.Inject

class AppFeaturesUseCaseImpl @Inject constructor(
    val repository: AppFeaturesRepository,
    val gson: Gson
) : AppFeaturesUseCase {

    override fun getAppFeatures(): Flow<Resource<List<AppFeatureModel?>?>> =
    flow<Resource<List<AppFeatureModel?>?>> {
        emit(Resource.Loading)
        try {
            val type = object : TypeToken<List<AppFeatureModel>>() {}.type
            val response =  gson.fromJson(JSONObject(repository.getAppFeatures()).getJSONArray("features").toString(), type) as ArrayList<AppFeatureModel>
            emit(Resource.Success(data = response))
        } catch (e: Exception) {
            emit(NetworkUtils.resolveError(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun setAppFeatureSeen(): Flow<Resource<PageRoutes>> =
        flow {
            emit(Resource.Loading)
            try {
                val response = repository.saveAppFeatureSeen()
                emit(Resource.Success(data = response))
            } catch (e: Exception) {
                emit(NetworkUtils.resolveError(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun getCurrentRoute(): Flow<Resource<PageRoutes>> =
        flow<Resource<PageRoutes>> {
            emit(Resource.Loading)
            try {
                val response = repository.getCurrentRoute()
                emit(Resource.Success(data = response))
            } catch (e: Exception) {
                emit(NetworkUtils.resolveError(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun isAppFeaturesShown(): Boolean = repository.isAppFeaturesShown()

}