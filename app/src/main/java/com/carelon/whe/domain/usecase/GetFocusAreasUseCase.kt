package com.carelon.whe.domain.usecase

import com.carelon.whe.domain.model.FocusAreaItem
import com.carelon.whe.domain.repository.FocusAreasRepository
import com.carelon.whe.network.Resource
import com.carelon.whe.util.NetworkUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import javax.inject.Inject

/**
 * Use case class that return list of focus areas
 */

class GetFocusAreasUseCase @Inject constructor(
    private val repository: FocusAreasRepository,
    private val gson: Gson
) {

    suspend fun getFocusAreas() = flow {
        emit(Resource.Loading)
        try {
            val type = object : TypeToken<List<FocusAreaItem>>() {}.type
            val response  = gson.fromJson(JSONObject(repository.getYoursFocusAreas()).getJSONArray("data").toString(), type) as ArrayList<FocusAreaItem>
            emit(Resource.Success(data = response))
        } catch (e: Exception) {
            emit(NetworkUtils.resolveError(e))
        }
    }.flowOn(Dispatchers.IO)

}