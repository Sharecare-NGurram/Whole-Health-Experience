package com.carelon.whe.domain.usecase

import com.carelon.whe.domain.repository.TrackYourStepsRepository
import com.carelon.whe.network.Resource
import com.carelon.whe.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TrackYourStepsUseCase @Inject constructor(private val repository: TrackYourStepsRepository,){

    suspend fun trackStepActivity () = flow {
        emit(Resource.Loading)
        try {
            val response = repository.trackStepActivity()
            emit(Resource.Success(data = response.data))
        } catch (e: Exception){
            emit(NetworkUtils.resolveError(e))
        }
    }.flowOn(Dispatchers.IO)

    fun getTrackStepActivity() = repository.getTrackStepActivity()

    fun updateTrackStepActivity(isTrackStepActivityCompleted: Int){
        repository.updateTrackStepActivity(isTrackStepActivityCompleted)
    }

}