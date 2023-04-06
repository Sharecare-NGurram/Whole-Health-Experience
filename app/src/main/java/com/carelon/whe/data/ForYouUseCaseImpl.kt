package com.carelon.whe.data

import com.carelon.whe.domain.model.GeneralResponse
import com.carelon.whe.domain.model.MedImportAvailability
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.repository.ForYouRepository
import com.carelon.whe.domain.usecase.ForYouUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.NetworkUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import javax.inject.Inject

class ForYouUseCaseImpl @Inject constructor(private val repository: ForYouRepository):
    ForYouUseCase {

    //it will check if the meds available for user to import and if not hide the import feature
    override suspend fun checkMedImportAvailable(): Flow<Resource<MedImportAvailability?>> = flow {
        emit(Resource.Loading)
        try{
            val response = repository.getMedicine()
            if (response == null){
                emit(NetworkUtils.resolveError(NullPointerException()))
            }else{
                val responseObj = JSONObject(response)
                if (responseObj.has("medicines")){
                    val records = Gson().fromJson(responseObj.getJSONArray("medicines").toString(), Array<Medicine>::class.java).toList().size
                    emit(Resource.Success(data = MedImportAvailability(medsImportAvailable = records>0)))
                }else{
                    emit(Resource.Success(data = MedImportAvailability(medsImportAvailable = false)))
                }
            }
        } catch (e: Exception){
            emit(NetworkUtils.resolveError(e))
        }
    }.flowOn(Dispatchers.IO)

    //save user action to cancel meds import for now
    override suspend fun medsImportCanceled(isCancelled: Boolean): Flow<Resource<GeneralResponse?>> = flow<Resource<GeneralResponse?>> {
        val response = repository.medsImportCanceled()
        if (response == null){
            emit(NetworkUtils.resolveError(NullPointerException()))
        }else{
            emit(Resource.Success(data = GeneralResponse(success = true)))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getSavedMedications(): Flow<Resource<List<Medicine?>?>> =
        flow<Resource<List<Medicine?>?>> {
            emit(Resource.Loading)
            try {
                val response = repository.getSavedMedication()
                if (response == null){
                    emit(NetworkUtils.resolveError(NullPointerException()))
                }else{
                    emit(Resource.Success(data = response))
                }
            } catch (e: Exception) {
                emit(NetworkUtils.resolveError(e))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun removeSavedMeds(): Flow<Resource<GeneralResponse?>> = flow<Resource<GeneralResponse?>> {
        val response = repository.removeSavedMedication()
        emit(Resource.Success(data = GeneralResponse(success = true)))
    }.flowOn(Dispatchers.Default)
}