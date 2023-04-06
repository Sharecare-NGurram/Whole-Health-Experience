package com.carelon.whe.data

import com.carelon.whe.MedicationListQuery
import com.carelon.whe.domain.model.Medicine
import com.carelon.whe.domain.repository.AddMedicationRepository
import com.carelon.whe.domain.usecase.AddMedicationUseCase
import com.carelon.whe.network.Resource
import com.carelon.whe.util.NetworkUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import javax.inject.Inject

class AddMedicationUseCaseImpl @Inject constructor(
    private val repository: AddMedicationRepository
) : AddMedicationUseCase {

    override fun getMedicineList(): Flow<Resource<MedicationListQuery.Data?>> =
        flow {
            emit(Resource.Loading)
            try {
                val response = repository.getMedicineToImport()
                emit(Resource.Success(response.data))
            } catch (e: Exception) {
                emit(NetworkUtils.resolveError(e))
            }
        }.flowOn(Dispatchers.IO)
}