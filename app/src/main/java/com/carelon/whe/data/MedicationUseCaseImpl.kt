package com.carelon.whe.data

import com.carelon.whe.domain.model.GeneralResponse
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.model.MedicineReminder
import com.carelon.whe.domain.prefs.AppPreferenceHelper
import com.carelon.whe.domain.repository.MedicationRepository
import com.carelon.whe.domain.repository.MedicineReminderRepository
import com.carelon.whe.domain.usecase.MedicationUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MedicationUseCaseImpl @Inject constructor(
    private val repository: MedicationRepository
) : MedicationUseCase {
    override suspend fun saveSelectedMeds(selectedMeds: String?): Flow<Resource<GeneralResponse?>> = flow<Resource<GeneralResponse?>> {
        val response = repository.saveSelectedMedication(selectedMeds)
        if (response == null){
            emit(NetworkUtils.resolveError(NullPointerException()))
        }else {
            emit(Resource.Success(data = GeneralResponse(success = true)))
        }
    }.flowOn(Dispatchers.Default)

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

    override suspend fun getNewPrescriptionAvailable(): Flow<Resource<List<Medicine?>?>> =
        flow<Resource<List<Medicine?>?>> {
            emit(Resource.Loading)
            try {
                val response = repository.getNewPrescription()
                val responseSaved = repository.getSavedMedication()
                if (response == null || responseSaved==null){
                    emit(NetworkUtils.resolveError(NullPointerException()))
                }else{
                    val iterator = response.iterator()
                    while (iterator.hasNext()){
                        val data = iterator.next()
                        for (rd in responseSaved){
                            if(rd.id==data.id){
                                iterator.remove()
                            }
                        }
                    }
                    emit(Resource.Success(data = response))
                }
            } catch (e: Exception) {
                emit(NetworkUtils.resolveError(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun markedNewPrescriptionRead() {
       repository.markedNewPrescriptionRead()
    }

}